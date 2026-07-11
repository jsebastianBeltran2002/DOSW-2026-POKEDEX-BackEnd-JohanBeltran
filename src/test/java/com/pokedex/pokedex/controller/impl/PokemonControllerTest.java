package com.pokedex.pokedex.controller.impl;

import com.pokedex.pokedex.controller.dto.response.PokemonResponse;
import com.pokedex.pokedex.controller.mapper.PokemonDtoMapper;
import com.pokedex.pokedex.core.model.Pokemon;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PokemonController.class)
@AutoConfigureMockMvc(addFilters = false)
class PokemonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PokemonService pokemonService;

    @MockitoBean
    private PokemonDtoMapper mapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    private final Pokemon pikachu = Pokemon.builder()
            .id(1L).numero(25).nombre("Pikachu").tipos(List.of("Electrico"))
            .region("Kanto").generacion(1).legendario(false).tieneMega(false)
            .hp(35).ataque(55).defensa(40).ataqueEspecial(50).defensaEspecial(50).velocidad(90)
            .build();

    private PokemonResponse pikachuResponse;

    @BeforeEach
    void setUp() {
        pikachuResponse = new PokemonResponse(1L, 25, "Pikachu", null, null, null, null,
                List.of("Electrico"), "Kanto", 1, false, false, 35, 55, 40, 50, 50, 90);
        lenient().when(mapper.toResponse(pikachu)).thenReturn(pikachuResponse);
        lenient().when(mapper.toResponseList(any())).thenReturn(List.of(pikachuResponse));
    }

    @Test
    void findAll_returns200() throws Exception {
        when(pokemonService.findAll()).thenReturn(List.of(pikachu));

        mockMvc.perform(get("/v1/pokemon"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void findById_returns200_whenExists() throws Exception {
        when(pokemonService.findById(1L)).thenReturn(pikachu);

        mockMvc.perform(get("/v1/pokemon/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void findById_returns404_whenNotFound() throws Exception {
        when(pokemonService.findById(99L)).thenThrow(
                new com.pokedex.pokedex.core.exception.ResourceNotFoundException("Pokemon", "id", 99L));

        mockMvc.perform(get("/v1/pokemon/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscar_returns200_whenCriterioIsNumber() throws Exception {
        when(pokemonService.findByNumero(25)).thenReturn(pikachu);

        mockMvc.perform(get("/v1/pokemon/buscar").param("criterio", "25"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void buscar_returns200_whenCriterioIsName() throws Exception {
        when(pokemonService.findAll()).thenReturn(List.of(pikachu));

        mockMvc.perform(get("/v1/pokemon/buscar").param("criterio", "Pikachu"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void buscar_returns404_whenNameNotFound() throws Exception {
        when(pokemonService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/v1/pokemon/buscar").param("criterio", "Unknown"))
                .andExpect(status().isNotFound());
    }

    @Test
    void filtrar_returns200() throws Exception {
        when(pokemonService.filtrar(any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(List.of(pikachu));

        mockMvc.perform(get("/v1/pokemon/filtrar")
                        .param("region", "Kanto"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void ordenar_returns200() throws Exception {
        when(pokemonService.ordenar("nombre", "asc")).thenReturn(List.of(pikachu));

        mockMvc.perform(get("/v1/pokemon/ordenar")
                        .param("criterio", "nombre")
                        .param("direccion", "asc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void create_returns201_whenValid() throws Exception {
        when(mapper.toDomain(any())).thenReturn(pikachu);
        when(pokemonService.create(any())).thenReturn(pikachu);

        mockMvc.perform(post("/v1/pokemon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"numero":25,"nombre":"Pikachu","tipos":["Electrico"],"altura":0.4,"peso":6.0}
                                """))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void create_returns400_whenInvalid() throws Exception {
        mockMvc.perform(post("/v1/pokemon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"numero":-1,"nombre":"","tipos":[]}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_returns400_whenEmptyBody() throws Exception {
        mockMvc.perform(post("/v1/pokemon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_returns409_whenDuplicate() throws Exception {
        when(mapper.toDomain(any())).thenReturn(pikachu);
        when(pokemonService.create(any())).thenThrow(
                new com.pokedex.pokedex.core.exception.DuplicateResourceException("Pokemon", "numero", 25));

        mockMvc.perform(post("/v1/pokemon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"numero":25,"nombre":"Pikachu","tipos":["Electrico"],"altura":0.4,"peso":6.0}
                                """))
                .andExpect(status().isConflict());
    }

    @Test
    void update_returns200_whenValid() throws Exception {
        when(mapper.toDomain(any())).thenReturn(pikachu);
        when(pokemonService.update(anyLong(), any())).thenReturn(pikachu);

        mockMvc.perform(put("/v1/pokemon/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"numero":25,"nombre":"Raichu","tipos":["Electrico"],"altura":0.4,"peso":6.0}
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void update_returns400_whenInvalid() throws Exception {
        mockMvc.perform(put("/v1/pokemon/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_returns404_whenNotFound() throws Exception {
        when(mapper.toDomain(any())).thenReturn(pikachu);
        when(pokemonService.update(anyLong(), any())).thenThrow(
                new com.pokedex.pokedex.core.exception.ResourceNotFoundException("Pokemon", "id", 99L));

        mockMvc.perform(put("/v1/pokemon/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"numero":25,"nombre":"Pikachu","tipos":["Electrico"],"altura":0.4,"peso":6.0}
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_returns204_whenExists() throws Exception {
        mockMvc.perform(delete("/v1/pokemon/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_returns404_whenNotFound() throws Exception {
        doThrow(new com.pokedex.pokedex.core.exception.ResourceNotFoundException("Pokemon", "id", 99L))
                .when(pokemonService).delete(99L);

        mockMvc.perform(delete("/v1/pokemon/99"))
                .andExpect(status().isNotFound());
    }
}
