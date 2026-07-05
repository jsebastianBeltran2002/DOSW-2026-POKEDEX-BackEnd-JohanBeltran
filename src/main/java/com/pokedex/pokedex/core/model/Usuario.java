package com.pokedex.pokedex.core.model;

import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
public class Usuario {
    Long id;
    String nombre;
    String correo;
    String password;
    Boolean activo;
    String rol;
    LocalDateTime fechaRegistro;
}