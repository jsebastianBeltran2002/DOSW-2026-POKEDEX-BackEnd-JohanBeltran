package com.pokedex.pokedex.controller.api;

import com.pokedex.pokedex.controller.dto.request.EquipoRequest;
import com.pokedex.pokedex.controller.dto.response.EquipoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Equipos", description = "Gestión de equipos Pokémon")
@RequestMapping("/v1/equipos")
public interface EquipoApi {

    @Operation(summary = "Listar equipos del usuario")
    @GetMapping("/usuario/{usuarioId}")
    ResponseEntity<List<EquipoResponse>> findByUsuarioId(@PathVariable Long usuarioId);

    @Operation(summary = "Obtener equipo por ID")
    @GetMapping("/{id}")
    ResponseEntity<EquipoResponse> findById(@PathVariable Long id);

    @Operation(summary = "Crear equipo Pokémon")
    @PostMapping("/usuario/{usuarioId}")
    ResponseEntity<EquipoResponse> crear(@PathVariable Long usuarioId, @Valid @RequestBody EquipoRequest request);

    @Operation(summary = "Eliminar equipo")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> eliminar(@PathVariable Long id);
}