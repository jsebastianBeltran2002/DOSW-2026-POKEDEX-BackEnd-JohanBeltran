package com.pokedex.pokedex.controller.mapper;

import com.pokedex.pokedex.controller.dto.request.UsuarioRequest;
import com.pokedex.pokedex.controller.dto.response.UsuarioResponse;
import com.pokedex.pokedex.core.model.Usuario;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioDtoMapperTest {

    private final UsuarioDtoMapper mapper = new UsuarioDtoMapperImpl();

    @Test
    void toResponse_mapsAllFields() {
        var now = LocalDateTime.now();
        var usuario = Usuario.builder()
                .id(1L).nombre("Entrenador").correo("trainer@test.com")
                .password("pass").activo(true).rol("USUARIO").fechaRegistro(now)
                .build();

        UsuarioResponse response = mapper.toResponse(usuario);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Entrenador", response.nombre());
        assertEquals("trainer@test.com", response.correo());
        assertTrue(response.activo());
        assertEquals("USUARIO", response.rol());
        assertEquals(now, response.fechaRegistro());
    }

    @Test
    void toResponse_returnsNull_whenNull() {
        assertNull(mapper.toResponse(null));
    }

    @Test
    void toDomain_mapsAllFields() {
        var request = new UsuarioRequest("Nuevo", "nuevo@test.com", "password123");

        Usuario result = mapper.toDomain(request);

        assertNotNull(result);
        assertNull(result.getId());
        assertEquals("Nuevo", result.getNombre());
        assertEquals("nuevo@test.com", result.getCorreo());
        assertEquals("password123", result.getPassword());
        assertNull(result.getActivo());
        assertNull(result.getRol());
        assertNull(result.getFechaRegistro());
    }

    @Test
    void toDomain_returnsNull_whenNull() {
        assertNull(mapper.toDomain(null));
    }
}
