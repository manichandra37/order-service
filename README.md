# Order Service

> A domain-driven microservice for order management, part of an **Event-Driven Commerce Platform**.

## Overview

Order Service is built with a **Domain-Driven Design (DDD)** approach, emphasizing a rich domain model over anemic CRUD operations. Business rules and state transitions live inside the aggregate root, ensuring invariants are always enforced -- not scattered across service layers or controllers.

## Tech Stack

| Layer         | Technology                          |
|---------------|-------------------------------------|
| Framework     | Spring Boot 4.x                     |
| Language      | Java 17                             |
| Persistence   | Spring Data JPA, PostgreSQL         |
| Migrations    | Flyway                              |
| Build         | Maven                               |
| Testing       | JUnit 5, Testcontainers, AssertJ    |
| Monitoring    | Spring Boot Actuator                |

## Domain Model

### Rich Aggregate, Not Anemic CRUD

The `Order` entity is a true **aggregate root**. Instead of exposing public setters and pushing logic into service classes, all state mutations go through intention-revealing methods on the entity itself:

```java
order.confirm();  // succeeds only if status == CREATED
order.cancel();   // succeeds only if status == CREATED
```

Invalid transitions throw `IllegalStateException`, making it impossible to put an order into an inconsistent state.

### OrderStatus Lifecycle

```
CREATED ──┬──> CONFIRMED
           └──> CANCELLED
```

- **CREATED** -- initial state when an order is placed.
- **CONFIRMED** -- the order has been validated and accepted.
- **CANCELLED** -- the order was cancelled before confirmation.

Both `CONFIRMED` and `CANCELLED` are terminal states. Transitions are only allowed from `CREATED`.

### Optimistic Locking

The aggregate uses a `@Version` field to prevent lost updates in concurrent scenarios.

## Database Migrations

Schema evolution is managed by **Flyway**. Migration scripts live in:

```
src/main/resources/db/migration/
  V1__create_orders_table.sql
```

Hibernate DDL auto-generation is disabled (`ddl-auto=none`) -- Flyway is the single source of truth for the database schema.

## Testing

Integration tests run against a **real PostgreSQL instance** spun up by [Testcontainers](https://testcontainers.com/). No H2, no mocks -- the tests exercise the actual database driver, SQL dialect, and Flyway migrations.

```
src/test/java/.../OrderRepositoryIT.java
```

Tests verify:
- Orders can be persisted and loaded
- Status values are stored and retrieved correctly

## Prerequisites

- **Java 17+**
- **Docker** (required for Testcontainers during tests)
- **Maven 3.8+** (or use the included `./mvnw` wrapper)
- **PostgreSQL 15+** (for local development; tests use Testcontainers)

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/manichandra37/order-service.git
cd order-service
```

### 2. Start PostgreSQL

```bash
docker run -d \
  --name order-postgres \
  -e POSTGRES_DB=order_service \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  postgres:15
```

### 3. Configure the datasource

Update `src/main/resources/application.properties` with your database credentials if they differ from the defaults.

### 4. Run the application

```bash
./mvnw spring-boot:run
```

The service starts on **port 8080** by default.

### 5. Run tests

```bash
./mvnw test
```

Docker must be running -- Testcontainers will automatically pull and start a PostgreSQL container for the integration tests.

## API Endpoints

| Method | Endpoint              | Description              |
|--------|-----------------------|--------------------------|
| GET    | `/actuator/health`    | Health check             |

> Additional REST endpoints for order creation, confirmation, and cancellation are planned as the service evolves.

## Project Structure

```
src/
 main/
  java/com/mani/commerce/orderservice/
   order/
    domain/
     Order.java            # Aggregate root with business rules
     OrderStatus.java      # CREATED, CONFIRMED, CANCELLED
    repository/
     OrderRepository.java  # Spring Data JPA repository
  resources/
   db/migration/
    V1__create_orders_table.sql
   application.properties
 test/
  java/com/mani/commerce/orderservice/
   order/repository/
    OrderRepositoryIT.java  # Integration tests with Testcontainers
```

## License

This project is part of the Event-Driven Commerce Platform.
