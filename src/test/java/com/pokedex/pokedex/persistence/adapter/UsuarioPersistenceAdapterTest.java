package com.pokedex.pokedex.persistence.adapter;

import com.pokedex.pokedex.core.model.Usuario;
import com.pokedex.pokedex.persistence.entity.relational.UserEntity;
import com.pokedex.pokedex.persistence.entity.relational.UserJpaRepository;
import com.pokedex.pokedex.persistence.mapper.UsuarioPersistenceMapper;
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
class UsuarioPersistenceAdapterTest {

    @Mock
    private UserJpaRepository repository;

    @Mock
    private UsuarioPersistenceMapper mapper;

    @InjectMocks
    private UsuarioPersistenceAdapter adapter;

    private Usuario usuario;
    private UserEntity entity;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder().id(1L).nombre("Entrenador").correo("trainer@test.com").build();
        entity = UserEntity.builder().id(1L).nombre("Entrenador").correo("trainer@test.com").build();
    }

    @Nested
    @DisplayName("findAll")
    class FindAll {
        @Test
        void returnsList() {
            when(repository.findAll()).thenReturn(List.of(entity));
            when(mapper.toDomain(entity)).thenReturn(usuario);

            var result = adapter.findAll();

            assertEquals(1, result.size());
            assertEquals("Entrenador", result.getFirst().getNombre());
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
        void returnsUsuarioWhenExists() {
            when(repository.findById(1L)).thenReturn(Optional.of(entity));
            when(mapper.toDomain(entity)).thenReturn(usuario);

            var result = adapter.findById(1L);

            assertTrue(result.isPresent());
            assertEquals("Entrenador", result.get().getNombre());
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
    @DisplayName("findByCorreo")
    class FindByCorreo {
        @Test
        void returnsUsuarioWhenExists() {
            when(repository.findByCorreo("trainer@test.com")).thenReturn(Optional.of(entity));
            when(mapper.toDomain(entity)).thenReturn(usuario);

            var result = adapter.findByCorreo("trainer@test.com");

            assertTrue(result.isPresent());
            assertEquals("trainer@test.com", result.get().getCorreo());
            verify(repository).findByCorreo("trainer@test.com");
            verify(mapper).toDomain(entity);
        }

        @Test
        void returnsEmptyWhenNotFound() {
            when(repository.findByCorreo("unknown@test.com")).thenReturn(Optional.empty());

            var result = adapter.findByCorreo("unknown@test.com");

            assertTrue(result.isEmpty());
            verify(repository).findByCorreo("unknown@test.com");
            verifyNoInteractions(mapper);
        }
    }

    @Test
    void existsByCorreo_delegates() {
        when(repository.existsByCorreo("trainer@test.com")).thenReturn(true);

        assertTrue(adapter.existsByCorreo("trainer@test.com"));
        verify(repository).existsByCorreo("trainer@test.com");
    }

    @Nested
    @DisplayName("save")
    class Save {
        @Test
        void convertsAndSaves() {
            var savedEntity = UserEntity.builder().id(1L).nombre("Entrenador").correo("trainer@test.com").build();
            when(mapper.toEntity(usuario)).thenReturn(entity);
            when(repository.save(entity)).thenReturn(savedEntity);
            when(mapper.toDomain(savedEntity)).thenReturn(usuario);

            var result = adapter.save(usuario);

            assertNotNull(result);
            assertEquals("Entrenador", result.getNombre());
            verify(mapper).toEntity(usuario);
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
