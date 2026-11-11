#!/usr/bin/env bash
# Deploys the CLAI website on the EC2 host. Run by Jenkins (Publish over SSH) after it has copied
# clai-website.war, Dockerfile and this script into /opt/docker:
#
#     bash /opt/docker/deploy.sh <build-tag>        e.g. bash /opt/docker/deploy.sh 42   (Jenkins $BUILD_NUMBER)
#
# Steps: pre-flight checks -> build the new image while the old site keeps running -> swap containers ->
# health check -> on failure roll back to the previous image and exit 1 (the Jenkins build fails) ->
# on success keep the last KEEP_IMAGES images and remove older ones.
# Downtime is only the swap + app start (~5-10 s). The user running it needs docker access.
#
# Every setting can be overridden through the environment (used for local testing on Docker Desktop):
#   BASE_DIR      folder with the WAR, Dockerfile and config        (default /opt/docker)
#   CONFIG_DIR    mounted read-only as /config (smtp.properties)    (default $BASE_DIR/config)
#   HOST_PORT     nginx proxies to 127.0.0.1:HOST_PORT              (default 9090)
#   NAME / IMAGE  container name / image repository                 (default clai-website)
#   PROFILE       Spring profile                                    (default prod)
#   HEALTH_PATHS  pages that must return 200                        (default "/ /toxin")
#   HEALTH_TIMEOUT seconds to wait for them                          (default 90)
#   KEEP_IMAGES   newest images to keep for rollback                (default 3)
#   MIN_FREE_MB   free disk needed before building                  (default 1500)
set -euo pipefail

TAG="${1:-$(date +%Y%m%d-%H%M%S)}"
BASE_DIR="${BASE_DIR:-/opt/docker}"
CONFIG_DIR="${CONFIG_DIR:-$BASE_DIR/config}"
HOST_PORT="${HOST_PORT:-9090}"
NAME="${NAME:-clai-website}"
IMAGE="${IMAGE:-clai-website}"
PROFILE="${PROFILE:-prod}"
HEALTH_PATHS="${HEALTH_PATHS:-/ /toxin}"
HEALTH_TIMEOUT="${HEALTH_TIMEOUT:-90}"
KEEP_IMAGES="${KEEP_IMAGES:-3}"
MIN_FREE_MB="${MIN_FREE_MB:-1500}"

log() { printf '[deploy %s] %s\n' "$(date +%H:%M:%S)" "$*"; }
fail() { log "FAILED: $*"; exit 1; }

run_container() {  # $1 = image:tag
  docker run -d --name "$NAME" --restart unless-stopped \
    -p "127.0.0.1:${HOST_PORT}:8080" \
    -e "SPRING_PROFILES_ACTIVE=${PROFILE}" \
    -v "${CONFIG_DIR}:/config:ro" \
    "$1" >/dev/null
}

healthy() {  # every HEALTH_PATH returns 200 within HEALTH_TIMEOUT seconds
  local deadline=$((SECONDS + HEALTH_TIMEOUT)) path code
  while [ "$SECONDS" -lt "$deadline" ]; do
    local ok=1
    for path in $HEALTH_PATHS; do
      code=$(curl -s -o /dev/null -m 5 -w '%{http_code}' "http://127.0.0.1:${HOST_PORT}${path}" || true)
      [ "$code" = "200" ] || { ok=0; break; }
    done
    [ "$ok" = 1 ] && return 0
    sleep 2
  done
  return 1
}

cd "$BASE_DIR" || fail "no folder $BASE_DIR"

# ---- 1. pre-flight
[ -s clai-website.war ] || fail "clai-website.war missing or empty in $BASE_DIR"
[ -f Dockerfile ] || fail "Dockerfile missing in $BASE_DIR"
[ -f "$CONFIG_DIR/smtp.properties" ] || fail "$CONFIG_DIR/smtp.properties missing"
docker info >/dev/null 2>&1 || fail "cannot talk to docker (is it running, and may this user use it?)"
docker builder prune -f >/dev/null 2>&1 || true   # build cache filled the 8 GB disk before (Nov 2026)
free_mb=$(df -Pm "$BASE_DIR" | awk 'NR==2 {print $4}')
[ "${free_mb:-0}" -ge "$MIN_FREE_MB" ] || fail "only ${free_mb} MB free on disk, need ${MIN_FREE_MB} MB"
log "deploying ${IMAGE}:${TAG} (disk free ${free_mb} MB)"

# ---- 2. build while the current site keeps running
docker build -q -t "${IMAGE}:${TAG}" . >/dev/null || fail "docker build failed (site unchanged)"
log "image built: ${IMAGE}:${TAG}"

# ---- 3. swap
previous=$(docker inspect -f '{{.Config.Image}}' "$NAME" 2>/dev/null || true)
log "previous image: ${previous:-none}"
if [ -n "$previous" ]; then
  docker stop -t 20 "$NAME" >/dev/null || true
  docker rm "$NAME" >/dev/null || true
fi
run_container "${IMAGE}:${TAG}" || fail "docker run failed"

# ---- 4. health check, 5. rollback
if healthy; then
  log "healthy: ${HEALTH_PATHS} return 200"
else
  log "health check failed; last log lines of the new container:"
  docker logs --tail 40 "$NAME" 2>&1 | sed 's/^/    /' || true
  docker rm -f "$NAME" >/dev/null 2>&1 || true
  docker rmi "${IMAGE}:${TAG}" >/dev/null 2>&1 || true   # never keep a failed release as a rollback candidate
  if [ -n "$previous" ]; then
    log "rolling back to ${previous}"
    run_container "$previous" && healthy && log "rollback OK: site is back on ${previous}" \
      || log "ROLLBACK ALSO FAILED: site is down, check 'docker ps -a' and 'docker logs ${NAME}' on the server"
  fi
  fail "new release ${IMAGE}:${TAG} did not become healthy"
fi

# ---- 6. tidy up: keep the newest KEEP_IMAGES images of this repository (the running one always stays)
docker tag "${IMAGE}:${TAG}" "${IMAGE}:current"
docker images "$IMAGE" --format '{{.CreatedAt}}|{{.Repository}}:{{.Tag}}' | grep -v ':current$' | sort -r \
  | tail -n +"$((KEEP_IMAGES + 1))" | cut -d'|' -f2 | while read -r old; do
    [ "$old" = "${IMAGE}:${TAG}" ] && continue
    docker rmi "$old" >/dev/null 2>&1 && log "removed old image $old" || true
  done
docker image prune -f >/dev/null 2>&1 || true   # dangling layers only, never tagged rollback images
log "done: ${NAME} runs ${IMAGE}:${TAG}; images kept: $(docker images "$IMAGE" --format '{{.Tag}}' | tr '\n' ' ')"
