FROM openjdk:11
WORKDIR /app
COPY target/my_auction-0.0.1-SNAPSHOT.jar ./my_auction-0.0.1.jar
ENTRYPOINT [ "java", "-jar", "./my_auction-0.0.1.jar" ]