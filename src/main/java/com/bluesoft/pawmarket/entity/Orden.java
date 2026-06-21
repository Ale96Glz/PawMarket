package com.bluesoft.pawmarket.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "ordenes"
)
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoOrden estado;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime creadoEn;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrdenDetalle> detalles = new ArrayList<>();

    @UpdateTimestamp
    private LocalDateTime actualizadoEn;

    public enum EstadoOrden{
        PENDIENTE,
        CONFIRMADA,
        CANCELADA
    }

}
