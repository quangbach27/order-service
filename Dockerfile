# Stage 1: Build and extract the layers from the JAR file
FROM eclipse-temurin:21 AS builder
WORKDIR /workspace
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} order-service.jar
RUN java -Djarmode=layertools -jar order-service.jar extract

# Stage 2: Create the final runtime image
FROM eclipse-temurin:21-jre
RUN useradd spring
USER spring
WORKDIR /workspace
COPY --from=builder /workspace/dependencies/ ./
COPY --from=builder /workspace/spring-boot-loader/ ./
COPY --from=builder /workspace/snapshot-dependencies/ ./
COPY --from=builder /workspace/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]