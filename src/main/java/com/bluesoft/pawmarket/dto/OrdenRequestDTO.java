package com.bluesoft.pawmarket.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenRequestDTO {

    @NotNull(message = "El usuario es obligatorio")
    @Positive(message = "El ID del usuario debe ser positivo")
    private Long usuarioId;

    @NotEmpty(message = "La orden debe tener al menos un producto")
    @Valid
    private List<OrdenDetalleRequestDTO> items;
}
