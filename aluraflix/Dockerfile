FROM adoptopenjdk/openjdk11:latest
RUN adduser --system --group spring
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} aluraflix.jar
ENTRYPOINT ["java","-jar","/aluraflix.jar"]
