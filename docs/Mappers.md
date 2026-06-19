# Mappers en PawMarket

Guía de referencia sobre cómo convertimos **Entity ↔ DTO** en el proyecto.

---

## Enfoque actual: MapStruct

Usamos [MapStruct](https://mapstruct.org/) para generar el código de mapeo en **tiempo de compilación**.

| Ventaja | Detalle |
|---------|---------|
| Menos boilerplate | Declarás una interfaz; Maven genera `*MapperImpl` |
| Errores en compile | Si falta un campo, falla al compilar, no en runtime |
| Spring integrado | `@Mapper(componentModel = "spring")` → bean inyectable |
| Misma arquitectura | El **service** sigue resolviendo relaciones (`categoriaId` → `Categoria`) |

### Dependencias (`pom.xml`)

```xml
<properties>
    <mapstruct.version>1.6.3</mapstruct.version>
</properties>

<dependency>
    <groupId>org.mapstruct</groupId>
    <artifactId>mapstruct</artifactId>
    <version>${mapstruct.version}</version>
</dependency>
```

Procesadores de anotaciones (junto con Lombok):

```xml
<path>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok-mapstruct-binding</artifactId>
    <version>0.2.0</version>
</path>
<path>
    <groupId>org.mapstruct</groupId>
    <artifactId>mapstruct-processor</artifactId>
    <version>${mapstruct.version}</version>
</path>
```

### Código generado

Al compilar (`mvnw compile`), MapStruct crea implementaciones en:

```
target/generated-sources/annotations/
  com/bluesoft/pawmarket/mapper/CategoriaMapperImpl.java
  com/bluesoft/pawmarket/mapper/ProductoMapperImpl.java
  com/bluesoft/pawmarket/mapper/UsuarioMapperImpl.java
```

No editás esos archivos; se regeneran en cada build.

---

## Responsabilidad del mapper

El mapper **solo transforma objetos**. No valida reglas de negocio ni persiste en base de datos.

| Sí hace | No hace |
|---------|---------|
| Copiar campos DTO → Entity | `repository.save()` |
| Copiar campos Entity → Response | `existsByNombre()`, validaciones |
| Valores por defecto de mapeo (`estado ACTIVO`, `rol CLIENTE`) | Hashear passwords |
| Aplanar relaciones (`categoria.nombre` → `String`) | Consultar repositories |

---

## Flujo al crear un producto

```
POST /api/v1/producto  { "categoriaId": 2, ... }
        │
        ▼
ProductoServiceImpl.crear(dto)
        │
        ├─ existsByNombre()          ← regla de negocio (service)
        ├─ resolverCategoria(2)      ← findById (service)
        ├─ productoMapper.toEntity(dto, categoria)   ← MapStruct
        └─ productoRepository.save(producto)
```

**Resolver la categoría** = el service convierte `categoriaId` del DTO en la entidad `Categoria` que JPA necesita.

---

## Ejemplos MapStruct en el proyecto

### CategoriaMapper (simple)

```java
@Mapper(componentModel = "spring")
public interface CategoriaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productos", ignore = true)
    Categoria toEntity(CategoriaRequestDTO dto);

    CategoriaResponseDTO toResponseDTO(Categoria categoria);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productos", ignore = true)
    void updateEntityFromDTO(CategoriaRequestDTO dto, @MappingTarget Categoria categoria);
}
```

### UsuarioMapper (defaults con `@AfterMapping`)

```java
@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "rol", ignore = true)
    // ...
    Usuario toEntity(UsuarioRequestDTO dto);

    UsuarioResponseDTO toResponseDTO(Usuario usuario);

    @AfterMapping
    default void defaultsAlCrear(@MappingTarget Usuario usuario, UsuarioRequestDTO dto) {
        usuario.setActivo(true);
        usuario.setRol(Usuario.RolUsuario.CLIENTE);
    }
}
```

`@AfterMapping` con `UsuarioRequestDTO` solo corre en **crear**, no en update (`UsuarioUpdateRequestDTO`).

### ProductoMapper (relación resuelta por el service)

Cuando hay **dos parámetros fuente** (`dto` + `categoria`), hay que indicar de dónde viene cada campo si los nombres coinciden:

```java
@Mapping(target = "nombre", source = "dto.nombre")
@Mapping(target = "categoria", source = "categoria")
@Mapping(target = "estadoProducto", ignore = true)
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
```

El `if (== null)` evita sobrescribir el estado en **update** (create y update comparten `ProductoRequestDTO`).

---

## Patrón anterior: mapper manual (`@Component`)

Antes de MapStruct, los mappers eran **clases Java** con métodos escritos a mano.

### Ejemplo manual (ProductoMapper)

```java
@Component
public class ProductoMapper {

    public Producto toEntity(ProductoRequestDTO dto, Categoria categoria) {
        return Producto.builder()
                .nombre(dto.getNombre())
                .precio(dto.getPrecio())
                .descripcion(dto.getDescripcion())
                .categoria(categoria)
                .cantidad(dto.getCantidad())
                .estadoProducto(Producto.EstadoProducto.ACTIVO)
                .build();
    }

    public ProductoResponseDTO toResponseDTO(Producto producto) {
        return new ProductoResponseDTO(
                producto.getId(),
                producto.getNombre(),
                // ...
                producto.getCategoria() != null ? producto.getCategoria().getNombre() : null,
                producto.getEstadoProducto().name(),
                producto.getCreadoEn()
        );
    }
}
```

**Cuándo usarlo:** aprendizaje, proyectos muy chicos, o cuando no querés dependencia extra.

**Por qué migramos a MapStruct:** menos código repetitivo, mismas reglas declarativas con `@Mapping`, estándar en equipos Spring medianos/grandes.

---

## Patrón más antiguo: mapper con repository

Aún antes, `ProductoMapper` inyectaba `CategoriaRepository` y hacía el `findById` dentro del mapper.

```java
@Component
@RequiredArgsConstructor
public class ProductoMapper {

    private final CategoriaRepository categoriaRepository;

    public Producto toEntity(ProductoRequestDTO dto) {
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(...);
        return Producto.builder().categoria(categoria).build();
    }
}
```

**Problema:** mezclaba consulta a BD con conversión. Hoy el **service** resuelve la categoría y MapStruct solo mapea.

---

## Los 3 métodos estándar por dominio

| Método | Cuándo | Retorno |
|--------|--------|---------|
| `toEntity(dto)` o `toEntity(dto, relaciones...)` | POST crear | Nueva entidad |
| `toResponseDTO(entity)` | GET, respuestas | Record/DTO de salida |
| `updateEntityFromDTO(dto, entity, ...)` | PUT actualizar | `void` (`@MappingTarget`) |

---

## Anotaciones MapStruct más usadas

| Anotación | Uso |
|-----------|-----|
| `@Mapper(componentModel = "spring")` | Bean de Spring |
| `@Mapping(target = "x", ignore = true)` | No mapear (id, timestamps, password en update) |
| `@Mapping(target = "x", source = "y.z")` | Campo anidado o parámetro explícito |
| `@MappingTarget` | Update in-place sobre entidad existente |
| `@AfterMapping` | Defaults después de mapear (ej. `rol`, `estado ACTIVO`) |

---

## Referencia por mapper

| Mapper | Notas |
|--------|-------|
| `CategoriaMapper` | Ignora `productos` (relación inversa) |
| `UsuarioMapper` | Sin password en response; `@AfterMapping` para `rol` y `activo` al crear |
| `ProductoMapper` | `Categoria` la pasa el service; response aplanado |

Ver también: [Cap. 5.1 en Spring Boot for Dumies.md](./Spring%20Boot%20for%20Dumies.md#cap-51-mappers-entity--dto).
