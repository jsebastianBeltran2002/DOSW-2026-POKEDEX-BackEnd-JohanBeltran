package com.pokedex.pokedex.controller.impl;

import com.pokedex.pokedex.controller.api.PokemonApi;
import com.pokedex.pokedex.controller.dto.request.PokemonRequest;
import com.pokedex.pokedex.controller.dto.response.PokemonResponse;
import com.pokedex.pokedex.controller.mapper.PokemonDtoMapper;
import com.pokedex.pokedex.core.model.Pokemon;
import com.pokedex.pokedex.core.service.interfaces.PokemonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PokemonController implements PokemonApi {

    private final PokemonService pokemonService;
    private final PokemonDtoMapper mapper;

    @Override
    public ResponseEntity<List<PokemonResponse>> findAll() {
        List<PokemonResponse> response = mapper.toResponseList(pokemonService.findAll());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<PokemonResponse> findById(Long id) {
        Pokemon pokemon = pokemonService.findById(id);
        return ResponseEntity.ok(mapper.toResponse(pokemon));
    }

    @Override
    public ResponseEntity<PokemonResponse> buscar(String criterio) {
        Pokemon pokemon;
        try {
            Integer numero = Integer.parseInt(criterio);
            pokemon = pokemonService.findByNumero(numero);
        } catch (NumberFormatException e) {
            pokemon = pokemonService.findAll().stream()
                    .filter(p -> p.getNombre().equalsIgnoreCase(criterio))
                    .findFirst()
                    .orElseThrow(() -> new com.pokedex.pokedex.core.exception.ResourceNotFoundException(
                            "Pokemon", "nombre", criterio));
        }
        return ResponseEntity.ok(mapper.toResponse(pokemon));
    }

    @Override
    public ResponseEntity<List<PokemonResponse>> filtrar(String region, String tipo,
                                                         String generacion, String evolucion,
                                                         String habilidad, String ataque,
                                                         Boolean tieneMega, Boolean legendario) {
        List<Pokemon> resultado = pokemonService.filtrar(region, tipo, generacion,
                evolucion, habilidad, ataque, tieneMega, legendario);
        return ResponseEntity.ok(mapper.toResponseList(resultado));
    }

    @Override
    public ResponseEntity<List<PokemonResponse>> ordenar(String criterio, String direccion) {
        List<Pokemon> resultado = pokemonService.ordenar(criterio, direccion);
        return ResponseEntity.ok(mapper.toResponseList(resultado));
    }

    @Override
    public ResponseEntity<PokemonResponse> create(PokemonRequest request) {
        Pokemon creado = pokemonService.create(mapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(creado));
    }

    @Override
    public ResponseEntity<PokemonResponse> update(Long id, PokemonRequest request) {
        Pokemon actualizado = pokemonService.update(id, mapper.toDomain(request));
        return ResponseEntity.ok(mapper.toResponse(actualizado));
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        pokemonService.delete(id);
        return ResponseEntity.noContent().build();
    }
}