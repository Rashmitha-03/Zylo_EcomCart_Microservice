# Ecommerce Cart Items Microservice

A Spring Boot-based cart management microservice built for an e-commerce application. It provides endpoints to add, update, remove, and retrieve cart items for a specific user while keeping cart totals synchronized with product quantities.

## Overview

This service is designed to manage the shopping cart lifecycle for a user in an online store. It handles:

- Adding products to the cart
- Updating item quantity
- Removing items from the cart
- Fetching the current cart for a user
- Maintaining total cart value based on item totals

The application follows a typical layered architecture with:

- Controller layer for REST endpoints
- Service layer for business rules
- Repository layer for database interaction
- Entity/DTO layer for persistence and transfer objects
- Exception handling for invalid user/cart/item scenarios

## Features

- User-specific cart retrieval
- Product add-to-cart capability
- Quantity update for existing cart items
- Remove item from cart
- Cart total price calculation
- JPA-based persistence with MySQL
- RESTful API for integration with frontend or other microservices

## Tech Stack

- Java 17
- Spring Boot 3.5.0
- Spring Web
- Spring Data JPA
- MySQL Connector
- Lombok
- ModelMapper
- Maven

## Project Structure

```text
EcommerceCartItems/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── cts/
│   │   │           ├── controller/
│   │   │           ├── dto/
│   │   │           ├── entity/
│   │   │           ├── exception/
│   │   │           ├── repository/
│   │   │           ├── service/
│   │   │           └── EcommerceCartItemsApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── com/
│               └── cts/
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

## Prerequisites

Before running this project, ensure you have the following installed:

- JDK 17+
- Maven 3.8+
- MySQL Server
- Git
- IDE such as IntelliJ IDEA or VS Code

## Configuration

The application uses Spring Boot and expects database configuration to be added under `src/main/resources/application.properties`.

Example configuration:

```properties
spring.application.name=EcommerceCartItems
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_cart_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
server.port=8080
```

> Update the database URL, username, and password to match your local or cloud MySQL setup.

## Run the Application

### Using Maven

```bash
mvn clean install
mvn spring-boot:run
```

### Using the wrapper

```bash
./mvnw clean install
./mvnw spring-boot:run
```

On default Spring Boot settings, the service will run on:

```text
http://localhost:8080
```

## API Endpoints

The cart service exposes REST endpoints under the base path `/api/v1`.

### 1. Add item to cart

```http
POST /api/v1/add
```

Request body:

```json
{
  "userId": 1,
  "productId": 10,
  "quantity": 2
}
```

### 2. Remove item from cart

```http
DELETE /api/v1/remove/{userId}/{productId}
```

Example:

```http
DELETE /api/v1/remove/1/10
```

### 3. Update cart item quantity

```http
PUT /api/v1/update
```

Request body:

```json
{
  "userId": 1,
  "productId": 10,
  "quantity": 5
}
```

### 4. Get cart by user ID

```http
GET /api/v1/{userId}
```

Example:

```http
GET /api/v1/1
```

## Data Model Summary

The core entities include:

- User
- Cart
- CartItem
- Product

The cart keeps a list of cart items associated with a user, and each item stores product details, quantity, and computed total price.

## Typical Business Flow

1. A user is identified by `userId`.
2. The system loads or creates the user’s cart.
3. A product is looked up using `productId`.
4. The quantity is validated and added or updated.
5. The cart total is recalculated.
6. The updated cart is returned to the caller.

## Exception Handling

The project includes custom exceptions for common cart issues:

- `UserNotFoundException`
- `CartNotFoundException`
- `CartItemNotFoundException`

These exceptions help return meaningful error handling during invalid access or missing records.

## Notes

This project is a backend microservice for cart operations and is intended to be integrated with:

- product services
- user/profile services
- payment services
- frontend web or mobile clients

## Future Enhancements

- Add cart persistence validation and audit logs
- Add Swagger/OpenAPI documentation
- Add authentication and authorization
- Implement coupon and discount logic
- Add order checkout integration
- Add unit and integration tests for controller/service layers



## Author

Java FSE / Ecommerce Cart Microservice
