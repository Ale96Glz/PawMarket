package com.bluesoft.pawmarket.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrdenResponseDTO(
        Long id,
        Long usuarioId,
        String usuarioNombre,
        String estado,
        BigDecimal total,
        LocalDateTime creadoEn,
        LocalDateTime actualizadoEn,
        List<OrdenDetalleResponseDTO> detalles
) {
}
