FROM eclipse-temurin:17-jdk
WORKDIR /app
ADD https://repo1.maven.org/maven2/org/mongodb/mongodb-driver-sync/5.2.0/mongodb-driver-sync-5.2.0.jar /app/lib/
ADD https://repo1.maven.org/maven2/org/mongodb/mongodb-driver-core/5.2.0/mongodb-driver-core-5.2.0.jar /app/lib/
ADD https://repo1.maven.org/maven2/org/mongodb/bson/5.2.0/bson-5.2.0.jar /app/lib/
ADD https://repo1.maven.org/maven2/org/mongodb/bson-record-codec/5.2.0/bson-record-codec-5.2.0.jar /app/lib/
COPY src ./src
RUN mkdir out && javac -cp "lib/*" -d out src/*.java
CMD ["java", "-cp", "out:lib/*", "WebApp"]
