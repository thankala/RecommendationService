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

It uses the latest version of Spring Boot and Java 17