FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} document-validator-1.0.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/document-validator-1.0.0-SNAPSHOT.jar"]