package com.pokedex.pokedex.controller.impl;

import com.pokedex.pokedex.controller.dto.response.FavoritoResponse;
import com.pokedex.pokedex.controller.mapper.FavoritoDtoMapper;
import com.pokedex.pokedex.core.model.Favorito;
import com.pokedex.pokedex.core.service.interfaces.FavoritoService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FavoritoController.class)
@AutoConfigureMockMvc(addFilters = false)
class FavoritoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FavoritoService favoritoService;

    @MockitoBean
    private FavoritoDtoMapper mapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    private FavoritoResponse favoritoResponse;

    @BeforeEach
    void setUp() {
        favoritoResponse = new FavoritoResponse(1L, 1L, 25L, null);
        lenient().when(mapper.toResponse(any())).thenReturn(favoritoResponse);
        lenient().when(mapper.toResponseList(any())).thenReturn(List.of(favoritoResponse));
    }

    @Test
    void guardar_returns201_whenSuccess() throws Exception {
        var favorito = Favorito.builder().id(1L).usuarioId(1L).pokemonId(25L).build();
        when(favoritoService.guardar(1L, 25L)).thenReturn(favorito);

        mockMvc.perform(post("/v1/favoritos/1/25"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void guardar_returns409_whenDuplicate() throws Exception {
        when(favoritoService.guardar(1L, 25L)).thenThrow(
                new com.pokedex.pokedex.core.exception.DuplicateResourceException("Favorito", "usuarioId y pokemonId", "1 - 25"));

        mockMvc.perform(post("/v1/favoritos/1/25"))
                .andExpect(status().isConflict());
    }

    @Test
    void guardar_returns404_whenUserNotFound() throws Exception {
        when(favoritoService.guardar(1L, 99L)).thenThrow(
                new com.pokedex.pokedex.core.exception.ResourceNotFoundException("Usuario", "id", 1L));

        mockMvc.perform(post("/v1/favoritos/1/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminar_returns204_whenSuccess() throws Exception {
        mockMvc.perform(delete("/v1/favoritos/1/25"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminar_returns404_whenNotFound() throws Exception {
        doThrow(new com.pokedex.pokedex.core.exception.ResourceNotFoundException("Favorito", "usuarioId y pokemonId", "1 - 99"))
                .when(favoritoService).eliminar(1L, 99L);

        mockMvc.perform(delete("/v1/favoritos/1/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findByUsuarioId_returns200() throws Exception {
        when(favoritoService.findByUsuarioId(1L)).thenReturn(List.of());
        when(mapper.toResponseList(any())).thenReturn(List.of());

        mockMvc.perform(get("/v1/favoritos/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
