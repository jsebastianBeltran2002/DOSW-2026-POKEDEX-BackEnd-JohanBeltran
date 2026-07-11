package com.pokedex.pokedex.persistence.mapper;

import com.pokedex.pokedex.core.model.Usuario;
import com.pokedex.pokedex.persistence.entity.relational.UserEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioPersistenceMapperTest {

    private final UsuarioPersistenceMapper mapper = new UsuarioPersistenceMapperImpl();

    @Test
    void toDomain_convertsRolEnumToString() {
        var entity = UserEntity.builder()
                .id(1L)
                .nombre("Entrenador")
                .correo("trainer@test.com")
                .password("pass")
                .activo(true)
                .rol(UserEntity.Rol.USUARIO)
                .build();

        Usuario result = mapper.toDomain(entity);

        assertNotNull(result);
        assertEquals("USUARIO", result.getRol());
        assertEquals("Entrenador", result.getNombre());
        assertEquals("trainer@test.com", result.getCorreo());
        assertTrue(result.getActivo());
    }

    @Test
    void toDomain_convertsAdminRol() {
        var entity = UserEntity.builder()
                .id(2L)
                .nombre("Admin")
                .correo("admin@test.com")
                .password("pass")
                .activo(true)
                .rol(UserEntity.Rol.ADMINISTRADOR)
                .build();

        Usuario result = mapper.toDomain(entity);

        assertEquals("ADMINISTRADOR", result.getRol());
    }

    @Test
    void toEntity_convertsStringToRolEnum() {
        var usuario = Usuario.builder()
                .id(1L)
                .nombre("Entrenador")
                .correo("trainer@test.com")
                .password("pass")
                .rol("USUARIO")
                .build();

        UserEntity result = mapper.toEntity(usuario);

        assertNotNull(result);
        assertEquals(UserEntity.Rol.USUARIO, result.getRol());
    }

    @Test
    void toEntity_convertsAdminString() {
        var usuario = Usuario.builder()
                .id(1L)
                .nombre("Admin")
                .correo("admin@test.com")
                .password("pass")
                .rol("ADMINISTRADOR")
                .build();

        UserEntity result = mapper.toEntity(usuario);

        assertEquals(UserEntity.Rol.ADMINISTRADOR, result.getRol());
    }

    @Test
    void toEntity_throwsExceptionForInvalidRol() {
        var usuario = Usuario.builder()
                .id(1L)
                .nombre("Test")
                .correo("test@test.com")
                .password("pass")
                .rol("INEXISTENTE")
                .build();

        assertThrows(IllegalArgumentException.class, () -> mapper.toEntity(usuario));
    }
}
