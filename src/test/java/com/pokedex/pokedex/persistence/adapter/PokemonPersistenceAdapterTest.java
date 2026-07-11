package com.pokedex.pokedex.persistence.adapter;

import com.pokedex.pokedex.core.model.Pokemon;
import com.pokedex.pokedex.persistence.entity.relational.PokemonEntity;
import com.pokedex.pokedex.persistence.entity.relational.PokemonJpaRepository;
import com.pokedex.pokedex.persistence.mapper.PokemonPersistenceMapper;
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
class PokemonPersistenceAdapterTest {

    @Mock
    private PokemonJpaRepository repository;

    @Mock
    private PokemonPersistenceMapper mapper;

    @InjectMocks
    private PokemonPersistenceAdapter adapter;

    private Pokemon pokemon;
    private PokemonEntity entity;

    @BeforeEach
    void setUp() {
        pokemon = Pokemon.builder().id(1L).numero(25).nombre("Pikachu").build();
        entity = PokemonEntity.builder().id(1L).numero(25).nombre("Pikachu").build();
    }

    @Nested
    @DisplayName("findAll")
    class FindAll {
        @Test
        void returnsList() {
            when(repository.findAll()).thenReturn(List.of(entity));
            when(mapper.toDomain(entity)).thenReturn(pokemon);

            var result = adapter.findAll();

            assertEquals(1, result.size());
            assertEquals("Pikachu", result.getFirst().getNombre());
            verify(repository).findAll();
            verify(mapper).toDomain(entity);
        }

        @Test
        void returnsEmptyList() {
            when(repository.findAll()).thenReturn(List.of());

            var result = adapter.findAll();

            assertTrue(result.isEmpty());
            verify(repository).findAll();
            verifyNoInteractions(mapper);
        }
    }

    @Nested
    @DisplayName("findById")
    class FindById {
        @Test
        void returnsPokemonWhenExists() {
            when(repository.findById(1L)).thenReturn(Optional.of(entity));
            when(mapper.toDomain(entity)).thenReturn(pokemon);

            var result = adapter.findById(1L);

            assertTrue(result.isPresent());
            assertEquals("Pikachu", result.get().getNombre());
            verify(repository).findById(1L);
            verify(mapper).toDomain(entity);
        }

        @Test
        void returnsEmptyWhenNotFound() {
            when(repository.findById(99L)).thenReturn(Optional.empty());

            var result = adapter.findById(99L);

            assertTrue(result.isEmpty());
            verify(repository).findById(99L);
            verifyNoInteractions(mapper);
        }
    }

    @Nested
    @DisplayName("findByNumero")
    class FindByNumero {
        @Test
        void returnsPokemonWhenExists() {
            when(repository.findByNumero(25)).thenReturn(Optional.of(entity));
            when(mapper.toDomain(entity)).thenReturn(pokemon);

            var result = adapter.findByNumero(25);

            assertTrue(result.isPresent());
            assertEquals("Pikachu", result.get().getNombre());
            verify(repository).findByNumero(25);
            verify(mapper).toDomain(entity);
        }

        @Test
        void returnsEmptyWhenNotFound() {
            when(repository.findByNumero(99)).thenReturn(Optional.empty());

            var result = adapter.findByNumero(99);

            assertTrue(result.isEmpty());
            verify(repository).findByNumero(99);
            verifyNoInteractions(mapper);
        }
    }

    @Test
    void existsByNumero_delegates() {
        when(repository.existsByNumero(25)).thenReturn(true);

        assertTrue(adapter.existsByNumero(25));
        verify(repository).existsByNumero(25);
    }

    @Nested
    @DisplayName("save")
    class Save {
        @Test
        void convertsAndSaves() {
            var savedEntity = PokemonEntity.builder().id(1L).numero(25).nombre("Pikachu").build();
            when(mapper.toEntity(pokemon)).thenReturn(entity);
            when(repository.save(entity)).thenReturn(savedEntity);
            when(mapper.toDomain(savedEntity)).thenReturn(pokemon);

            var result = adapter.save(pokemon);

            assertNotNull(result);
            assertEquals("Pikachu", result.getNombre());
            verify(mapper).toEntity(pokemon);
            verify(repository).save(entity);
            verify(mapper).toDomain(savedEntity);
        }
    }

    @Test
    void deleteById_delegates() {
        adapter.deleteById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void existsById_delegates() {
        when(repository.existsById(1L)).thenReturn(true);

        assertTrue(adapter.existsById(1L));
        verify(repository).existsById(1L);
    }
}
