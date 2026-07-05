package com.pokedex.pokedex.core.service.interfaces;

import com.pokedex.pokedex.core.model.Equipo;
import java.util.List;

public interface EquipoService {
    Equipo crear(Equipo equipo);
    Equipo findById(Long id);
    List<Equipo> findByUsuarioId(Long usuarioId);
    void eliminar(Long id);
}