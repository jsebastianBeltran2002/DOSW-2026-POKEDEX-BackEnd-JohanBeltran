package com.pokedex.pokedex.core.service.impl;

import com.pokedex.pokedex.controller.dto.request.IntercambiarRequest;
import com.pokedex.pokedex.core.exception.BusinessException;
import com.pokedex.pokedex.core.exception.DuplicateResourceException;
import com.pokedex.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.pokedex.core.model.Usuario;
import com.pokedex.pokedex.core.service.interfaces.UsuarioPersistencePort;
import com.pokedex.pokedex.persistence.entity.relational.PokemonEntity;
import com.pokedex.pokedex.persistence.entity.relational.PokemonJpaRepository;
import com.pokedex.pokedex.persistence.entity.relational.TeamEntity;
import com.pokedex.pokedex.persistence.entity.relational.TeamJpaRepository;
import com.pokedex.pokedex.persistence.entity.relational.UserEntity;
import com.pokedex.pokedex.persistence.entity.relational.UserJpaRepository;
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

import java.util.ArrayList;
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

    @Mock
    private UserJpaRepository userRepo;

    @Mock
    private PokemonJpaRepository pokemonRepo;

    @Mock
    private TeamJpaRepository teamRepo;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuario;
    private UserEntity userEntity1;
    private UserEntity userEntity2;
    private PokemonEntity pokemon1;
    private PokemonEntity pokemon2;
    private TeamEntity team1;
    private TeamEntity team2;

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

        userEntity1 = UserEntity.builder().id(1L).nombre("Entrenador1").correo("e1@test.com").build();
        userEntity2 = UserEntity.builder().id(2L).nombre("Entrenador2").correo("e2@test.com").build();
        pokemon1 = PokemonEntity.builder().id(1L).numero(25).nombre("Pikachu").build();
        pokemon2 = PokemonEntity.builder().id(2L).numero(4).nombre("Charmander").build();
        team1 = TeamEntity.builder().id(1L).nombre("Equipo1").pokemons(new ArrayList<>(List.of(pokemon1))).build();
        team2 = TeamEntity.builder().id(2L).nombre("Equipo2").pokemons(new ArrayList<>(List.of(pokemon2))).build();
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

    @Nested
    @DisplayName("intercambiar")
    class Intercambiar {

        private IntercambiarRequest request;

        @BeforeEach
        void setUp() {
            request = new IntercambiarRequest(1L, 2L, 1L, 2L);
        }

        @Test
        @DisplayName("intercambia Pokémon exitosamente")
        void intercambiaExitosamente() {
            when(userRepo.findById(1L)).thenReturn(Optional.of(userEntity1));
            when(userRepo.findById(2L)).thenReturn(Optional.of(userEntity2));
            when(pokemonRepo.findById(1L)).thenReturn(Optional.of(pokemon1));
            when(pokemonRepo.findById(2L)).thenReturn(Optional.of(pokemon2));
            when(teamRepo.findByUsuarioId(1L)).thenReturn(List.of(team1));
            when(teamRepo.findByUsuarioIdAndPokemonId(1L, 1L)).thenReturn(Optional.of(team1));
            when(teamRepo.findByUsuarioIdAndPokemonId(2L, 2L)).thenReturn(Optional.of(team2));

            var result = usuarioService.intercambiar(request);

            assertNotNull(result);
            assertEquals("Intercambio realizado exitosamente", result.mensaje());
            verify(teamRepo).save(team1);
            verify(teamRepo).save(team2);
        }

        @Test
        @DisplayName("lanza BusinessException cuando ofertante y receptor son el mismo")
        void lanzaExcepcionCuandoMismoUsuario() {
            var mismoUsuario = new IntercambiarRequest(1L, 1L, 1L, 2L);

            assertThrows(BusinessException.class, () -> usuarioService.intercambiar(mismoUsuario));
        }

        @Test
        @DisplayName("lanza BusinessException cuando se ofrece el mismo Pokémon")
        void lanzaExcepcionCuandoMismoPokemon() {
            var mismoPokemon = new IntercambiarRequest(1L, 2L, 1L, 1L);

            assertThrows(BusinessException.class, () -> usuarioService.intercambiar(mismoPokemon));
        }

        @Test
        @DisplayName("lanza ResourceNotFoundException cuando ofertante no existe")
        void lanzaExcepcionCuandoOfertanteNoExiste() {
            when(userRepo.findById(1L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> usuarioService.intercambiar(request));
        }

        @Test
        @DisplayName("lanza ResourceNotFoundException cuando Pokémon ofertado no existe")
        void lanzaExcepcionCuandoPokemonOfertadoNoExiste() {
            when(userRepo.findById(1L)).thenReturn(Optional.of(userEntity1));
            when(userRepo.findById(2L)).thenReturn(Optional.of(userEntity2));
            when(pokemonRepo.findById(1L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> usuarioService.intercambiar(request));
        }

        @Test
        @DisplayName("lanza BusinessException cuando ofertante no tiene Pokémon registrados")
        void lanzaExcepcionCuandoOfertanteSinPokemon() {
            when(userRepo.findById(1L)).thenReturn(Optional.of(userEntity1));
            when(userRepo.findById(2L)).thenReturn(Optional.of(userEntity2));
            when(pokemonRepo.findById(1L)).thenReturn(Optional.of(pokemon1));
            when(pokemonRepo.findById(2L)).thenReturn(Optional.of(pokemon2));
            when(teamRepo.findByUsuarioId(1L)).thenReturn(List.of());

            assertThrows(BusinessException.class, () -> usuarioService.intercambiar(request));
        }

        @Test
        @DisplayName("lanza BusinessException cuando Pokémon ofertado no pertenece al ofertante")
        void lanzaExcepcionCuandoPokemonNoPertenece() {
            when(userRepo.findById(1L)).thenReturn(Optional.of(userEntity1));
            when(userRepo.findById(2L)).thenReturn(Optional.of(userEntity2));
            when(pokemonRepo.findById(1L)).thenReturn(Optional.of(pokemon1));
            when(pokemonRepo.findById(2L)).thenReturn(Optional.of(pokemon2));
            when(teamRepo.findByUsuarioId(1L)).thenReturn(List.of(team1));
            when(teamRepo.findByUsuarioIdAndPokemonId(1L, 1L)).thenReturn(Optional.empty());

            assertThrows(BusinessException.class, () -> usuarioService.intercambiar(request));
        }

        @Test
        @DisplayName("lanza BusinessException cuando Pokémon solicitado no pertenece al receptor")
        void lanzaExcepcionCuandoPokemonSolicitadoNoPertenece() {
            when(userRepo.findById(1L)).thenReturn(Optional.of(userEntity1));
            when(userRepo.findById(2L)).thenReturn(Optional.of(userEntity2));
            when(pokemonRepo.findById(1L)).thenReturn(Optional.of(pokemon1));
            when(pokemonRepo.findById(2L)).thenReturn(Optional.of(pokemon2));
            when(teamRepo.findByUsuarioId(1L)).thenReturn(List.of(team1));
            when(teamRepo.findByUsuarioIdAndPokemonId(1L, 1L)).thenReturn(Optional.of(team1));
            when(teamRepo.findByUsuarioIdAndPokemonId(2L, 2L)).thenReturn(Optional.empty());

            assertThrows(BusinessException.class, () -> usuarioService.intercambiar(request));
        }
    }
}
