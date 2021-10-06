FROM openjdk:11
ARG jarFile=target/*.jar
COPY ${jarFile} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
