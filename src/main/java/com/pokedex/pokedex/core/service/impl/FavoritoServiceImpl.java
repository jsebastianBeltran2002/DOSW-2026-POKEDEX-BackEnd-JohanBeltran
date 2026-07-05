package com.pokedex.pokedex.core.service.impl;

import com.pokedex.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.pokedex.core.model.Favorito;
import com.pokedex.pokedex.core.service.interfaces.FavoritoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavoritoServiceImpl implements FavoritoService {

    @Override
    public Favorito guardar(Long usuarioId, Long pokemonId) {
        return Favorito.builder()
                .usuarioId(usuarioId)
                .pokemonId(pokemonId)
                .build();
    }

    @Override
    public void eliminar(Long usuarioId, Long pokemonId) {
        throw new ResourceNotFoundException("Favorito", "usuarioId", usuarioId);
    }

    @Override
    public List<Favorito> findByUsuarioId(Long usuarioId) {
        return List.of();
    }
}