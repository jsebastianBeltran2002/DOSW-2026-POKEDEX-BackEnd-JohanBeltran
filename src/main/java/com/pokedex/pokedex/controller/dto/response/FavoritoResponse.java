package com.pokedex.pokedex.controller.dto.response;

import java.time.LocalDateTime;

public record FavoritoResponse(
        Long id,
        Long usuarioId,
        Long pokemonId,
        LocalDateTime fechaGuardado
) {}
