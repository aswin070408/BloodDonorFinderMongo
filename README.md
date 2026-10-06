# Blood Donor Finder - Web Version (Java + MongoDB)

Pure Java web server (no Tomcat). Needs JDK 17+ and MongoDB (local or Atlas cloud).

## Run on your laptop
1. Get MongoDB: install MongoDB Community Server locally, OR create a free MongoDB Atlas cluster.
2. Download the driver jars into a `lib` folder (PowerShell, inside the project folder):
       mkdir lib
       $b="https://repo1.maven.org/maven2/org/mongodb"
       iwr "$b/mongodb-driver-sync/5.2.0/mongodb-driver-sync-5.2.0.jar" -OutFile lib\mongodb-driver-sync-5.2.0.jar
       iwr "$b/mongodb-driver-core/5.2.0/mongodb-driver-core-5.2.0.jar" -OutFile lib\mongodb-driver-core-5.2.0.jar
       iwr "$b/bson/5.2.0/bson-5.2.0.jar" -OutFile lib\bson-5.2.0.jar
       iwr "$b/bson-record-codec/5.2.0/bson-record-codec-5.2.0.jar" -OutFile lib\bson-record-codec-5.2.0.jar
3. If you use Atlas, set your connection string in the terminal (local MongoDB needs nothing):
       $env:MONGO_URI="mongodb+srv://USER:PASSWORD@cluster0.xxxx.mongodb.net/?retryWrites=true&w=majority"
4. Compile and run:
       javac -cp "lib/*" -d out src/*.java
       java -cp "out;lib/*" WebApp          (Windows)
       java -cp "out:lib/*" WebApp          (Mac/Linux)
5. Open http://localhost:8080  - admin login: admin / admin123

## Deploy on Render
1. Push to GitHub.
2. Render > New > Web Service > your repo > Language: Docker > Free.
3. Environment variables:
       MONGO_URI        your Atlas connection string
       ADMIN_PASSWORD   a password you choose
       MONGO_DB         (optional) database name, default blood_donor_web
4. Deploy. Collections and the admin account are created automatically.

## Files (src/)
WebApp, Views, DonorDAO, AdminDAO, Donor, ExportService, PasswordUtil, Schema, MongoConnection

## Collections
admins: username, passwordHash
donors: username, passwordHash, name, age, gender, bloodGroup, city, phone, lastDonation, createdAt
