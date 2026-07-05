package com.pokedex.pokedex.controller.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record EquipoResponse(
        Long id,
        String nombre,
        String analisisCompetitivo,
        LocalDateTime fechaCreacion,
        Long usuarioId,
        List<PokemonResponse> pokemons
) {}