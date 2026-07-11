package com.pokedex.pokedex.core.service.impl;

import com.pokedex.pokedex.core.exception.DuplicateResourceException;
import com.pokedex.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.pokedex.core.model.Equipo;
import com.pokedex.pokedex.core.model.Pokemon;
import com.pokedex.pokedex.persistence.entity.relational.PokemonEntity;
import com.pokedex.pokedex.persistence.entity.relational.PokemonJpaRepository;
import com.pokedex.pokedex.persistence.entity.relational.TeamEntity;
import com.pokedex.pokedex.persistence.entity.relational.TeamJpaRepository;
import com.pokedex.pokedex.persistence.entity.relational.UserEntity;
import com.pokedex.pokedex.persistence.entity.relational.UserJpaRepository;
import com.pokedex.pokedex.persistence.mapper.EquipoPersistenceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
class EquipoServiceImplTest {

    @Mock
    private TeamJpaRepository equipoRepository;

    @Mock
    private UserJpaRepository userRepository;

    @Mock
    private PokemonJpaRepository pokemonRepository;

    @Mock
    private EquipoPersistenceMapper mapper;

    @InjectMocks
    private EquipoServiceImpl equipoService;

    private Equipo equipo;
    private TeamEntity teamEntity;
    private UserEntity userEntity;
    private PokemonEntity pokemonEntity;
    private Pokemon pokemon;

    @BeforeEach
    void setUp() {
        pokemon = Pokemon.builder().id(1L).numero(25).nombre("Pikachu").build();

        pokemonEntity = PokemonEntity.builder().id(1L).numero(25).nombre("Pikachu").build();

        userEntity = UserEntity.builder().id(1L).nombre("Entrenador").correo("trainer@test.com").build();

        equipo = Equipo.builder()
                .id(1L)
                .nombre("Mi Equipo")
                .usuarioId(1L)
                .pokemons(List.of(pokemon))
                .build();

        teamEntity = TeamEntity.builder()
                .id(1L)
                .nombre("Mi Equipo")
                .usuario(userEntity)
                .pokemons(List.of(pokemonEntity))
                .build();
    }

    @Nested
    @DisplayName("crear")
    class Crear {

        @Test
        @DisplayName("crea equipo exitosamente")
        void createsSuccessfully() {
            when(equipoRepository.existsByNombreAndUsuarioId("Mi Equipo", 1L)).thenReturn(false);
            when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
            when(pokemonRepository.findById(1L)).thenReturn(Optional.of(pokemonEntity));
            when(equipoRepository.save(any(TeamEntity.class))).thenReturn(teamEntity);
            when(mapper.toDomain(teamEntity)).thenReturn(equipo);

            var result = equipoService.crear(equipo);

            assertNotNull(result);
            assertEquals("Mi Equipo", result.getNombre());
            assertEquals(1L, result.getUsuarioId());
            verify(equipoRepository).existsByNombreAndUsuarioId("Mi Equipo", 1L);
            verify(userRepository).findById(1L);
            verify(pokemonRepository).findById(1L);
            verify(equipoRepository).save(any(TeamEntity.class));
            verify(mapper).toDomain(teamEntity);
        }

        @Test
        @DisplayName("lanza DuplicateResourceException cuando el nombre ya existe para el usuario")
        void throwsExceptionWhenDuplicate() {
            when(equipoRepository.existsByNombreAndUsuarioId("Mi Equipo", 1L)).thenReturn(true);

            assertThrows(DuplicateResourceException.class, () -> equipoService.crear(equipo));
            verify(equipoRepository).existsByNombreAndUsuarioId("Mi Equipo", 1L);
            verifyNoMoreInteractions(equipoRepository, userRepository, pokemonRepository);
        }

        @Test
        @DisplayName("lanza ResourceNotFoundException cuando el usuario no existe")
        void throwsExceptionWhenUserNotFound() {
            when(equipoRepository.existsByNombreAndUsuarioId("Mi Equipo", 1L)).thenReturn(false);
            when(userRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> equipoService.crear(equipo));
            verify(userRepository).findById(1L);
        }

        @Test
        @DisplayName("lanza ResourceNotFoundException cuando un pokemon no existe")
        void throwsExceptionWhenPokemonNotFound() {
            when(equipoRepository.existsByNombreAndUsuarioId("Mi Equipo", 1L)).thenReturn(false);
            when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
            when(pokemonRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> equipoService.crear(equipo));
            verify(pokemonRepository).findById(1L);
        }
    }

    @Nested
    @DisplayName("findById")
    class FindById {

        @Test
        @DisplayName("retorna equipo cuando existe")
        void returnsEquipoWhenExists() {
            when(equipoRepository.findById(1L)).thenReturn(Optional.of(teamEntity));
            when(mapper.toDomain(teamEntity)).thenReturn(equipo);

            var result = equipoService.findById(1L);

            assertNotNull(result);
            assertEquals("Mi Equipo", result.getNombre());
            verify(equipoRepository).findById(1L);
            verify(mapper).toDomain(teamEntity);
        }

        @Test
        @DisplayName("lanza ResourceNotFoundException cuando no existe")
        void throwsExceptionWhenNotFound() {
            when(equipoRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> equipoService.findById(99L));
            verify(equipoRepository).findById(99L);
            verifyNoInteractions(mapper);
        }
    }

    @Nested
    @DisplayName("findByUsuarioId")
    class FindByUsuarioId {

        @Test
        @DisplayName("retorna lista de equipos del usuario")
        void returnsEquiposForUser() {
            when(equipoRepository.findByUsuarioId(1L)).thenReturn(List.of(teamEntity));
            when(mapper.toDomain(teamEntity)).thenReturn(equipo);

            var result = equipoService.findByUsuarioId(1L);

            assertEquals(1, result.size());
            assertEquals("Mi Equipo", result.getFirst().getNombre());
            verify(equipoRepository).findByUsuarioId(1L);
            verify(mapper).toDomain(teamEntity);
        }

        @Test
        @DisplayName("retorna lista vacia cuando el usuario no tiene equipos")
        void returnsEmptyList() {
            when(equipoRepository.findByUsuarioId(1L)).thenReturn(List.of());

            var result = equipoService.findByUsuarioId(1L);

            assertTrue(result.isEmpty());
            verify(equipoRepository).findByUsuarioId(1L);
            verifyNoInteractions(mapper);
        }
    }

    @Nested
    @DisplayName("eliminar")
    class Eliminar {

        @Test
        @DisplayName("elimina equipo cuando existe")
        void deletesWhenExists() {
            when(equipoRepository.existsById(1L)).thenReturn(true);

            assertDoesNotThrow(() -> equipoService.eliminar(1L));
            verify(equipoRepository).existsById(1L);
            verify(equipoRepository).deleteById(1L);
        }

        @Test
        @DisplayName("lanza ResourceNotFoundException cuando no existe")
        void throwsExceptionWhenNotFound() {
            when(equipoRepository.existsById(99L)).thenReturn(false);

            assertThrows(ResourceNotFoundException.class, () -> equipoService.eliminar(99L));
            verify(equipoRepository).existsById(99L);
            verify(equipoRepository, never()).deleteById(any());
        }
    }
}
