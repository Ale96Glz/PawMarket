package com.bluesoft.pawmarket.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductoResponseDTO(
        Long          id,
        String        nombre,
        BigDecimal    precio,
        String        descripcion,
        String        categoria,
        Integer       cantidad,
        String        estado,
        LocalDateTime creadoEn
) {
}
