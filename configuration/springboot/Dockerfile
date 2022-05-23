FROM amazoncorretto:11.0.9
EXPOSE 80
ARG jarFile=target/*.jar
COPY ${jarFile} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
