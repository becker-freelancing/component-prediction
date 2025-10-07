FROM openjdk:17-jdk-slim

WORKDIR /app

ARG JAR_FILE
ARG LIBS_DIR

COPY ${JAR_FILE} app.jar
COPY ${LIBS_DIR} libs/

CMD ["java", "-cp", "app.jar:libs/*", "com.becker.freelance.component.prediction.language.detection.LanguageDetectionStarter"]