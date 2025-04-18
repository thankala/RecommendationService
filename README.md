# Recommendation Service for XM
This project was developed as part of an interview assessment for XM. It reflects the application of Domain-Driven Design (DDD) and Clean Architecture principles, drawing from my experience in previous projects.

The project was initialized using Spring Initializr with the following command:
```
spring init \
  --name=RecommendationService \
  --type=maven-project \
  --package-name=com.xm.recommendationservice \
  --dependencies=web,lombok,data-jpa,postgresql \
  RecommendationService
```

### Technologies Used

    Java 17

    Spring Boot

    Spring Web

    Spring Data JPA

    PostgreSQL

    Lombok

    OpenAPI (Swagger)

    Bucket4j + Redis (Rate Limiting)

### Architecture

This application follows a Clean Architecture approach with Domain-Driven Design (DDD). It is modularized into:

    api – REST controllers, request/response models, Swagger annotations

    application – use cases, services, scheduling logic

    domain – core business models and interfaces

    infrastructure – persistence and Redis rate-limiting implementations

### CSV Data Loading

Crypto price data is loaded from CSV files using a scheduled background task.

    CSVs are processed asynchronously in parallel using CompletableFuture.

    After parsing, data is saved to a Postgres database to ensure scalability.

### Containerization & Kubernetes Readiness

The Recommendation Service is fully containerized using Docker.

- A docker compose file can be found in the directory. 

- Environment consistency across development, testing, and production



### Rate Limiting with Redis

The service includes IP-based rate limiting.

    Implemented using Bucket4j + Redis

The use of Redis as the backing store for rate limiting was intentional — it ensures shared state across distributed instances, which is essential in a Kubernetes deployment.

### Details worth mentioning:
- The endpoints```/api/cryptos/normalized-range``` and ```/api/cryptos/{symbol}/stats```  **process data from the entire available dataset by default**, not just the past month. You can customize the date range by providing optional ```startDate``` and ```endDate``` query parameters.
- The scheduled task was introduced so that we could avoid triggering a rolling update every time the configuration changes. Since the files are mounted from a ConfigMap as volumes, updating the ConfigMap allows the application to automatically detect the new files during its hourly database refresh—no redeployment required.
- The job doesn't save all the entities because a databaseintegrityviolation would be thrown during the second time it inserts the same data. I wrote a custome query which basically is an upsrt 

```
@Modifying
    @Query(value = "INSERT INTO crypto_prices (timestamp, symbol, price) " +
            "VALUES (:timestamp, :symbol, :price) " +
            "ON CONFLICT (symbol, timestamp) DO NOTHING", nativeQuery = true)
    void insertIfNotExists(@Param("timestamp") Instant timestamp,
            @Param("symbol") String symbol,
            @Param("price") Double price);
```
- In production, Flyway would be used for structured, versioned schema migrations. Schema generation is currently handled via ```spring.jpa.hibernate.ddl-auto=update```.
- Unit and integration tests are not included due to the scope and focus of the assessment but would be implemented in a production environment.
- A liveness probe was not added due to the scope of the assessment.
- Swagger is available on http://localhost:8080/swagger-ui/index.html