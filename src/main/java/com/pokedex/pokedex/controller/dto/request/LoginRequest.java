package com.pokedex.pokedex.controller.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo debe tener formato válido")
        String correo,

        @NotBlank(message = "La contraseña es obligatoria")
        String password
) {}