FROM openjdk:17-jdk-alpine
COPY target/clai-website.war clai-website.war
ENTRYPOINT ["java","-jar","/clai-website.war"]