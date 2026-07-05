package com.pokedex.pokedex.controller.dto.request;

import jakarta.validation.constraints.*;
import java.util.List;

public record EquipoRequest(
        @NotBlank(message = "El nombre del equipo es obligatorio")
        String nombre,

        @NotEmpty(message = "El equipo debe tener al menos un Pokémon")
        @Size(max = 6, message = "El equipo puede tener máximo 6 Pokémon")
        List<Long> pokemonIds
) {}