package com.bluesoft.pawmarket.repository;

import com.bluesoft.pawmarket.entity.Orden;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {
   Page<Orden> findByUsuario_Id(Long usuarioId, Pageable pageable);

   Page<Orden> findByEstado(Orden.EstadoOrden estado, Pageable pageable);

   @Query("SELECT o FROM Orden o LEFT JOIN FETCH o.detalles WHERE o.id = :id")
   Optional<Orden> findByIdWithDetalles(@Param("id") Long id);
}
