package com.pokedex.pokedex.controller.impl;

import com.pokedex.pokedex.controller.api.UsuarioApi;
import com.pokedex.pokedex.controller.dto.request.UsuarioRequest;
import com.pokedex.pokedex.controller.dto.response.UsuarioResponse;
import com.pokedex.pokedex.core.service.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UsuarioController implements UsuarioApi {

    private final UsuarioService usuarioService;

    @Override
    public ResponseEntity<List<UsuarioResponse>> findAll() {
        return ResponseEntity.ok(List.of());
    }

    @Override
    public ResponseEntity<UsuarioResponse> findById(Long id) {
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<UsuarioResponse> crear(UsuarioRequest request) {
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<UsuarioResponse> actualizar(Long id, UsuarioRequest request) {
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<UsuarioResponse> activarDesactivar(Long id, Boolean activo) {
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<Void> eliminar(Long id) {
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> compararLegendarios(Long usuarioId1, Long usuarioId2) {
        return ResponseEntity.ok(null);
    }
}