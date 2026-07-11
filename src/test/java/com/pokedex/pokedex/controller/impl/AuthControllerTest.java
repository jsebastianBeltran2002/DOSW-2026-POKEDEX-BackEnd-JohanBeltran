package com.pokedex.pokedex.controller.impl;

import com.pokedex.pokedex.controller.dto.response.UsuarioResponse;
import com.pokedex.pokedex.controller.mapper.UsuarioDtoMapper;
import com.pokedex.pokedex.core.model.Usuario;
import com.pokedex.pokedex.core.service.interfaces.AuthService;
import com.pokedex.pokedex.core.service.interfaces.UsuarioService;
import com.pokedex.pokedex.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private UsuarioDtoMapper mapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    private UsuarioResponse usuarioResponse;

    @BeforeEach
    void setUp() {
        usuarioResponse = new UsuarioResponse(1L, "Entrenador", "trainer@test.com", true, "USUARIO", null);
        lenient().when(mapper.toResponse(any())).thenReturn(usuarioResponse);
    }

    @Test
    void register_returns201_whenValid() throws Exception {
        var usuario = Usuario.builder().id(1L).nombre("Entrenador").correo("trainer@test.com").rol("USUARIO").build();
        when(mapper.toDomain(any())).thenReturn(usuario);
        when(authService.register(any())).thenReturn(usuario);

        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"Entrenador","correo":"trainer@test.com","password":"password123"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void register_returns400_whenInvalid() throws Exception {
        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"","correo":"invalido","password":"123"}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_returns400_whenEmptyBody() throws Exception {
        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_returns200_whenValid() throws Exception {
        var usuario = Usuario.builder().id(1L).correo("trainer@test.com").rol("USUARIO").build();
        when(authService.login("trainer@test.com", "password123")).thenReturn("jwt-token");
        when(usuarioService.findByCorreo("trainer@test.com")).thenReturn(usuario);

        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"correo":"trainer@test.com","password":"password123"}
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.tipo").value("Bearer"))
                .andExpect(jsonPath("$.correo").value("trainer@test.com"))
                .andExpect(jsonPath("$.rol").value("USUARIO"));
    }

    @Test
    void login_returns400_whenInvalid() throws Exception {
        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"correo":"","password":""}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_returns401_whenInvalidCredentials() throws Exception {
        when(authService.login("trainer@test.com", "wrong"))
                .thenThrow(new com.pokedex.pokedex.core.exception.BusinessException("Correo o contraseña incorrectos", "INVALID_CREDENTIALS"));

        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"correo":"trainer@test.com","password":"wrong"}
                                """))
                .andExpect(status().isUnauthorized());
    }
}
