# CLAI website image. Source of truth for production (Phase 3, Nov 2026); until the automated deploy ships it,
# the server still builds from its own copy in /opt/docker (eclipse-temurin:17-jdk-jammy, same layout).
#
# Production (in /opt/docker, where Jenkins puts the WAR):  docker build -t clai-website:<tag> .
# Locally (repo root, after ./mvnw package):                 docker build --build-arg WAR=target/clai-website.war -t clai-website:local .
# Run:  docker run -d --name clai-website --restart unless-stopped -p 127.0.0.1:9090:8080 \
#         -e SPRING_PROFILES_ACTIVE=prod -v /opt/docker/config:/config clai-website:<tag>
FROM eclipse-temurin:17-jre-jammy

ARG WAR=clai-website.war

# Run as an unprivileged user instead of root.
RUN useradd --system --no-create-home --shell /usr/sbin/nologin clai
WORKDIR /app
COPY --chown=clai:clai ${WAR} /app/clai-website.war
USER clai

EXPOSE 8080
# The EC2 host has 1 GiB RAM and no swap: cap the heap so the JVM leaves room for the OS, nginx and Docker.
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=50 -XX:+ExitOnOutOfMemoryError"
ENTRYPOINT ["java", "-jar", "/app/clai-website.war"]
