# 🎬 Movie House

**Movie House** is a microservices-based movie ticket booking platform built using Java 17 and Spring Boot. It demonstrates a modular architecture including service discovery, API gateway, and RESTful communication between services.

---

## 🚀 Features

- **Microservices Architecture** – Independently deployable and scalable services.
- **Service Discovery** – Uses Zookeeper for dynamic service registration and discovery.
- **API Gateway** – Centralized entry point for all service communications.
- **RESTful APIs** – Clean and well-documented endpoints.
- **Database Integration** – Works with PostgreSQL or MySQL.
- **Swagger/OpenAPI** – Easy API documentation and testing interface.

---

## 🧱 Microservices Overview

| Service Name        | Description                                   |
|---------------------|-----------------------------------------------|
| `user-service`      | Manages user registration and profiles.       |
| `movie_service`     | Handles movie listings and details.           |
| `ticket-service`    | Manages ticket bookings and schedules.        |
| `location-service`  | Manages theater locations and addresses.      |
| `image-service`     | Handles image upload, storage, and retrieval. |

---

## 🛠️ Tech Stack

- **Language**: Java 17
- **Framework**: Spring Boot, Spring Cloud
- **Service Discovery**: Apache Zookeeper
- **API Gateway**: Spring Cloud Gateway
- **Databases**: PostgreSQL / MySQL
- **API Documentation**: Swagger / OpenAPI

---

## ⚙️ Getting Started

### Prerequisites

- Java 17
- Maven
- Zookeeper
- PostgreSQL or MySQL

### Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/vivekkumarq/movie-house.git
   cd movie-house
