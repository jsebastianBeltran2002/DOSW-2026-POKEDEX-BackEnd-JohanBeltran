package com.pokedex.pokedex.core.model;

import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
public class Favorito {
    Long id;
    Long usuarioId;
    Long pokemonId;
    LocalDateTime fechaGuardado;
}