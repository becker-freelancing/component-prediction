FROM eclipse-temurin:17-jdk-jammy


WORKDIR /app

ARG JAR_FILE
ARG LIBS_DIR

COPY ${JAR_FILE} app.jar
COPY ${LIBS_DIR} libs/

CMD ["java", "-cp", "app.jar:libs/*", "com.becker.freelance.component.prediction.EmbeddingServiceStarter"]