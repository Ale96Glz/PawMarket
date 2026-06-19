# PawMarket API

**PawMarket** is a pet products marketplace backend built with Spring Boot. Modular monolith architecture, ready to evolve into microservices.

## Stack

- Java 17
- Spring Boot 4
- Spring Data JPA + H2
- MapStruct
- Spring Validation
- Spring Security (BCrypt)
- SpringDoc OpenAPI

## Modules

| Domain    | Endpoints              | Status   |
|-----------|------------------------|----------|
| Categoría | `/api/v1/categoria`    | Complete |
| Producto  | `/api/v1/producto`     | Complete |
| Usuario   | `/api/v1/usuario`      | Complete |
| Orden     | `/api/v1/orden`        | Planned  |

## Architecture

```
Controller → Service → Mapper (MapStruct) → Repository → Entity
                ↓
         Request DTO / Response record
```

## Run locally

```bash
./mvnw spring-boot:run
```

- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- H2 Console: http://localhost:8080/h2-console  
  - JDBC URL: `jdbc:h2:mem:pawmarket`

## Documentation

- [Spring Boot for Dumies](docs/Spring%20Boot%20for%20Dumies.md) — learning guide
- [Mappers](docs/Mappers.md) — MapStruct and mapping patterns

## License

Portfolio project — `com.bluesoft.pawmarket`
