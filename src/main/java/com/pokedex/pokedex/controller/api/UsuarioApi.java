package com.pokedex.pokedex.controller.api;

import com.pokedex.pokedex.controller.dto.request.UsuarioRequest;
import com.pokedex.pokedex.controller.dto.response.UsuarioResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Usuarios", description = "Gestión de usuarios")
@RequestMapping("/v1/usuarios")
public interface UsuarioApi {

    @Operation(summary = "Listar todos los usuarios - Solo ADMIN")
    @GetMapping
    ResponseEntity<List<UsuarioResponse>> findAll();

    @Operation(summary = "Obtener usuario por ID - Solo ADMIN")
    @GetMapping("/{id}")
    ResponseEntity<UsuarioResponse> findById(@PathVariable Long id);

    @Operation(summary = "Crear perfil de usuario")
    @PostMapping
    ResponseEntity<UsuarioResponse> crear(@Valid @RequestBody UsuarioRequest request);

    @Operation(summary = "Editar perfil de usuario - Solo ADMIN")
    @PutMapping("/{id}")
    ResponseEntity<UsuarioResponse> actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioRequest request);

    @Operation(summary = "Activar o desactivar usuario - Solo ADMIN")
    @PatchMapping("/{id}/estado")
    ResponseEntity<UsuarioResponse> activarDesactivar(@PathVariable Long id, @RequestParam Boolean activo);

    @Operation(summary = "Eliminar usuario - Solo ADMIN")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> eliminar(@PathVariable Long id);

    @Operation(summary = "Comparar Pokémon legendarios entre dos usuarios")
    @GetMapping("/comparar")
    ResponseEntity<?> compararLegendarios(@RequestParam Long usuarioId1, @RequestParam Long usuarioId2);
}