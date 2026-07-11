package com.pokedex.pokedex.controller.impl;

import com.pokedex.pokedex.controller.dto.response.EquipoResponse;
import com.pokedex.pokedex.controller.mapper.EquipoDtoMapper;
import com.pokedex.pokedex.core.model.Equipo;
import com.pokedex.pokedex.core.model.Pokemon;
import com.pokedex.pokedex.core.service.interfaces.EquipoService;
import com.pokedex.pokedex.core.service.interfaces.PokemonService;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EquipoController.class)
@AutoConfigureMockMvc(addFilters = false)
class EquipoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EquipoService equipoService;

    @MockitoBean
    private PokemonService pokemonService;

    @MockitoBean
    private EquipoDtoMapper mapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    private EquipoResponse equipoResponse;

    @BeforeEach
    void setUp() {
        equipoResponse = new EquipoResponse(1L, "Mi Equipo", null, null, 1L, List.of());
        lenient().when(mapper.toResponse(any())).thenReturn(equipoResponse);
    }

    @Test
    void findByUsuarioId_returns200() throws Exception {
        when(equipoService.findByUsuarioId(1L)).thenReturn(List.of());

        mockMvc.perform(get("/v1/equipos/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void findById_returns200_whenExists() throws Exception {
        var equipo = Equipo.builder().id(1L).nombre("Mi Equipo").build();
        when(equipoService.findById(1L)).thenReturn(equipo);

        mockMvc.perform(get("/v1/equipos/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void findById_returns404_whenNotFound() throws Exception {
        when(equipoService.findById(99L)).thenThrow(
                new com.pokedex.pokedex.core.exception.ResourceNotFoundException("Equipo", "id", 99L));

        mockMvc.perform(get("/v1/equipos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crear_returns201_whenValid() throws Exception {
        when(pokemonService.findById(anyLong())).thenReturn(
                Pokemon.builder().id(1L).build());
        when(equipoService.crear(any())).thenReturn(
                Equipo.builder().id(1L).nombre("Mi Equipo").usuarioId(1L).build());

        mockMvc.perform(post("/v1/equipos/usuario/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"Mi Equipo","pokemonIds":[1,2,3]}
                                """))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void crear_returns400_whenInvalid() throws Exception {
        mockMvc.perform(post("/v1/equipos/usuario/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"","pokemonIds":[]}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void crear_returns400_whenTooManyPokemon() throws Exception {
        mockMvc.perform(post("/v1/equipos/usuario/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"Equipo","pokemonIds":[1,2,3,4,5,6,7]}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void eliminar_returns204_whenExists() throws Exception {
        mockMvc.perform(delete("/v1/equipos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminar_returns404_whenNotFound() throws Exception {
        doThrow(new com.pokedex.pokedex.core.exception.ResourceNotFoundException("Equipo", "id", 99L))
                .when(equipoService).eliminar(99L);

        mockMvc.perform(delete("/v1/equipos/99"))
                .andExpect(status().isNotFound());
    }
}
