package com.pokedex.pokedex.core.service.impl;

import com.pokedex.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.pokedex.core.model.Equipo;
import com.pokedex.pokedex.core.service.interfaces.EquipoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EquipoServiceImpl implements EquipoService {

    @Override
    public Equipo crear(Equipo equipo) {
        return equipo;
    }

    @Override
    public Equipo findById(Long id) {
        throw new ResourceNotFoundException("Equipo", "id", id);
    }

    @Override
    public List<Equipo> findByUsuarioId(Long usuarioId) {
        return List.of();
    }

    @Override
    public void eliminar(Long id) {
        throw new ResourceNotFoundException("Equipo", "id", id);
    }
}