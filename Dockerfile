#Build
FROM maven:3.9.9-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY . /app/shoppe-be
# RUN mvn package -f /app/shoppe-be/pom.xml
RUN mvn package -Dspring.profiles.active=docker -DskipTests -f /app/shoppe-be/pom.xml

#multi-staging
FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/shoppe-be/target/shoppe-be-1.0.0.jar app.jar
#COPY --from=build /app/shoppe-be/uploads uploads

EXPOSE 4006
CMD ["java","-jar","app.jar"]