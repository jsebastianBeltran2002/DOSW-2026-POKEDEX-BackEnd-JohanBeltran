package com.pokedex.pokedex.controller.impl;

import com.pokedex.pokedex.controller.api.PokemonApi;
import com.pokedex.pokedex.controller.dto.request.PokemonRequest;
import com.pokedex.pokedex.controller.dto.response.PokemonResponse;
import com.pokedex.pokedex.core.model.Pokemon;
import com.pokedex.pokedex.core.service.interfaces.PokemonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PokemonController implements PokemonApi {

    private final PokemonService pokemonService;

    @Override
    public ResponseEntity<List<PokemonResponse>> findAll() {
        return ResponseEntity.ok(List.of());
    }

    @Override
    public ResponseEntity<PokemonResponse> findById(Long id) {
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<PokemonResponse> buscar(String criterio) {
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<List<PokemonResponse>> filtrar(String region, String tipo,
                                                         String generacion, String evolucion, String habilidad, String ataque,
                                                         Boolean tieneMega, Boolean legendario) {
        return ResponseEntity.ok(List.of());
    }

    @Override
    public ResponseEntity<List<PokemonResponse>> ordenar(String criterio, String direccion) {
        return ResponseEntity.ok(List.of());
    }

    @Override
    public ResponseEntity<PokemonResponse> create(PokemonRequest request) {
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<PokemonResponse> update(Long id, PokemonRequest request) {
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        return ResponseEntity.noContent().build();
    }
}