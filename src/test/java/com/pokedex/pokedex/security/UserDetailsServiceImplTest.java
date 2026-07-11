package com.pokedex.pokedex.security;

import com.pokedex.pokedex.persistence.entity.relational.UserEntity;
import com.pokedex.pokedex.persistence.entity.relational.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserJpaRepository userJpaRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void loadUserByUsername_returnsUserDetails_whenExists() {
        var entity = UserEntity.builder()
                .correo("trainer@test.com")
                .password("encoded-pass")
                .activo(true)
                .rol(UserEntity.Rol.USUARIO)
                .build();
        when(userJpaRepository.findByCorreo("trainer@test.com")).thenReturn(Optional.of(entity));

        var userDetails = userDetailsService.loadUserByUsername("trainer@test.com");

        assertNotNull(userDetails);
        assertEquals("trainer@test.com", userDetails.getUsername());
        assertEquals("encoded-pass", userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USUARIO")));
    }

    @Test
    void loadUserByUsername_throwsException_whenNotFound() {
        when(userJpaRepository.findByCorreo("unknown@test.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("unknown@test.com"));
    }

    @Test
    void loadUserByUsername_disablesWhenInactivo() {
        var entity = UserEntity.builder()
                .correo("inactive@test.com")
                .password("encoded-pass")
                .activo(false)
                .rol(UserEntity.Rol.USUARIO)
                .build();
        when(userJpaRepository.findByCorreo("inactive@test.com")).thenReturn(Optional.of(entity));

        var userDetails = userDetailsService.loadUserByUsername("inactive@test.com");

        assertFalse(userDetails.isEnabled());
    }
}
