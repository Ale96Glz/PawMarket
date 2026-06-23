package com.bluesoft.pawmarket.dto;

import com.bluesoft.pawmarket.entity.Orden;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenEstadoRequestDTO {

    @NotNull(message = "El estado es obligatorio")
    private Orden.EstadoOrden estado;
}
