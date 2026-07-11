package com.pokedex.pokedex.controller.impl;

import com.pokedex.pokedex.core.service.interfaces.UsuarioService;
import com.pokedex.pokedex.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void findAll_returns200() throws Exception {
        mockMvc.perform(get("/v1/usuarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void findById_returns200() throws Exception {
        mockMvc.perform(get("/v1/usuarios/1"))
                .andExpect(status().isOk());
    }

    @Test
    void crear_returns200() throws Exception {
        mockMvc.perform(post("/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"Test","correo":"test@test.com","password":"password123"}
                                """))
                .andExpect(status().isOk());
    }

    @Test
    void actualizar_returns200() throws Exception {
        mockMvc.perform(put("/v1/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"Test","correo":"test@test.com","password":"password123"}
                                """))
                .andExpect(status().isOk());
    }

    @Test
    void activarDesactivar_returns200() throws Exception {
        mockMvc.perform(patch("/v1/usuarios/1/estado")
                        .param("activo", "true"))
                .andExpect(status().isOk());
    }

    @Test
    void eliminar_returns204() throws Exception {
        mockMvc.perform(delete("/v1/usuarios/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void compararLegendarios_returns200() throws Exception {
        mockMvc.perform(get("/v1/usuarios/comparar")
                        .param("usuarioId1", "1")
                        .param("usuarioId2", "2"))
                .andExpect(status().isOk());
    }
}
