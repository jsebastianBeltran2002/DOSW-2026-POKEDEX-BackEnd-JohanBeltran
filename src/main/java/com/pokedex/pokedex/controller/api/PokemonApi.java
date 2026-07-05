package com.pokedex.pokedex.controller.api;

import com.pokedex.pokedex.controller.dto.request.PokemonRequest;
import com.pokedex.pokedex.controller.dto.response.PokemonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Pokemon", description = "Gestión del catálogo de Pokémon")
@RequestMapping("/v1/pokemon")
public interface PokemonApi {

    @Operation(summary = "Listar todos los Pokémon")
    @GetMapping
    ResponseEntity<List<PokemonResponse>> findAll();

    @Operation(summary = "Obtener Pokémon por ID")
    @GetMapping("/{id}")
    ResponseEntity<PokemonResponse> findById(@PathVariable Long id);

    @Operation(summary = "Buscar Pokémon por nombre o número")
    @GetMapping("/buscar")
    ResponseEntity<PokemonResponse> buscar(@RequestParam String criterio);

    @Operation(summary = "Filtrar Pokémon")
    @GetMapping("/filtrar")
    ResponseEntity<List<PokemonResponse>> filtrar(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String generacion,
            @RequestParam(required = false) String evolucion,
            @RequestParam(required = false) String habilidad,
            @RequestParam(required = false) String ataque,
            @RequestParam(required = false) Boolean tieneMega,
            @RequestParam(required = false) Boolean legendario);

    @Operation(summary = "Ordenar Pokémon")
    @GetMapping("/ordenar")
    ResponseEntity<List<PokemonResponse>> ordenar(
            @RequestParam String criterio,
            @RequestParam String direccion);

    @Operation(summary = "Crear Pokémon - Solo ADMIN")
    @PostMapping
    ResponseEntity<PokemonResponse> create(@Valid @RequestBody PokemonRequest request);

    @Operation(summary = "Actualizar Pokémon - Solo ADMIN")
    @PutMapping("/{id}")
    ResponseEntity<PokemonResponse> update(@PathVariable Long id, @Valid @RequestBody PokemonRequest request);

    @Operation(summary = "Eliminar Pokémon - Solo ADMIN")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);
}