package com.pokedex.pokedex.core.service.impl;

import com.pokedex.pokedex.core.exception.DuplicateResourceException;
import com.pokedex.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.pokedex.core.model.Favorito;
import com.pokedex.pokedex.persistence.entity.relational.FavoritoEntity;
import com.pokedex.pokedex.persistence.entity.relational.FavoritoJpaRepository;
import com.pokedex.pokedex.persistence.entity.relational.PokemonEntity;
import com.pokedex.pokedex.persistence.entity.relational.UserEntity;
import com.pokedex.pokedex.persistence.entity.relational.PokemonJpaRepository;
import com.pokedex.pokedex.persistence.entity.relational.UserJpaRepository;
import com.pokedex.pokedex.persistence.mapper.FavoritoPersistenceMapper;
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
class FavoritoServiceImplTest {

    @Mock
    private FavoritoJpaRepository favoritoRepository;

    @Mock
    private UserJpaRepository userRepository;

    @Mock
    private PokemonJpaRepository pokemonRepository;

    @Mock
    private FavoritoPersistenceMapper mapper;

    @InjectMocks
    private FavoritoServiceImpl favoritoService;

    private Favorito favorito;
    private FavoritoEntity favoritoEntity;
    private UserEntity userEntity;
    private PokemonEntity pokemonEntity;

    @BeforeEach
    void setUp() {
        userEntity = UserEntity.builder().id(1L).nombre("Entrenador").correo("trainer@test.com").build();
        pokemonEntity = PokemonEntity.builder().id(25L).numero(25).nombre("Pikachu").build();

        favorito = Favorito.builder()
                .id(1L)
                .usuarioId(1L)
                .pokemonId(25L)
                .build();

        favoritoEntity = FavoritoEntity.builder()
                .id(1L)
                .usuario(userEntity)
                .pokemon(pokemonEntity)
                .build();
    }

    @Nested
    @DisplayName("guardar")
    class Guardar {

        @Test
        @DisplayName("guarda favorito exitosamente")
        void savesSuccessfully() {
            when(favoritoRepository.existsByUsuarioIdAndPokemonId(1L, 25L)).thenReturn(false);
            when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
            when(pokemonRepository.findById(25L)).thenReturn(Optional.of(pokemonEntity));
            when(favoritoRepository.save(any(FavoritoEntity.class))).thenReturn(favoritoEntity);
            when(mapper.toDomain(favoritoEntity)).thenReturn(favorito);

            var result = favoritoService.guardar(1L, 25L);

            assertNotNull(result);
            assertEquals(1L, result.getUsuarioId());
            assertEquals(25L, result.getPokemonId());
            verify(favoritoRepository).existsByUsuarioIdAndPokemonId(1L, 25L);
            verify(userRepository).findById(1L);
            verify(pokemonRepository).findById(25L);
            verify(favoritoRepository).save(any(FavoritoEntity.class));
            verify(mapper).toDomain(favoritoEntity);
        }

        @Test
        @DisplayName("lanza DuplicateResourceException cuando ya existe")
        void throwsExceptionWhenDuplicate() {
            when(favoritoRepository.existsByUsuarioIdAndPokemonId(1L, 25L)).thenReturn(true);

            assertThrows(DuplicateResourceException.class, () -> favoritoService.guardar(1L, 25L));
            verify(favoritoRepository).existsByUsuarioIdAndPokemonId(1L, 25L);
            verifyNoMoreInteractions(favoritoRepository, userRepository, pokemonRepository);
        }

        @Test
        @DisplayName("lanza ResourceNotFoundException cuando el usuario no existe")
        void throwsExceptionWhenUserNotFound() {
            when(favoritoRepository.existsByUsuarioIdAndPokemonId(1L, 25L)).thenReturn(false);
            when(userRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> favoritoService.guardar(1L, 25L));
            verify(userRepository).findById(1L);
        }

        @Test
        @DisplayName("lanza ResourceNotFoundException cuando el pokemon no existe")
        void throwsExceptionWhenPokemonNotFound() {
            when(favoritoRepository.existsByUsuarioIdAndPokemonId(1L, 25L)).thenReturn(false);
            when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
            when(pokemonRepository.findById(25L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> favoritoService.guardar(1L, 25L));
            verify(pokemonRepository).findById(25L);
        }
    }

    @Nested
    @DisplayName("eliminar")
    class Eliminar {

        @Test
        @DisplayName("elimina favorito existente")
        void deletesWhenExists() {
            when(favoritoRepository.findByUsuarioIdAndPokemonId(1L, 25L)).thenReturn(Optional.of(favoritoEntity));

            assertDoesNotThrow(() -> favoritoService.eliminar(1L, 25L));
            verify(favoritoRepository).findByUsuarioIdAndPokemonId(1L, 25L);
            verify(favoritoRepository).delete(favoritoEntity);
        }

        @Test
        @DisplayName("lanza ResourceNotFoundException cuando no existe")
        void throwsExceptionWhenNotFound() {
            when(favoritoRepository.findByUsuarioIdAndPokemonId(99L, 99L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> favoritoService.eliminar(99L, 99L));
            verify(favoritoRepository).findByUsuarioIdAndPokemonId(99L, 99L);
            verify(favoritoRepository, never()).delete(any());
        }
    }

    @Nested
    @DisplayName("findByUsuarioId")
    class FindByUsuarioId {

        @Test
        @DisplayName("retorna lista de favoritos del usuario")
        void returnsFavoritosForUser() {
            when(favoritoRepository.findByUsuarioId(1L)).thenReturn(List.of(favoritoEntity));
            when(mapper.toDomain(favoritoEntity)).thenReturn(favorito);

            var result = favoritoService.findByUsuarioId(1L);

            assertEquals(1, result.size());
            assertEquals(25L, result.getFirst().getPokemonId());
            verify(favoritoRepository).findByUsuarioId(1L);
            verify(mapper).toDomain(favoritoEntity);
        }

        @Test
        @DisplayName("retorna lista vacia cuando el usuario no tiene favoritos")
        void returnsEmptyList() {
            when(favoritoRepository.findByUsuarioId(1L)).thenReturn(List.of());

            var result = favoritoService.findByUsuarioId(1L);

            assertTrue(result.isEmpty());
            verify(favoritoRepository).findByUsuarioId(1L);
            verifyNoInteractions(mapper);
        }
    }
}
