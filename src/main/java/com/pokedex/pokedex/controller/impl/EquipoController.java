package com.pokedex.pokedex.controller.impl;

import com.pokedex.pokedex.controller.api.EquipoApi;
import com.pokedex.pokedex.controller.dto.request.EquipoRequest;
import com.pokedex.pokedex.controller.dto.response.EquipoResponse;
import com.pokedex.pokedex.core.service.interfaces.EquipoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class EquipoController implements EquipoApi {

    private final EquipoService equipoService;

    @Override
    public ResponseEntity<List<EquipoResponse>> findByUsuarioId(Long usuarioId) {
        return ResponseEntity.ok(List.of());
    }

    @Override
    public ResponseEntity<EquipoResponse> findById(Long id) {
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<EquipoResponse> crear(Long usuarioId, EquipoRequest request) {
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<Void> eliminar(Long id) {
        return ResponseEntity.noContent().build();
    }
}