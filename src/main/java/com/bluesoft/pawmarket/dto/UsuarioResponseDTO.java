package com.bluesoft.pawmarket.dto;

import java.time.LocalDateTime;

public record UsuarioResponseDTO(
        Long id,
        String usuario,
        String email,
        String nombre,
        boolean activo,
        String rol,
        LocalDateTime creadoEn
) {
}
