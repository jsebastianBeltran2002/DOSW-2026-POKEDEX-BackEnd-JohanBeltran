package com.pokedex.pokedex.controller.dto.request;

import jakarta.validation.constraints.NotNull;

public record IntercambiarRequest(
        @NotNull(message = "El ID del usuario ofertante es obligatorio")
        Long ofertanteId,

        @NotNull(message = "El ID del usuario receptor es obligatorio")
        Long receptorId,

        @NotNull(message = "El ID del Pokémon ofrecido es obligatorio")
        Long pokemonOfertadoId,

        @NotNull(message = "El ID del Pokémon solicitado es obligatorio")
        Long pokemonSolicitadoId
) {}
