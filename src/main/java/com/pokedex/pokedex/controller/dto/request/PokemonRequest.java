package com.pokedex.pokedex.controller.dto.request;

import jakarta.validation.constraints.*;
import java.util.List;

public record PokemonRequest(
        @NotNull(message = "El número de Pokédex es obligatorio")
        @Min(value = 1, message = "El número debe ser mayor a 0")
        Integer numero,

        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        String descripcion,

        @NotEmpty(message = "Debe tener al menos un tipo")
        @Size(max = 3, message = "Un Pokémon puede tener máximo 3 tipos")
        List<String> tipos,

        @NotNull(message = "La altura es obligatoria")
        @DecimalMin(value = "0.0", inclusive = false, message = "La altura debe ser mayor a 0")
        Double altura,

        @NotNull(message = "El peso es obligatorio")
        @DecimalMin(value = "0.0", inclusive = false, message = "El peso debe ser mayor a 0")
        Double peso,

        String sprite,

        String region,

        Integer generacion,

        Boolean legendario,

        Boolean tieneMega,

        @Min(value = 0, message = "HP debe ser mayor o igual a 0")
        Integer hp,

        @Min(value = 0, message = "Ataque debe ser mayor o igual a 0")
        Integer ataque,

        @Min(value = 0, message = "Defensa debe ser mayor o igual a 0")
        Integer defensa,

        @Min(value = 0, message = "Ataque especial debe ser mayor o igual a 0")
        Integer ataqueEspecial,

        @Min(value = 0, message = "Defensa especial debe ser mayor o igual a 0")
        Integer defensaEspecial,

        @Min(value = 0, message = "Velocidad debe ser mayor o igual a 0")
        Integer velocidad
) {}