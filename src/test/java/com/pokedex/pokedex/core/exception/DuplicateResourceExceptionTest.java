package com.pokedex.pokedex.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DuplicateResourceExceptionTest {

    @Test
    void constructor_createsFormattedMessage() {
        var exception = new DuplicateResourceException("Pokemon", "numero", 25);
        assertEquals("Pokemon con numero=25 ya existe", exception.getMessage());
        assertEquals("DUPLICATE", exception.getErrorCode());
    }

    @Test
    void isBusinessException() {
        var exception = new DuplicateResourceException("Usuario", "correo", "a@b.com");
        assertTrue(exception instanceof BusinessException);
    }
}
