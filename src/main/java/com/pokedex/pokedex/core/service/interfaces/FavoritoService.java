package com.pokedex.pokedex.core.service.interfaces;

import com.pokedex.pokedex.core.model.Favorito;
import java.util.List;

public interface FavoritoService {
    Favorito guardar(Long usuarioId, Long pokemonId);
    void eliminar(Long usuarioId, Long pokemonId);
    List<Favorito> findByUsuarioId(Long usuarioId);
}