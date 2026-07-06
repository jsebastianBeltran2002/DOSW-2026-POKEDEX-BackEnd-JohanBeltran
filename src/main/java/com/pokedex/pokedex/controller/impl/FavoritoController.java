package com.pokedex.pokedex.controller.impl;

import com.pokedex.pokedex.controller.api.FavoritoApi;
import com.pokedex.pokedex.controller.dto.response.FavoritoResponse;
import com.pokedex.pokedex.controller.mapper.FavoritoDtoMapper;
import com.pokedex.pokedex.core.service.interfaces.FavoritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FavoritoController implements FavoritoApi {

    private final FavoritoService favoritoService;
    private final FavoritoDtoMapper mapper;

    @Override
    public ResponseEntity<FavoritoResponse> guardar(Long usuarioId, Long pokemonId) {
        var favorito = favoritoService.guardar(usuarioId, pokemonId);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(favorito));
    }

    @Override
    public ResponseEntity<Void> eliminar(Long usuarioId, Long pokemonId) {
        favoritoService.eliminar(usuarioId, pokemonId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<FavoritoResponse>> findByUsuarioId(Long usuarioId) {
        var favoritos = favoritoService.findByUsuarioId(usuarioId);
        return ResponseEntity.ok(mapper.toResponseList(favoritos));
    }
}
