package com.bluesoft.pawmarket.repository;

import com.bluesoft.pawmarket.entity.Categoria;
import com.bluesoft.pawmarket.entity.Producto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
     List<Producto> findByNombre(String nombre);

     Optional<Producto> findByNombreAndEstadoProducto(String nombre, Producto.EstadoProducto estadoProducto);

     List<Producto> findByEstadoProducto(Producto.EstadoProducto estadoProducto);

     Page<Producto> findByCategoria(Categoria categoria, Pageable pageable);

     @Query("select p from Producto p where p.precio between :min and :max order by p.precio asc")
     List<Producto> findByRangoPrecio(
         @Param("min")BigDecimal min,
         @Param("max") BigDecimal max
     );

     boolean existsByNombre(String nombre);

     boolean existsByCategoria_Id(Long categoriaId);
}
