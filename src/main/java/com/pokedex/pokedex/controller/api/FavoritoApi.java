package com.pokedex.pokedex.controller.api;

import com.pokedex.pokedex.controller.dto.response.FavoritoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Favoritos", description = "Gestión de Pokémon favoritos")
@RequestMapping("/v1/favoritos")
public interface FavoritoApi {

    @Operation(summary = "Guardar Pokémon en favoritos")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/{usuarioId}/{pokemonId}")
    ResponseEntity<FavoritoResponse> guardar(@PathVariable Long usuarioId, @PathVariable Long pokemonId);

    @Operation(summary = "Eliminar Pokémon de favoritos")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{usuarioId}/{pokemonId}")
    ResponseEntity<Void> eliminar(@PathVariable Long usuarioId, @PathVariable Long pokemonId);

    @Operation(summary = "Listar favoritos de un usuario")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/usuario/{usuarioId}")
    ResponseEntity<List<FavoritoResponse>> findByUsuarioId(@PathVariable Long usuarioId);
}
