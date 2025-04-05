FROM maven:3.8.3-amazoncorretto-17

WORKDIR /app

# 1. Copia o POM pai e instala
COPY pom.xml ./parent-pom.xml
RUN mvn install:install-file \
    -Dfile=parent-pom.xml \
    -DgroupId=com.mygradhub \
    -DartifactId=mygradhub-parent \
    -Dversion=0.0.1-SNAPSHOT \
    -Dpackaging=pom

# 2. Copia o código fonte
COPY mygradhub-auth/pom.xml .
COPY mygradhub-auth/src ./src

# 3. Executa diretamente via Spring Boot Maven Plugin
CMD ["mvn", "spring-boot:run", "-Dspring-boot.run.profiles=default"]