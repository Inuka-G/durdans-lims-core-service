# Build stage
FROM gradle:8-jdk21 AS builder

WORKDIR /app

# Copy project files
COPY . .

# Build the application
RUN gradle clean build -x test

# Runtime stage
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copy the built JAR from builder
COPY --from=builder /app/lims-core-service-app/build/libs/lims-core-service.jar .

# Expose the application port
EXPOSE 11000

# Environment variables updated for Docker Compose networking
ENV DB_URL=jdbc:postgresql://postgres-db:5435/lims_db \
    DB_USERNAME=postgres \
    DB_PASSWORD=postgres \
    AWS_REGION=us-east-1 \
    AWS_ENDPOINT=http://localstack:4566 \
    AWS_S3_BUCKET=lims-patient-documents \
    AWS_ACCESS_KEY=test \
    AWS_SECRET_KEY=test \
    KAFKA_BOOTSTRAP_SERVERS=kafka:9092 \
    KEYCLOAK_ISSUER_URI=http://host.docker.internal:8081/realms/lims-realm \
    MAIL_HOST=smtp.gmail.com \
    MAIL_PORT=587

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:11000/actuator/health || exit 1

# Run the application
CMD ["java", "-jar", "lims-core-service.jar"]