# UserService

## Overview
UserService is a Spring Boot application that provides user management services. It integrates with a MySQL database and uses Spring Security for authentication and authorization. The application also registers itself with a Eureka server for service discovery.

## Prerequisites
- Java 17
- Maven
- MySQL

## Setup

### Database Configuration
Ensure you have a MySQL database running and update the `application.properties` file with your database credentials:
```ini
spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:3306/userservice
spring.datasource.username=root
spring.datasource.password=password
```

### Build and Run
1. Clone the repository:
    ```sh
    git clone <repository-url>
    cd UserService
    ```

2. Build the project using Maven:
    ```sh
    mvn clean install
    ```

3. Run the application:
    ```sh
    mvn spring-boot:run
    ```

## Configuration
The application can be configured using the `application.properties` file located in the `src/main/resources` directory. Key configurations include:

- **Database Configuration**:
    ```ini
    spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:3306/userservice
    spring.datasource.username=root
    spring.datasource.password=password
    ```

- **Eureka Client Configuration**:
    ```ini
    eureka.client.registerWithEureka=true
    eureka.client.fetchRegistry=true
    eureka.client.service-url.defaultZone=http://localhost:8761/eureka
    ```

## Testing
The project includes unit tests that can be run using:
```sh
mvn test
```

## Dependencies
Key dependencies used in this project include:
- Spring Boot Starter Data JPA
- Spring Boot Starter Web
- Spring Boot Starter Security
- Spring Boot Starter OAuth2 Authorization Server
- Spring Cloud Starter Netflix Eureka Client
- MySQL Connector
- Flyway Core

Refer to the `pom.xml` file for the complete list of dependencies.

## License
This project is licensed under the MIT License. See the `LICENSE` file for details.
