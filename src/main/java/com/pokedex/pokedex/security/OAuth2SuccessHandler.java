package com.pokedex.pokedex.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokedex.pokedex.controller.dto.response.TokenResponse;
import com.pokedex.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.pokedex.core.model.Usuario;
import com.pokedex.pokedex.core.service.interfaces.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UsuarioService usuarioService;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String correo = oAuth2User.getAttribute("email");
        String nombre = oAuth2User.getAttribute("name");

        if (correo == null || correo.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Google no devolvió un correo válido");
            return;
        }

        Usuario usuario;
        try {
            usuario = usuarioService.findByCorreo(correo);
        } catch (ResourceNotFoundException e) {
            log.info("Creando usuario nuevo a partir de login con Google: {}", correo);
            Usuario nuevo = Usuario.builder()
                    .nombre(nombre != null && !nombre.isBlank() ? nombre : correo)
                    .correo(correo)
                    .password(UUID.randomUUID().toString())
                    .rol("USUARIO")
                    .build();
            usuario = usuarioService.crear(nuevo);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getCorreo());
        String jwt = jwtService.generateToken(userDetails);

        TokenResponse tokenResponse = new TokenResponse(jwt, "Bearer", usuario.getCorreo(), usuario.getRol());

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(response.getOutputStream(), tokenResponse);
    }
}