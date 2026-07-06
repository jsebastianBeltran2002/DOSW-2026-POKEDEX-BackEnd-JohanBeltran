package com.pokedex.pokedex.controller.impl;

import com.pokedex.pokedex.controller.api.EquipoApi;
import com.pokedex.pokedex.controller.dto.request.EquipoRequest;
import com.pokedex.pokedex.controller.dto.response.EquipoResponse;
import com.pokedex.pokedex.controller.mapper.EquipoDtoMapper;
import com.pokedex.pokedex.core.model.Equipo;
import com.pokedex.pokedex.core.model.Pokemon;
import com.pokedex.pokedex.core.service.interfaces.EquipoService;
import com.pokedex.pokedex.core.service.interfaces.PokemonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EquipoController implements EquipoApi {

    private final EquipoService equipoService;
    private final PokemonService pokemonService;
    private final EquipoDtoMapper mapper;

    @Override
    public ResponseEntity<List<EquipoResponse>> findByUsuarioId(Long usuarioId) {
        List<EquipoResponse> response = equipoService.findByUsuarioId(usuarioId).stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<EquipoResponse> findById(Long id) {
        Equipo equipo = equipoService.findById(id);
        return ResponseEntity.ok(mapper.toResponse(equipo));
    }

    @Override
    public ResponseEntity<EquipoResponse> crear(Long usuarioId, EquipoRequest request) {
        List<Pokemon> pokemons = request.pokemonIds().stream()
                .map(pokemonService::findById)
                .toList();
        Equipo equipo = mapper.toDomainWithIds(request, usuarioId, pokemons);
        Equipo creado = equipoService.crear(equipo);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(creado));
    }

    @Override
    public ResponseEntity<Void> eliminar(Long id) {
        equipoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
