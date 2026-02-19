# 1️⃣ Imagen base
FROM eclipse-temurin:17-jdk

# 2️⃣ Directorio dentro del contenedor
WORKDIR /app

# 3️⃣ Copiar pom y código
COPY pom.xml .
COPY src ./src

# 4️⃣ Instalar Maven dentro del contenedor y compilar
RUN apt-get update && apt-get install -y maven \
    && mvn clean package -DskipTests

# 5️⃣ Exponer puerto
EXPOSE 8080

# 6️⃣ Ejecutar la app
CMD ["java", "-jar", "target/users-api-0.0.1-SNAPSHOT.jar"]
