package com.bluesoft.pawmarket.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioUpdateRequestDTO {

    @NotBlank(message = "El usuario es obligatorio")
    @Size(min = 3, max = 12, message = "El usuario debe tener entre 3 y 12 caracteres")
    private String usuario;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato válido")
    @Size(max = 100)
    private String email;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;
}
