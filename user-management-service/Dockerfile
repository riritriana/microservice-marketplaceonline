# Gunakan base image Java 17 yang ringan
FROM openjdk:17-jdk-slim

# Tentukan argumen untuk mengambil file .jar dari hasil build Maven
ARG JAR_FILE=target/*.jar

# Salin file .jar tersebut ke dalam image dengan nama app.jar
COPY ${JAR_FILE} app.jar

# Perintah yang akan dijalankan saat container dimulai
ENTRYPOINT ["java","-jar","/app.jar"]