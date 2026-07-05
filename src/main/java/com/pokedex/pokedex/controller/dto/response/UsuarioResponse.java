package com.pokedex.pokedex.controller.dto.response;

import java.time.LocalDateTime;

public record UsuarioResponse(
        Long id,
        String nombre,
        String correo,
        Boolean activo,
        String rol,
        LocalDateTime fechaRegistro
) {}