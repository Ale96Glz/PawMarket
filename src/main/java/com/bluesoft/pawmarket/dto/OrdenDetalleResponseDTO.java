package com.bluesoft.pawmarket.dto;

import java.math.BigDecimal;

public record OrdenDetalleResponseDTO(
        Long id,
        Long productoId,
        String productoNombre,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal
) {
}
