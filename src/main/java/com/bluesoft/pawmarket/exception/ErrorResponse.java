package com.bluesoft.pawmarket.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        int status,
        String error,
        String mensaje,
        LocalDateTime timestamp
) {
}
