package com.pokedex.pokedex.controller.dto.response;

public record TokenResponse(
        String token,
        String tipo,
        String correo,
        String rol
) {}