package com.pokedex.pokedex.core.service.impl;

import com.pokedex.pokedex.core.exception.BusinessException;
import com.pokedex.pokedex.core.model.Usuario;
import com.pokedex.pokedex.core.service.interfaces.UsuarioService;
import com.pokedex.pokedex.security.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    private final Usuario usuario = Usuario.builder()
            .nombre("Entrenador")
            .correo("trainer@test.com")
            .password("password123")
            .build();

    private final UserDetails userDetails = User.builder()
            .username("trainer@test.com")
            .password("encodedPassword")
            .authorities(List.of())
            .build();

    @Nested
    @DisplayName("register")
    class Register {

        @Test
        @DisplayName("delega en usuarioService.crear y retorna el resultado")
        void delegatesAndReturns() {
            when(usuarioService.crear(usuario)).thenReturn(usuario);

            var result = authService.register(usuario);

            assertNotNull(result);
            assertEquals("trainer@test.com", result.getCorreo());
            verify(usuarioService).crear(usuario);
        }
    }

    @Nested
    @DisplayName("login")
    class Login {

        @Test
        @DisplayName("retorna token JWT con credenciales validas")
        void returnsTokenWithValidCredentials() {
            when(userDetailsService.loadUserByUsername("trainer@test.com")).thenReturn(userDetails);
            when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
            when(jwtService.generateToken(userDetails)).thenReturn("jwt-token");

            var token = authService.login("trainer@test.com", "password123");

            assertEquals("jwt-token", token);
            verify(userDetailsService).loadUserByUsername("trainer@test.com");
            verify(passwordEncoder).matches("password123", "encodedPassword");
            verify(jwtService).generateToken(userDetails);
        }

        @Test
        @DisplayName("lanza BusinessException cuando el usuario no existe")
        void throwsExceptionWhenUserNotFound() {
            when(userDetailsService.loadUserByUsername("unknown@test.com"))
                    .thenThrow(new org.springframework.security.core.userdetails.UsernameNotFoundException("not found"));

            var exception = assertThrows(BusinessException.class,
                    () -> authService.login("unknown@test.com", "password"));
            assertEquals("INVALID_CREDENTIALS", exception.getErrorCode());
            verify(userDetailsService).loadUserByUsername("unknown@test.com");
            verifyNoInteractions(passwordEncoder, jwtService);
        }

        @Test
        @DisplayName("lanza BusinessException cuando la contrasena es incorrecta")
        void throwsExceptionWhenWrongPassword() {
            when(userDetailsService.loadUserByUsername("trainer@test.com")).thenReturn(userDetails);
            when(passwordEncoder.matches("wrong-password", "encodedPassword")).thenReturn(false);

            var exception = assertThrows(BusinessException.class,
                    () -> authService.login("trainer@test.com", "wrong-password"));
            assertEquals("INVALID_CREDENTIALS", exception.getErrorCode());
            verify(passwordEncoder).matches("wrong-password", "encodedPassword");
            verifyNoInteractions(jwtService);
        }
    }
}
