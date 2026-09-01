FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/student-manager-1.0.0.jar app.jar
EXPOSE 8082
HEALTHCHECK --interval=30s --timeout=3s \
  CMD wget -qO- http://localhost:8082/api/students/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]