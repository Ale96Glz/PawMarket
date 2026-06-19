# Spring Boot for Dumies

> Guía práctica para aprender Spring Boot desde cero con buenas prácticas.
> Proyecto de referencia: **PawMarket** (marketplace de productos para mascotas).

---

## Cómo usar esta guía

1. Lee un capítulo.
2. Implementa en tu proyecto PawMarket.
3. Ejecuta y verifica (`mvnw spring-boot:run`).
4. Si algo falla, lee el stack trace de abajo hacia arriba (último `Caused by`).
5. Pasa al siguiente capítulo solo cuando el actual funcione.

**Requisitos previos:**

- Java 17+
- Maven (incluido `mvnw` en el proyecto)
- IDE (IntelliJ, VS Code/Cursor, Eclipse)
- Conceptos básicos de Java (clases, interfaces, listas)

---

## Índice

### Módulo 0 — Fundamentos

- [Cap. 0.1](#cap-01-qué-es-spring-boot) ¿Qué es Spring Boot?
- [Cap. 0.2](#cap-02-anatomía-de-un-proyecto) Anatomía de un proyecto Spring Boot
- [Cap. 0.3](#cap-03-el-ciclo-arrancar-y-leer-errores) Arrancar la app y leer errores

### Módulo 1 — Capas y arquitectura

- [Cap. 1.1](#cap-11-arquitectura-por-capas) Arquitectura por capas
- [Cap. 1.2](#cap-12-paquetes-y-responsabilidades) Paquetes y responsabilidades
- [Cap. 1.3](#cap-13-flujo-de-una-petición) Flujo de una petición HTTP

### Módulo 2 — Persistencia (JPA)

- [Cap. 2.1](#cap-21-la-entidad-pet) La entidad `Pet`
- [Cap. 2.2](#cap-22-repositorios-spring-data-jpa) Repositorios Spring Data JPA
- [Cap. 2.3](#cap-23-configuración-h2-y-jpa) Configuración H2 y JPA
- [Cap. 2.4](#cap-24-consultas-simples-y-booleanos) Consultas derivadas y booleanos

### Módulo 3 — Lógica de negocio

- [Cap. 3.1](#cap-31-servicio-interfaz-e-implementación) Service: interfaz + implementación
- [Cap. 3.2](#cap-32-transacciones-y-dependencias) `@Transactional` e inyección por constructor
- [Cap. 3.3](#cap-33-dtos-por-qué-no-exponer-la-entidad) DTOs: por qué no exponer la entidad

### Módulo 4 — API y presentación

- [Cap. 4.1](#cap-41-controller-rest-vs-mvc) `@RestController` vs `@Controller`
- [Cap. 4.2](#cap-42-crud-completo-de-pet) CRUD completo de Pet
- [Cap. 4.3](#cap-43-validación-de-entrada) Validación con `@Valid`
- [Cap. 4.4](#cap-44-thymeleaf-vistas-html) Thymeleaf (vistas HTML)
- [Cap. 4.5](#cap-45-swagger-con-springdoc) Documentación API con SpringDoc

### Módulo 5 — Calidad y producción

- [Cap. 5.1](#cap-51-mappers) Mappers (Entity ↔ DTO)
- [Cap. 5.2](#cap-52-manejo-de-excepciones) Manejo de excepciones (`@RestControllerAdvice`)
- [Cap. 5.3](#cap-53-tests-básicos) Tests básicos
- [Cap. 5.4](#cap-54-checklist-de-buenas-prácticas) Checklist de buenas prácticas

---

# Módulo 0 — Fundamentos

## Cap. 0.1 ¿Qué es Spring Boot?

**Spring Boot** es un framework de Java que simplifica crear aplicaciones empresariales:

- Configura automáticamente gran parte del proyecto (auto-configuración).
- Incluye servidor web embebido (no necesitas instalar Tomcat aparte).
- Integra fácilmente base de datos, seguridad, APIs, etc.

### Conceptos clave

| Concepto | Significado |
|----------|-------------|
| **Framework** | Estructura que te da herramientas y reglas para construir apps |
| **Bean** | Objeto gestionado por Spring (repositorio, servicio, controlador…) |
| **IoC / DI** | Spring crea e inyecta dependencias por ti |
| **Starter** | Dependencia Maven que agrupa librerías relacionadas |

### En PawMarket usamos

- `spring-boot-starter-webmvc` → APIs y controladores web
- `spring-boot-starter-data-jpa` → base de datos con JPA
- `spring-boot-starter-thymeleaf` → vistas HTML
- `h2` → base de datos en memoria (desarrollo)
- `springdoc-openapi-starter-webmvc-ui` → documentación Swagger de la API

### Ejercicio 0.1

Abre `pom.xml` e identifica cada dependencia. Pregúntate: *¿para qué capa de mi app sirve cada una?*

---

## Cap. 0.2 Anatomía de un proyecto

```
PawMarket/
├── pom.xml                          # Dependencias Maven
├── src/main/java/.../pawmarket/
│   ├── PawMarketApplication.java     # Punto de entrada
│   ├── model/                       # Entidades JPA
│   ├── repository/                  # Acceso a datos
│   ├── service/                     # Lógica de negocio
│   ├── controller/                  # HTTP (web)
│   ├── dto/                         # Objetos de entrada/salida (por crear)
│   └── mapper/                      # Entity ↔ DTO (por crear)
├── src/main/resources/
│   ├── application.properties       # Configuración
│   └── templates/                   # Vistas Thymeleaf
└── src/test/java/                   # Tests
```

### `PawMarketApplication.java`

```java
@SpringBootApplication
public class PawMarketApplication {
    public static void main(String[] args) {
        SpringApplication.run(PawMarketApplication.class, args);
    }
}
```

`@SpringBootApplication` hace tres cosas importantes:

1. Marca la clase como configuración principal.
2. Activa auto-configuración.
3. Escanea componentes en `com.bluesoft.pawmarket` y subpaquetes.

### Ejercicio 0.2

Crea mentalmente el mapa: *¿qué archivo tocarías para cambiar la BD? ¿Y para añadir un endpoint?*

---

## Cap. 0.3 El ciclo: arrancar y leer errores

### Comandos básicos

```bash
mvnw.cmd clean compile    # Compila
mvnw.cmd spring-boot:run  # Arranca la aplicación
```

> **Importante:** compilar bien NO garantiza que la app arranque.
> JPA valida entidades y repositorios al iniciar el contexto de Spring.

### Cómo leer un error de Spring

1. Ignora el bloque grande inicial.
2. Busca el **último** `Caused by:`.
3. Lee el mensaje corto (ej: `Not a managed type: class ...Pet`).
4. Abre la clase mencionada.

### Errores que ya viste en PawMarket

| Mensaje | Causa | Solución |
|---------|-------|----------|
| `Not a managed type: Pet` | Falta `@Entity` en la clase | Añadir `@Entity` a `Pet` |
| `No property 'tipo' found` | Método del repo no coincide con campos | Corregir nombre del método |
| `QueryCreationException` | Método mal formado en repositorio | Revisar gramática Spring Data |

### Ejercicio 0.3

Arranca la app. Si falla, copia solo el último `Caused by` y escribe en una frase qué clase debes revisar.

---

# Módulo 1 — Capas y arquitectura

## Cap. 1.1 Arquitectura por capas

```
Cliente (navegador / Postman)
        ↓
   Controller    ← Recibe HTTP, devuelve respuesta
        ↓
    Service       ← Lógica de negocio
        ↓
   Repository     ← Consultas a BD
        ↓
    Entity        ← Tabla en base de datos
```

### Regla de oro

**Cada capa hace una sola cosa.** No pongas SQL en el controlador ni lógica de negocio en el repositorio.

### Ejercicio 1.1

Para "listar mascotas pendientes de adopción", escribe qué haría cada capa (una frase por capa).

**Respuesta esperada (ejemplo):**

- **Controller:** recibe `GET /pets` y pide la lista al servicio.
- **Service:** llama al repositorio y devuelve el resultado (o lo mapea a DTO).
- **Repository:** ejecuta la consulta `findByAdoptadoFalse()` en la BD.
- **Entity:** representa cada fila de la tabla `pets`.

---

## Cap. 1.2 Paquetes y responsabilidades

| Paquete | Contiene | Anotaciones típicas |
|---------|----------|---------------------|
| `model` | Entidades JPA | `@Entity`, `@Id`, `@Column` |
| `repository` | Interfaces de datos | `extends JpaRepository` |
| `service` | Lógica de negocio | `@Service`, `@Transactional` |
| `controller` | Endpoints HTTP | `@RestController`, `@GetMapping` |
| `dto` | Datos de entrada/salida | `@NotBlank`, `@Min` (validación) |
| `mapper` | Conversiones | `@Component` |

### Ejercicio 1.2

Crea las carpetas `dto` y `mapper` en tu proyecto (vacías por ahora).

---

## Cap. 1.3 Flujo de una petición

Ejemplo: `GET /api/pets`

1. Cliente hace petición HTTP.
2. `PetRestController` recibe la llamada.
3. Llama a `petService.getPets()`.
4. El servicio llama a `petRepository.findAll()`.
5. Hibernate ejecuta `SELECT * FROM pets`.
6. Los datos suben la cadena hasta el cliente en JSON.

```
Cliente → Controller → Service → Repository → BD
                ↑          ↑          ↑
                └──────────┴──────────┘
                   respuesta de vuelta
```

### Ejercicio 1.3

Dibuja este flujo en papel con flechas. Añade dónde entraría el `Mapper` cuando uses DTOs.

---

# Módulo 2 — Persistencia (JPA)

## Cap. 2.1 La entidad `Pet`

Una **entidad** es una clase Java que representa una tabla.

### Anotaciones esenciales

```java
@Entity                              // Registra la clase en JPA
@Table(name = "pets")                // Nombre de la tabla
public class Pet {

    @Id                              // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)        // Regla de columna
    private String nombre;

    private Boolean adoptado;
}
```

### Buenas prácticas

- [ ] Campos `private`
- [ ] `@Entity` en la clase
- [ ] `@Id` en la clave primaria
- [ ] No exponer la entidad directamente en APIs públicas (usar DTO después)
- [ ] Lombok: usa `@Data` **o** `@Getter/@Setter`, no ambos redundantes

### Ejercicio 2.1

Revisa tu `Pet.java` con la checklist anterior. Corrige lo que falte.

---

## Cap. 2.2 Repositorios Spring Data JPA

```java
public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByNombre(String nombre);
    List<Pet> findByAdoptadoFalse();
}
```

### Gramática de métodos derivados

```
prefijo + By + NombreCampo + Condición
```

Prefijos válidos: `find`, `get`, `read`, `query` (siempre seguidos de `By`).

Ejemplos con `adoptado` (boolean):

- `findByAdoptadoTrue()` → adoptado = true
- `findByAdoptadoFalse()` → adoptado = false
- `findByAdoptado(Boolean valor)` → según el parámetro

### Errores comunes en repositorios

| Método incorrecto | Por qué falla |
|-------------------|---------------|
| `getAdoptado()` | No indica true ni false |
| `getAdoptadoFalse()` | Falta el `By` |
| `findByAdoptadoFalse(Boolean x)` | Mezcla condición fija + parámetro |
| `findByTipo(...)` | No existe campo `tipo` en `Pet` |

### Ejercicio 2.2

Añade un método para buscar mascotas por `raza`. Nómbralo tú siguiendo la gramática.

**Pista:** `findByRaza(String raza)`

---

## Cap. 2.3 Configuración H2 y JPA

En `application.properties`:

```properties
# Base de datos H2 en memoria
spring.datasource.url=jdbc:h2:mem:pawmarket
spring.datasource.username=sa
spring.datasource.password=

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Consola H2 (navegador)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

| Propiedad | Qué hace |
|-----------|----------|
| `ddl-auto=update` | Crea/actualiza tablas según entidades |
| `show-sql=true` | Muestra SQL en consola (útil para aprender) |
| `h2.console.enabled` | Activa consola web de H2 |

### Acceder a H2 Console

1. Arranca la app.
2. Abre `http://localhost:8080/h2-console`
3. JDBC URL: `jdbc:h2:mem:pawmarket`
4. Usuario: `sa` / Contraseña: (vacía)

### Ejercicio 2.3

Arranca la app, entra a `/h2-console` y verifica que existe la tabla `PETS`.

---

## Cap. 2.4 Consultas derivadas y booleanos

**Razonamiento para "pendientes de adopción":**

1. Requisito de negocio: mascotas no adoptadas.
2. En datos: `adoptado = false`.
3. En repositorio: `findByAdoptadoFalse()`.
4. En servicio: delegar sin `.filter()` extra en Java.

### Comparación: filtro en BD vs filtro en Java

| Enfoque | Código | ¿Recomendado? |
|---------|--------|---------------|
| BD (repositorio) | `findByAdoptadoFalse()` | ✅ Sí |
| Java (servicio) | `findAll().stream().filter(...)` | ❌ Solo para aprender o pocos datos |

### Ejercicio 2.4

Implementa en el servicio un método `getPendientesAdopcion()` que use el repositorio correctamente, sin `.filter()` redundante.

---

# Módulo 3 — Lógica de negocio

> 📝 **Implementa este módulo en tu proyecto PawMarket.**

## Cap. 3.1 Service: interfaz + implementación

### ¿Por qué interfaz + implementación?

| Ventaja | Explicación |
|---------|-------------|
| Desacoplamiento | El controlador depende del contrato, no de la implementación |
| Testabilidad | Puedes mockear `PetService` en tests |
| Claridad | La interfaz documenta qué puede hacer tu app |

### Estructura

```
PetService.java       → contrato (qué puede hacer)
PetServiceImpl.java   → implementación (cómo lo hace)
```

### Ejemplo

```java
// PetService.java
public interface PetService {
    List<PetResponse> getPets();
    List<PetResponse> getPetByNombre(String nombre);
    List<PetResponse> getPendientesAdopcion();
}

// PetServiceImpl.java
@Service
public class PetServiceImpl implements PetService {
    // implementación...
}
```

### Ejercicio 3.1

Refactoriza tu `PetService` para que los métodos devuelvan `PetResponse` en lugar de `Pet`.

---

## Cap. 3.2 `@Transactional` e inyección por constructor

### `@Service`

Marca la clase como bean de lógica de negocio. Spring la detecta y la gestiona.

### `@Transactional`

Agrupa operaciones de BD en una transacción. Si algo falla, se revierte todo.

> Usa `org.springframework.transaction.annotation.Transactional` (Spring), no la de Jakarta.

### Inyección por constructor (buena práctica)

```java
@Service
@Transactional
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final PetMapper petMapper;

    public PetServiceImpl(PetRepository petRepository, PetMapper petMapper) {
        this.petRepository = petRepository;
        this.petMapper = petMapper;
    }
}
```

**Evita** `@Autowired` en campos. El constructor es más claro y facilita los tests.

### Ejercicio 3.2

Añade `PetMapper` como dependencia del servicio por constructor.

---

## Cap. 3.3 DTOs: por qué no exponer la entidad

### ¿Qué es un DTO?

**Data Transfer Object** — objeto para transferir datos entre capas o hacia el cliente.

### ¿Por qué no usar `Pet` directamente en la API?

| Problema | DTO lo evita |
|----------|--------------|
| Acoplas API y base de datos | Cambias BD sin romper API |
| Riesgo con relaciones LAZY | Solo envías lo necesario |
| El cliente ve campos internos | Controlas qué expones |
| Validación mezclada con persistencia | Validas el DTO de entrada |

### Dos DTOs típicos

**`PetRequest`** — lo que envía el cliente (crear/actualizar):

```java
public class PetRequest {
    private String nombre;
    private int edad;
    private double peso;
    private String raza;
    private Boolean adoptado;
    // Sin id: lo genera la BD
}
```

**`PetResponse`** — lo que devuelves al cliente:

```java
public class PetResponse {
    private Long id;
    private String nombre;
    private int edad;
    private double peso;
    private String raza;
    private Boolean adoptado;
}
```

### Ejercicio 3.3

Crea `dto/PetRequest.java` y `dto/PetResponse.java` con Lombok `@Data`.

---

# Módulo 4 — API y presentación

> 📝 **Por implementar después del Módulo 3.**

## Cap. 4.1 `@RestController` vs `@Controller`

| Anotación | Devuelve | Uso |
|-----------|----------|-----|
| `@Controller` | Nombre de vista HTML | Thymeleaf |
| `@RestController` | JSON automáticamente | API REST |

### Thymeleaf (vistas)

```java
@Controller
public class PetController {

    @GetMapping("/pets")
    public String getPets(Model model) {
        model.addAttribute("pets", petService.getPets());
        return "pets";  // → templates/pets.html
    }
}
```

### REST (API JSON)

```java
@RestController
@RequestMapping("/api/pets")
public class PetRestController {

    @GetMapping
    public List<PetResponse> getAll() {
        return petService.getPets();
    }
}
```

### Ejercicio 4.1

Decide qué endpoints serán HTML (Thymeleaf) y cuáles JSON (REST). Escríbelo en una tabla.

---

## Cap. 4.2 CRUD completo de Pet

| Operación | HTTP | URL | Body |
|-----------|------|-----|------|
| Listar todas | GET | `/api/pets` | — |
| Obtener una | GET | `/api/pets/{id}` | — |
| Crear | POST | `/api/pets` | `PetRequest` |
| Actualizar | PUT | `/api/pets/{id}` | `PetRequest` |
| Eliminar | DELETE | `/api/pets/{id}` | — |

### Anotaciones del controlador REST

| Anotación | Significado |
|-----------|-------------|
| `@GetMapping` | Petición GET |
| `@PostMapping` | Petición POST |
| `@PutMapping` | Petición PUT |
| `@DeleteMapping` | Petición DELETE |
| `@PathVariable` | Variable en la URL (`/api/pets/5` → id=5) |
| `@RequestBody` | JSON del body → objeto Java |
| `ResponseEntity<>` | Control de status HTTP (201, 404, etc.) |

### Ejercicio 4.2

Implementa `GET /api/pets` y `GET /api/pets/{id}` en un `PetRestController`.

---

## Cap. 4.3 Validación de entrada

Añade al `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

En `PetRequest`:

```java
@NotBlank(message = "El nombre es obligatorio")
private String nombre;

@Min(value = 0, message = "La edad no puede ser negativa")
private int edad;

@Positive(message = "El peso debe ser positivo")
private double peso;
```

En el controlador:

```java
@PostMapping
public ResponseEntity<PetResponse> create(@Valid @RequestBody PetRequest request) {
    // ...
}
```

### Ejercicio 4.3

Añade validaciones a `PetRequest` y prueba enviar datos inválidos con Postman.

---

## Cap. 4.4 Thymeleaf (vistas HTML)

En `templates/pets.html`:

```html
<table>
    <tr th:each="pet : ${pets}">
        <td th:text="${pet.nombre}"></td>
        <td th:text="${pet.raza}"></td>
        <td th:text="${pet.adoptado} ? 'Adoptada' : 'Disponible'"></td>
    </tr>
</table>
```

### Ejercicio 4.4

Conecta `/pets` con el servicio y muestra la lista en la vista.

---

## Cap. 4.5 Swagger con SpringDoc

Con la dependencia `springdoc-openapi` ya en tu proyecto, al arrancar visita:

```
http://localhost:8080/swagger-ui.html
```

Ahí verás todos tus endpoints documentados automáticamente.

### Ejercicio 4.5

Abre Swagger UI y prueba `GET /api/pets` desde el navegador.

---

# Módulo 5 — Calidad y producción

> 📝 **Por implementar al final del proyecto.**

## Cap. 5.1 Mappers (Entity ↔ DTO)

> 📄 **Documentación completa:** [Mappers.md](./Mappers.md)  
> Incluye MapStruct (actual), mapper manual y patrón antiguo con repository.

### Enfoque actual: MapStruct

Declaramos **interfaces** con anotaciones; Maven genera `CategoriaMapperImpl`, `ProductoMapperImpl`, etc.

```java
@Mapper(componentModel = "spring")
public interface ProductoMapper {

    @Mapping(target = "nombre", source = "dto.nombre")
    @Mapping(target = "categoria", source = "categoria")
    Producto toEntity(ProductoRequestDTO dto, Categoria categoria);

    @AfterMapping
    default void defaultsAlCrear(@MappingTarget Producto producto, ProductoRequestDTO dto) {
        if (producto.getEstadoProducto() == null) {
            producto.setEstadoProducto(Producto.EstadoProducto.ACTIVO);
        }
    }

    @Mapping(target = "categoria", source = "categoria.nombre")
    @Mapping(target = "estado", source = "estadoProducto")
    ProductoResponseDTO toResponseDTO(Producto producto);
}
```

### Reglas (igual con MapStruct o manual)

- Solo transforma objetos. **No** llama al repositorio.
- El **service** resuelve IDs externos (`categoriaId` → `Categoria`).
- Tres métodos: `toEntity`, `toResponseDTO`, `updateEntityFromDTO`.

### Service: resolver relaciones antes de mapear

```java
Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
        .orElseThrow(() -> new ResourceNotFoundException("Categoría", dto.getCategoriaId()));

Producto producto = productoMapper.toEntity(dto, categoria);
```

### Patrones anteriores (referencia en Mappers.md)

1. **Mapper manual** — clase `@Component` con builder/setters (bueno para aprender).
2. **Mapper con repository** — `findById` dentro del mapper (evitar).

### Ejercicio 5.1

Compilá el proyecto (`mvnw compile`) y abrí `target/generated-sources/annotations/.../ProductoMapperImpl.java` para ver el código que MapStruct genera.

---

## Cap. 5.2 Manejo de excepciones

```java
// exception/PetNotFoundException.java
public class PetNotFoundException extends RuntimeException {
    public PetNotFoundException(Long id) {
        super("Mascota no encontrada con id: " + id);
    }
}

// exception/GlobalExceptionHandler.java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PetNotFoundException.class)
    public ResponseEntity<String> handleNotFound(PetNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
```

### Ejercicio 5.2

Lanza `PetNotFoundException` cuando `findById` devuelva vacío.

---

## Cap. 5.3 Tests básicos

```java
@SpringBootTest
class PawMarketApplicationTests {

    @Test
    void contextLoads() {
        // Verifica que Spring arranca sin errores
    }
}
```

Tests más útiles (con Mockito):

```java
@ExtendWith(MockitoExtension.class)
class PetServiceImplTest {

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetServiceImpl petService;

    @Test
    void getPets_devuelveLista() {
        // given → when → then
    }
}
```

### Ejercicio 5.3

Escribe un test que verifique `getPendientesAdopcion()` con un mock del repositorio.

---

## Cap. 5.4 Checklist de buenas prácticas

### Entidad (`model`)

- [ ] `@Entity` presente
- [ ] Campos `private`
- [ ] `@Id` en clave primaria
- [ ] No se expone directamente en APIs

### Repositorio (`repository`)

- [ ] Solo consultas y persistencia
- [ ] Métodos derivados con gramática correcta (`findBy...`)
- [ ] Sin lógica de negocio

### Servicio (`service`)

- [ ] Interfaz + implementación
- [ ] `@Service` y `@Transactional`
- [ ] Inyección por constructor
- [ ] Devuelve DTOs hacia fuera
- [ ] Sin acceso directo a HTTP

### DTO (`dto`)

- [ ] `Request` sin `id` para crear
- [ ] `Response` con `id` para leer
- [ ] Validaciones en `Request`

### Mapper (`mapper`)

- [ ] `@Component`
- [ ] Conversiones Entity ↔ DTO
- [ ] Sin llamadas al repositorio

### Controlador (`controller`)

- [ ] Delgado: delega al servicio
- [ ] REST separado de vistas Thymeleaf
- [ ] `@Valid` en entradas
- [ ] Status HTTP correctos (`201` crear, `404` no encontrado)

### Configuración

- [ ] `application.properties` con H2/JPA en desarrollo
- [ ] `ddl-auto=update` solo en desarrollo (nunca en producción sin migraciones)

---

## Glosario rápido

| Término | Definición |
|---------|------------|
| **Entity** | Clase mapeada a tabla de BD |
| **DTO** | Objeto para transferir datos entre capas/cliente |
| **Bean** | Componente gestionado por Spring |
| **Repository** | Acceso a datos |
| **Service** | Lógica de negocio |
| **Controller** | Punto de entrada HTTP |
| **Mapper** | Convierte Entity ↔ DTO |
| **JPA** | API estándar de Java para persistencia |
| **Hibernate** | Implementación de JPA que usa Spring |
| **Spring Data** | Abstracción que genera consultas desde nombres de métodos |

---

## Registro de progreso

- [ ] Módulo 0 — Fundamentos
- [ ] Módulo 1 — Capas y arquitectura
- [ ] Módulo 2 — Persistencia (JPA)
- [ ] Módulo 3 — Lógica de negocio
- [ ] Módulo 4 — API y presentación
- [ ] Módulo 5 — Calidad y producción

---

*Última actualización: junio 2026 — PawMarket v0.0.1*
