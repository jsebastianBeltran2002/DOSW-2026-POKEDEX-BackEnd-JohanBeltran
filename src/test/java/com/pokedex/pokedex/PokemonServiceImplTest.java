package com.pokedex.pokedex;

import com.pokedex.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.pokedex.core.model.Pokemon;
import com.pokedex.pokedex.core.service.impl.PokemonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PokemonServiceImplTest {

    @InjectMocks
    private PokemonServiceImpl pokemonService;

    private Pokemon pikachu;

    @BeforeEach
    void setUp() {
        pikachu = Pokemon.builder()
                .id(1L)
                .numero(25)
                .nombre("Pikachu")
                .tipos(java.util.List.of("Eléctrico"))
                .region("Kanto")
                .generacion(1)
                .legendario(false)
                .tieneMega(false)
                .hp(35)
                .ataque(55)
                .defensa(40)
                .ataqueEspecial(50)
                .defensaEspecial(50)
                .velocidad(90)
                .build();
    }

    @Test
    @DisplayName("debe retornar lista vacía")
    void findAll_returnsEmptyList() {
        var result = pokemonService.findAll();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("debe lanzar ResourceNotFoundException")
    void findById_throwsException() {
        assertThrows(ResourceNotFoundException.class, () -> pokemonService.findById(1L));
    }

    @Test
    @DisplayName("debe lanzar ResourceNotFoundException")
    void delete_throwsException() {
        assertThrows(ResourceNotFoundException.class, () -> pokemonService.delete(1L));
    }
}