FROM maven:3.8.3-amazoncorretto-17 AS build
WORKDIR /app

COPY pom.xml ./parent-pom.xml

RUN mvn install:install-file \
    -Dfile=parent-pom.xml \
    -DgroupId=com.mygradhub \
    -DartifactId=mygradhub-parent \
    -Dversion=0.0.1-SNAPSHOT \
    -Dpackaging=pom

COPY mygradhub-auth/pom.xml .
COPY mygradhub-auth/src ./src

RUN mvn clean package -DskipTests

FROM amazoncorretto:17-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar ./app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]