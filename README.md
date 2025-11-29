# MessageMe Microservices Project

## Project Overview

The **MessageMe** project is a **real-time chat application** that consists of **three microservices**. These services are designed to communicate with each other via **gRPC** and expose **REST APIs** for frontend communication. The application is built using **Spring Boot** for backend services, and it incorporates **Kafka** for messaging and **PostgreSQL** for data storage.

The main microservices in the project are:

1. **MessageMe Microservice** - Handles user authentication, registration, JWT token management, and email/SMS verification.
2. **ChatGateway Microservice** - Manages chat functionality, including real-time message handling using **Kafka**.
3. **MessageStorageService Microservice** - Stores chat messages and user-related data in **PostgreSQL**.

---

## Technologies Used

### 1. **Spring Boot**
The backend services are built using **Spring Boot**, a widely-used framework for building Java-based web applications. It simplifies dependency injection, REST API creation, and other common web application tasks.

### 2. **Java 17**
The services are built with **Java 17**, the latest long-term support (LTS) release. Java 17 brings new features and improvements to the language, providing a stable and efficient foundation for building scalable backend services.

### 3. **JPA (Java Persistence API)**
**JPA** is used for object-relational mapping (ORM) in **Spring Boot**, enabling the application to interact with the **PostgreSQL** database seamlessly.

### 4. **Kafka**
**Apache Kafka** is used for message brokering between microservices. Kafka allows us to handle real-time data streaming efficiently, enabling the **ChatGateway Microservice** to manage messages between users and scale as needed.

### 5. **gRPC**
**gRPC** is used for communication between microservices, ensuring fast, efficient, and reliable communication over HTTP/2. gRPC enables strong typing and schema validation for messages, making the system more robust.

### 6. **PostgreSQL**
**PostgreSQL** is used as the primary database for **MessageMe** and **MessageStorageService**. It's a powerful, open-source relational database that provides support for complex queries and data consistency.

### 7. **JWT (JSON Web Token)**
**JWT** is used for secure user authentication and authorization. When a user logs in, they are issued a token that must be included in the headers of requests to secure endpoints.

### 8. **Maven**
**Maven** is used for project management and build automation. It handles dependencies, compiles the code, runs tests, and packages the services.

### 9. **Liquibase**
**Liquibase** is used for database migrations, allowing the application to manage and apply schema changes over time.

### 10. **Docker**
**Docker** is used to containerize the microservices, ensuring that each service can run independently in isolated environments. It also makes the deployment and scaling of services much easier.

### 11. **REST API**
REST APIs are exposed by each service to allow communication between the backend and the frontend (or other external clients). **MessageMe** uses REST APIs for user authentication and other user-related functionality.

---

## Project Structure
```plaintext
/messageme-frontend
  /src
    /app
    /assets
    /environments
  /node_modules
  angular.json
  package.json

/ChatGateway
  /src
  /resources

/MessageMe
  /src
  /resources

/MessageStorageService
  /src
  /resources

/proto-common
  /src/main

/infra
  docker-compose.yml
```

---

## Docker Setup

This project uses Docker to containerize all microservices. The microservices are configured to run in separate containers, allowing for easy scaling and maintenance.

### Setting up the Environment

Before running the services, you must set up the PostgreSQL databases and configure the environment variables. Here's how you do that:

#### 1. Create PostgreSQL Databases

You need to create two PostgreSQL databases:

- `messageme` for user-related data
- `message_storage` for storing messages

Example commands for creating databases:
```bash
psql -U postgres
CREATE DATABASE messageme;
CREATE DATABASE message_storage;
```

#### 2. Update Environment Variables in `docker-compose.yml`

Configure the following environment variables in your `docker-compose.yml`:
```yaml
messageme:
  environment:
    DB_URL: jdbc:postgresql://host.docker.internal:5433/usersMessageMe
    DB_USERNAME: your_postgres_username
    DB_PASSWORD: your_postgres_password
    JWT_SECRET: your_jwt_secret_key
    EMAIL_USER: your_email@gmail.com
    EMAIL_PASS: your_email_app_password

message-storage:
  environment:
    SPRING_DATASOURCE_URL: jdbc:postgresql://host.docker.internal:5433/message_storage
    SPRING_DATASOURCE_USERNAME: your_postgres_username
    SPRING_DATASOURCE_PASSWORD: your_postgres_password
```

Replace `your_postgres_username`, `your_postgres_password`, and `your_jwt_secret_key` with your own values.

---

## Running the Microservices

The project uses Docker Compose to run all microservices together. Here are the steps to run the services locally:

### 1. Configure Environment Variables

Follow the instructions above to configure environment variables in `docker-compose.yml`.

