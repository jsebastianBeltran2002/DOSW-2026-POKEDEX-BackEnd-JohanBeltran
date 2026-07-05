package com.pokedex.pokedex.controller.dto.request;

import jakarta.validation.constraints.*;
import java.util.List;

public record PokemonRequest(
        @NotNull(message = "El número de Pokédex es obligatorio")
        @Min(value = 1, message = "El número debe ser mayor a 0")
        Integer numero,

        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotEmpty(message = "Debe tener al menos un tipo")
        @Size(max = 3, message = "Un Pokémon puede tener máximo 3 tipos")
        List<String> tipos,

        @NotNull(message = "La altura es obligatoria")
        @DecimalMin(value = "0.0", inclusive = false, message = "La altura debe ser mayor a 0")
        Double altura,

        @NotNull(message = "El peso es obligatorio")
        @DecimalMin(value = "0.0", inclusive = false, message = "El peso debe ser mayor a 0")
        Double peso,

        String sprite
) {}