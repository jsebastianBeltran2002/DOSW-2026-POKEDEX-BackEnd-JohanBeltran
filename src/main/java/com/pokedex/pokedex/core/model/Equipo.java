package com.pokedex.pokedex.core.model;

import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder(toBuilder = true)
public class Equipo {
    Long id;
    String nombre;
    String analisisCompetitivo;
    LocalDateTime fechaCreacion;
    Long usuarioId;
    List<Pokemon> pokemons;
}