### 2. Start the Services

In the `infra` directory of the project, run:
```bash
docker compose build
docker compose up
```

This will build the Docker images and start the containers for all services:
- MessageMe
- ChatGateway
- MessageStorageService
- Kafka
- Zookeeper

### 3. Verify Services are Running

Check that all containers are running:
```bash
docker compose ps
```

### 4. Access the Services

- **MessageMe API**: `http://localhost:8081`
- **MessageStorageService API**: `http://localhost:8082`
- **ChatGateway API**: `http://localhost:8080`
- **Kafka**: `localhost:9093`

---

## Inter-Service Communication

The **ChatGateway** and **MessageStorageService** microservices communicate using **gRPC**. The frontend communicates with the services using **REST APIs** exposed by the microservices.

### Communication Flow:
1. User authenticates via **MessageMe** service and receives a JWT token
2. User sends messages through **ChatGateway**
3. **ChatGateway** publishes messages to **Kafka**
4. **MessageStorageService** consumes messages from **Kafka** and stores them in **PostgreSQL**

---

## Running the Frontend (Separate Repository)

The MessageMe backend does not include the frontend code, which will be hosted in a separate repository. However, once you integrate the frontend, the communication will happen through the REST APIs and WebSocket connections exposed by the backend services.

---

## Troubleshooting

### Common Errors:

#### Port Already in Use
If you encounter port conflicts (e.g., port 9093):
```bash
# Find the process using the port
sudo lsof -i :9093

# Stop the conflicting service (e.g., local Kafka)
brew services stop kafka

# Or change the port in docker-compose.yml
```

#### Kafka Communication Issues
Ensure that Kafka and Zookeeper are running properly in Docker containers:
```bash
docker compose logs kafka
docker compose logs zookeeper
```

#### gRPC Errors
Check gRPC server logs for issues related to message encoding/decoding:
```bash
docker compose logs messageme
docker compose logs message-storage
docker compose logs chatgateway
```

#### Database Connection Issues
Make sure the PostgreSQL databases are created and the correct credentials are provided in `docker-compose.yml`. Verify connection:
```bash
psql -U your_username -d messageme -h localhost -p 5433
```

#### Docker Build Failures
If Docker cannot pull images, check your internet connection:
```bash
# Test Docker network connectivity
docker run --rm alpine ping -c 3 google.com

# Restart Docker Desktop if needed
```

---

## Advantages of This Architecture

### 1. Scalability
The use of Kafka allows the system to scale efficiently by decoupling services and enabling asynchronous communication between them. This is especially important for real-time messaging.

### 2. Microservices
Each part of the application (authentication, messaging, storage) is handled by its own microservice, allowing independent scaling and maintenance.

### 3. Inter-Service Communication via gRPC
gRPC provides fast and efficient communication between microservices, ensuring low-latency and high-performance interactions.

### 4. Containerization with Docker
The entire application is containerized using Docker, allowing it to run consistently across different environments and making deployment easier.

### 5. Event-Driven Architecture
Kafka enables event-driven communication patterns, making the system more resilient and allowing for easy addition of new consumers or producers.

---

## API Documentation

### MessageMe Service Endpoints

#### Authentication
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login and receive JWT token
- `POST /api/auth/verify-email` - Verify email address
- `POST /api/auth/verify-sms` - Verify SMS code

#### User Management
- `GET /api/users/profile` - Get user profile (requires JWT)
- `PUT /api/users/profile` - Update user profile (requires JWT)

### ChatGateway Service Endpoints

#### Messaging
- `POST /api/chat/send` - Send a message (requires JWT)
- `GET /api/chat/history` - Get chat history (requires JWT)
- `WebSocket /ws/chat` - Real-time chat connection

### MessageStorageService gRPC

This service primarily communicates via gRPC and is not directly exposed via REST API.

---

## Development

### Prerequisites
- Java 17+
- Maven 3.6+
- Docker Desktop
- PostgreSQL 13+
- Node.js 16+ (for frontend)

### Building Services Locally
```bash
# Build MessageMe
cd MessageMe
mvn clean install

# Build ChatGateway
cd ../ChatGateway
mvn clean install

# Build MessageStorageService
cd ../MessageStorageService
mvn clean install
```

### Running Tests
```bash
mvn test
```

---

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---


## Contact

For questions or support, please contact the development team.

---

## Conclusion

This **MessageMe** project provides a scalable, real-time messaging platform using a microservices architecture. By leveraging technologies like **Spring Boot**, **Kafka**, **gRPC**, **PostgreSQL**, and **Docker**, the system is designed for flexibility, scalability, and high performance.
