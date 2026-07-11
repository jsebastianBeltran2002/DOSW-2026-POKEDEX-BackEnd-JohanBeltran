package com.pokedex.pokedex.core.service.impl;

import com.pokedex.pokedex.core.exception.DuplicateResourceException;
import com.pokedex.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.pokedex.core.model.Usuario;
import com.pokedex.pokedex.core.service.interfaces.UsuarioPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioPersistencePort usuarioPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .id(1L)
                .nombre("Entrenador")
                .correo("trainer@test.com")
                .password("raw-password")
                .activo(true)
                .rol("USUARIO")
                .build();
    }

    @Nested
    @DisplayName("crear")
    class Crear {

        @Test
        @DisplayName("crea usuario exitosamente con valores por defecto")
        void createsSuccessfully() {
            when(usuarioPort.existsByCorreo("trainer@test.com")).thenReturn(false);
            when(passwordEncoder.encode("raw-password")).thenReturn("encoded-password");
            when(usuarioPort.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

            var result = usuarioService.crear(usuario);

            assertNotNull(result);
            assertTrue(result.getActivo());
            assertEquals("USUARIO", result.getRol());
            assertEquals("encoded-password", result.getPassword());
            assertNotNull(result.getFechaRegistro());
            verify(usuarioPort).existsByCorreo("trainer@test.com");
            verify(passwordEncoder).encode("raw-password");
            verify(usuarioPort).save(any(Usuario.class));
        }

        @Test
        @DisplayName("crea usuario con rol personalizado si se proporciona")
        void createsWithCustomRole() {
            var adminUser = usuario.toBuilder().rol("ADMINISTRADOR").build();
            when(usuarioPort.existsByCorreo("trainer@test.com")).thenReturn(false);
            when(passwordEncoder.encode("raw-password")).thenReturn("encoded-password");
            when(usuarioPort.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

            var result = usuarioService.crear(adminUser);

            assertEquals("ADMINISTRADOR", result.getRol());
        }

        @Test
        @DisplayName("lanza DuplicateResourceException cuando el correo ya existe")
        void throwsExceptionWhenDuplicate() {
            when(usuarioPort.existsByCorreo("trainer@test.com")).thenReturn(true);

            assertThrows(DuplicateResourceException.class, () -> usuarioService.crear(usuario));
            verify(usuarioPort).existsByCorreo("trainer@test.com");
            verifyNoMoreInteractions(usuarioPort);
            verifyNoInteractions(passwordEncoder);
        }
    }

    @Nested
    @DisplayName("findById")
    class FindById {

        @Test
        @DisplayName("retorna usuario cuando existe")
        void returnsUsuarioWhenExists() {
            when(usuarioPort.findById(1L)).thenReturn(Optional.of(usuario));

            var result = usuarioService.findById(1L);

            assertNotNull(result);
            assertEquals("Entrenador", result.getNombre());
            verify(usuarioPort).findById(1L);
        }

        @Test
        @DisplayName("lanza ResourceNotFoundException cuando no existe")
        void throwsExceptionWhenNotFound() {
            when(usuarioPort.findById(99L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> usuarioService.findById(99L));
            verify(usuarioPort).findById(99L);
        }
    }

    @Nested
    @DisplayName("findByCorreo")
    class FindByCorreo {

        @Test
        @DisplayName("retorna usuario cuando existe")
        void returnsUsuarioWhenExists() {
            when(usuarioPort.findByCorreo("trainer@test.com")).thenReturn(Optional.of(usuario));

            var result = usuarioService.findByCorreo("trainer@test.com");

            assertNotNull(result);
            assertEquals("trainer@test.com", result.getCorreo());
            verify(usuarioPort).findByCorreo("trainer@test.com");
        }

        @Test
        @DisplayName("lanza ResourceNotFoundException cuando no existe")
        void throwsExceptionWhenNotFound() {
            when(usuarioPort.findByCorreo("unknown@test.com")).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> usuarioService.findByCorreo("unknown@test.com"));
            verify(usuarioPort).findByCorreo("unknown@test.com");
        }
    }

    @Nested
    @DisplayName("findAll")
    class FindAll {

        @Test
        @DisplayName("retorna lista de usuarios")
        void returnsAllUsuarios() {
            when(usuarioPort.findAll()).thenReturn(List.of(usuario));

            var result = usuarioService.findAll();

            assertEquals(1, result.size());
            assertEquals("Entrenador", result.getFirst().getNombre());
            verify(usuarioPort).findAll();
        }

        @Test
        @DisplayName("retorna lista vacia cuando no hay usuarios")
        void returnsEmptyList() {
            when(usuarioPort.findAll()).thenReturn(List.of());

            var result = usuarioService.findAll();

            assertTrue(result.isEmpty());
            verify(usuarioPort).findAll();
        }
    }

    @Nested
    @DisplayName("actualizar")
    class Actualizar {

        @Test
        @DisplayName("actualiza nombre y correo del usuario")
        void updatesSuccessfully() {
            var updates = Usuario.builder().nombre("NuevoNombre").correo("nuevo@test.com").build();
            when(usuarioPort.findById(1L)).thenReturn(Optional.of(usuario));
            when(usuarioPort.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

            var result = usuarioService.actualizar(1L, updates);

            assertEquals("NuevoNombre", result.getNombre());
            assertEquals("nuevo@test.com", result.getCorreo());
            verify(usuarioPort).findById(1L);
            verify(usuarioPort).save(any(Usuario.class));
        }

        @Test
        @DisplayName("lanza ResourceNotFoundException cuando no existe")
        void throwsExceptionWhenNotFound() {
            when(usuarioPort.findById(99L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> usuarioService.actualizar(99L, usuario));
            verify(usuarioPort).findById(99L);
            verify(usuarioPort, never()).save(any());
        }
    }

    @Nested
    @DisplayName("eliminar")
    class Eliminar {

        @Test
        @DisplayName("elimina usuario cuando existe")
        void deletesWhenExists() {
            when(usuarioPort.existsById(1L)).thenReturn(true);

            assertDoesNotThrow(() -> usuarioService.eliminar(1L));
            verify(usuarioPort).existsById(1L);
            verify(usuarioPort).deleteById(1L);
        }

        @Test
        @DisplayName("lanza ResourceNotFoundException cuando no existe")
        void throwsExceptionWhenNotFound() {
            when(usuarioPort.existsById(99L)).thenReturn(false);

            assertThrows(ResourceNotFoundException.class, () -> usuarioService.eliminar(99L));
            verify(usuarioPort).existsById(99L);
            verify(usuarioPort, never()).deleteById(any());
        }
    }

    @Nested
    @DisplayName("activarDesactivar")
    class ActivarDesactivar {

        @Test
        @DisplayName("activa usuario exitosamente")
        void activatesSuccessfully() {
            var inactivo = usuario.toBuilder().activo(false).build();
            when(usuarioPort.findById(1L)).thenReturn(Optional.of(inactivo));
            when(usuarioPort.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

            var result = usuarioService.activarDesactivar(1L, true);

            assertTrue(result.getActivo());
            verify(usuarioPort).findById(1L);
            verify(usuarioPort).save(any(Usuario.class));
        }

        @Test
        @DisplayName("desactiva usuario exitosamente")
        void deactivatesSuccessfully() {
            when(usuarioPort.findById(1L)).thenReturn(Optional.of(usuario));
            when(usuarioPort.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

            var result = usuarioService.activarDesactivar(1L, false);

            assertFalse(result.getActivo());
            verify(usuarioPort).findById(1L);
            verify(usuarioPort).save(any(Usuario.class));
        }

        @Test
        @DisplayName("lanza ResourceNotFoundException cuando no existe")
        void throwsExceptionWhenNotFound() {
            when(usuarioPort.findById(99L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> usuarioService.activarDesactivar(99L, true));
            verify(usuarioPort).findById(99L);
            verify(usuarioPort, never()).save(any());
        }
    }
}
