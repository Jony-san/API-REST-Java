# API-REST-Java
API REST desarrollada en Java (JDK 17) con Spring Boot y Maven para la gestión de usuarios, que incluye ordenamiento, filtrado dinámico, autenticación, encriptación AES256 de contraseñas y validaciones de RFC y teléfono, utilizando almacenamiento en memoria.

API REST desarrollada con Spring Boot 3 + Java 17 para la gestión de usuarios.

El proyecto implementa:

CRUD completo de usuarios

Filtros y ordenamiento

Validaciones personalizadas (RFC y teléfono)

Encriptación AES-256

Endpoint de login

Manejo global de excepciones

Documentación automática con Swagger

Dockerización

Tecnologías utilizadas
------------------------------------------

Java 17 (Temurin)

Spring Boot 3

Maven

H2 Database (en memoria)

Spring Data JPA

Spring Validation

Swagger / OpenAPI

Docker

Modo de ejecución (Local)
------------------------------------------
1️⃣ Requisitos

Java 17

Maven 3.6x

Docker (opcional)

NOTA:
El proyecto incluye Maven Wrapper. 
Puedes correr:
```
./mvnw clean install
```

Clonar el repositorio y ubicarse dentro de la carpeta users-api
```
git clone https://github.com/TU_USUARIO/users-api.git
cd users-api
```
Compilar
```
mvn clean install
```
Ejecutar la aplicación
```
mvn spring-boot:run
```
La API quedará disponible en:   http://localhost:8080

Documentación Swagger
------------------------------------------

Una vez levantada la aplicación:

http://localhost:8080/swagger-ui/index.html

Desde ahí se pueden probar todos los endpoints directamente.

Encriptación AES
------------------------------------------

Se implementó encriptación AES-256 para el manejo de información sensible.

Se dejaron tres variantes del manejo de clave, con fines demostrativos:

Versión actual (recomendada)

Uso de variables de entorno.

Permite configurar la clave sin exponerla en código.

Versión con clave hardcodeada

Útil únicamente para pruebas locales rápidas.

No recomendada para producción.

Versión usando variables de entorno del sistema directamente.

Estas versiones están organizadas dentro del servicio correspondiente para mostrar diferentes enfoques de configuración segura.

Hash (comentado)
------------------------------------------

Se dejó implementada la lógica para aplicar hash a contraseñas, pero actualmente está comentada.

La intención fue mostrar cómo podría integrarse hashing (por ejemplo con BCrypt) en un entorno real, aunque para esta prueba no era un requerimiento obligatorio.

Ejecución con Docker
------------------------------------------
Construir imagen

Desde la raíz del proyecto:
```
docker build -t users-api .
```
Ejecutar contenedor
```
docker run -p 8080:8080 users-api
```
Luego acceder a:
```
http://localhost:8080/swagger-ui/index.html
```

Base de datos
------------------------------------------

Se utiliza H2 en memoria para facilitar la ejecución sin configuraciones adicionales.

Cada vez que se reinicia la aplicación, los datos se reinician.

Endpoints principales
------------------------------------------
GET /users

POST /users

PATCH /users/{id}

DELETE /users/{id}

POST /users/login

Todos documentados y probables desde Swagger.

Control de versiones
------------------------------------------
El proyecto fue gestionado mediante Git, manteniendo un repositorio desde el inicio del desarrollo para dar seguimiento incremental a los cambios.

Se realizaron commits por funcionalidad (estructura base, entidades, validaciones, seguridad, Docker, etc.) con el objetivo de mantener trazabilidad y claridad en la evolución del código.

## License
This project is licensed under the MIT License.