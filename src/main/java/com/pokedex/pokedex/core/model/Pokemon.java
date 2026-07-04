package com.pokedex.pokedex.core.model;

import lombok.Builder;
import lombok.Value;
import java.util.List;

@Value
@Builder(toBuilder = true)
public class Pokemon {
    Long id;
    Integer numero;
    String nombre;
    String descripcion;
    Double altura;
    Double peso;
    String sprite;
    List<String> tipos;
    String region;
    Integer generacion;
    Boolean legendario;
    Boolean tieneMega;
    Integer hp;
    Integer ataque;
    Integer defensa;
    Integer ataqueEspecial;
    Integer defensaEspecial;
    Integer velocidad;
}