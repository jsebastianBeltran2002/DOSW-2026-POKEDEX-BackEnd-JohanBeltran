package com.pokedex.pokedex.controller.dto.response;

import java.util.List;

public record IntercambiarResponse(
        String mensaje,
        List<Long> pokemonsOfertante,
        List<Long> pokemonsReceptor
) {}
