package com.pokedex.pokedex;

import com.pokedex.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.pokedex.core.model.Pokemon;
import com.pokedex.pokedex.core.service.impl.PokemonServiceImpl;
import com.pokedex.pokedex.core.service.interfaces.PokemonPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PokemonServiceImplTest {

    @Mock
    private PokemonPersistencePort pokemonPort;

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
        when(pokemonPort.findAll()).thenReturn(List.of());
        var result = pokemonService.findAll();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("debe lanzar ResourceNotFoundException")
    void findById_throwsException() {
        when(pokemonPort.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> pokemonService.findById(1L));
    }

    @Test
    @DisplayName("debe lanzar ResourceNotFoundException")
    void delete_throwsException() {
        when(pokemonPort.existsById(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> pokemonService.delete(1L));
    }
}