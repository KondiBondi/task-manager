# Task Manager

Task Manager is a Spring Boot-based application designed to manage users and tasks efficiently. It uses a SQL database for data persistence and Gradle for build automation.

## Prerequisites

- Java 11 or higher
- Gradle 8.13 or higher
- A SQL database (e.g., MS SQL etc.)

## Configuration

Before running the application, ensure you configure the database connection in the `application.yml` or `application-local.yml` file. Update the following properties with your database credentials:

```properties
spring.datasource.url=jdbc:<your-database-url>
spring.datasource.username=<your-database-username>
spring.datasource.password=<your-database-password>
```

Replace <your-database-url>, <your-database-username>, and <your-database-password> with the correct values for your database.


## Useful Gradle Commands
Here are some useful Gradle commands for building and running the project:
- **Build the project**:
```bash
./gradlew build
```


- **Run tests**:
```
./gradlew test
```
- **Clean the build directory**:
```bash
./gradlew clean
```
- **Run the application**:
```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```
- **Running the Application**:
To start the application with the local profile, use the following command in the terminal:
```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```
This will run the application with the local Spring profile, which can be configured in the application-local.properties file.


## Features
- **User management (create, delete, and validate users)**
- **Task management (assign tasks to users)**
- **Database integration with SQL**

## Support
For any issues or questions, please contact the project maintainer or open an issue in the repository.


## License
This project is licensed under the MIT License. See the LICENSE file for details.