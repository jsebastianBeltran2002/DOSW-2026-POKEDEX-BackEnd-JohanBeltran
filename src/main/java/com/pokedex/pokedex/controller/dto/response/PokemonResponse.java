package com.pokedex.pokedex.controller.dto.response;

import java.util.List;

public record PokemonResponse(
        Long id,
        Integer numero,
        String nombre,
        String descripcion,
        Double altura,
        Double peso,
        String sprite,
        List<String> tipos,
        String region,
        Integer generacion,
        Boolean legendario,
        Boolean tieneMega,
        Integer hp,
        Integer ataque,
        Integer defensa,
        Integer ataqueEspecial,
        Integer defensaEspecial,
        Integer velocidad
) {}