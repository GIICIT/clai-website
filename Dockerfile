FROM openjdk:17-jdk-alpine
COPY target/clai-webapp.war clai-webapp.war
ENTRYPOINT ["java","-jar","/clai-webapp.war"]