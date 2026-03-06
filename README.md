# JWT Auth & WebSocket Messaging — Spring Boot

A backend application built with **Java Spring Boot** that combines custom JWT-based authentication with real-time messaging over **WebSockets using the STOMP protocol**. Built as a personal project to explore security implementation, stateless authentication, and persistent connections in a Spring ecosystem.

---

## Features

- **Custom JWT Authentication** — Manually implemented JWT generation, signing, and validation without relying on third-party identity providers
- **WebSocket Messaging over STOMP** — Real-time bidirectional messaging between clients using the STOMP sub-protocol over WebSockets
- **Secured WebSocket Connections** — JWT tokens are validated on WebSocket handshake, ensuring only authenticated users can connect
- **In-Memory H2 Database** — Lightweight embedded database used for user storage during development, no external DB setup required
- **Dockerized** — Fully containerized with Docker for easy, consistent deployment across any environment

---

## Tech Stack

| Technology | Purpose |
|---|---|
| Java / Spring Boot | Backend framework |
| Spring Security | Authentication & authorization |
| JWT (JSON Web Tokens) | Stateless auth token generation & validation |
| WebSockets + STOMP | Real-time messaging protocol |
| H2 Database | In-memory relational database |
| Docker | Containerization & deployment |
| Gradle | Build tool |

---

## How It Works

1. A user registers and logs in via the REST auth endpoints
2. On successful login, a signed JWT is returned to the client
3. The client includes the JWT when establishing a WebSocket connection
4. Spring Security validates the token on the handshake — invalid tokens are rejected
5. Authenticated users can send and receive real-time messages over STOMP channels

---

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Login and receive a JWT |

### WebSocket

| Endpoint | Description |
|---|---|
| `/ws` | WebSocket connection endpoint |
| `/app/message` | Send a message (STOMP destination) |
| `/topic/messages` | Subscribe to receive messages |

> Verify exact routes in the `src` folder as they may differ slightly.

---

## Getting Started

### Run Locally

**Prerequisites:** Java 17+, Gradle

```bash
git clone https://github.com/NavkaranXO/stomp-jwtAuth-springBoot.git
cd stomp-jwtAuth-springBoot
./gradlew bootRun
```

The app will start on `http://localhost:8080`. The H2 console is available at `http://localhost:8080/h2-console`.

---

### Run with Docker

```bash
docker build -t stomp-jwt-app .
docker run -p 8080:8080 stomp-jwt-app
```

---

## Project Structure

```
src/main/java/com/example/demo/
├── DemoApplication.java        # App entry point
├── SecurityConfig.java         # Spring Security & JWT filter chain config
├── WebSocketConfig.java        # STOMP WebSocket configuration
├── JwtUtil.java                # JWT generation & validation utilities
├── JwtAuthFilter.java          # JWT request filter
├── Controller.java             # REST auth endpoints (register, login)
├── ChatController.java         # STOMP WebSocket message controller
├── ChatMessage.java            # Chat message model
├── User.java                   # User entity
├── UserRepository.java         # H2 data access layer
├── ClientDetails.java          # Spring Security UserDetails implementation
├── ClientDetailsService.java   # UserDetailsService implementation
├── LoginRequest.java           # Login request payload
└── AddUserRequest.java         # Register request payload
```

---

## What I Learned

- Implementing JWT authentication from scratch using Spring Security filter chains
- Configuring and securing WebSocket connections with STOMP
- Working with an embedded H2 database for rapid development
- Containerizing a Spring Boot application with Docker
