FROM maven:3.8.3-amazoncorretto-17

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

CMD ["mvn", "spring-boot:run", "-Dspring-boot.run.profiles=default"]