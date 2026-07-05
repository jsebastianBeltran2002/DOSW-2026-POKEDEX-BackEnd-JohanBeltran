package com.pokedex.pokedex.controller.impl;

import com.pokedex.pokedex.controller.api.AuthApi;
import com.pokedex.pokedex.controller.dto.request.LoginRequest;
import com.pokedex.pokedex.controller.dto.request.UsuarioRequest;
import com.pokedex.pokedex.controller.dto.response.TokenResponse;
import com.pokedex.pokedex.controller.dto.response.UsuarioResponse;
import com.pokedex.pokedex.controller.mapper.UsuarioDtoMapper;
import com.pokedex.pokedex.core.model.Usuario;
import com.pokedex.pokedex.core.service.interfaces.AuthService;
import com.pokedex.pokedex.core.service.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;
    private final UsuarioService usuarioService;
    private final UsuarioDtoMapper mapper;

    @Override
    public ResponseEntity<UsuarioResponse> register(UsuarioRequest request) {
        Usuario creado = authService.register(mapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(creado));
    }

    @Override
    public ResponseEntity<TokenResponse> login(LoginRequest request) {
        String token = authService.login(request.correo(), request.password());
        Usuario usuario = usuarioService.findByCorreo(request.correo());
        TokenResponse response = new TokenResponse(token, "Bearer", usuario.getCorreo(), usuario.getRol());
        return ResponseEntity.ok(response);
    }
}