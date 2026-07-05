package com.pokedex.pokedex.controller.api;

import com.pokedex.pokedex.controller.dto.request.LoginRequest;
import com.pokedex.pokedex.controller.dto.request.UsuarioRequest;
import com.pokedex.pokedex.controller.dto.response.TokenResponse;
import com.pokedex.pokedex.controller.dto.response.UsuarioResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Auth", description = "Registro y autenticación")
@RequestMapping("/v1/auth")
public interface AuthApi {

    @Operation(summary = "Registrar un nuevo usuario")
    @PostMapping("/register")
    ResponseEntity<UsuarioResponse> register(@Valid @RequestBody UsuarioRequest request);

    @Operation(summary = "Login — retorna JWT")
    @PostMapping("/login")
    ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request);
}