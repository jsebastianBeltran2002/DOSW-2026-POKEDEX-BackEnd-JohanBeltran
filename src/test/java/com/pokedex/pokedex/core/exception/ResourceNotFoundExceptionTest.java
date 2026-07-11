package com.pokedex.pokedex.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResourceNotFoundExceptionTest {

    @Test
    void constructor_createsFormattedMessage() {
        var exception = new ResourceNotFoundException("Pokemon", "id", 1L);
        assertEquals("Pokemon con id=1 no encontrado", exception.getMessage());
        assertEquals("NOT_FOUND", exception.getErrorCode());
    }

    @Test
    void isBusinessException() {
        var exception = new ResourceNotFoundException("Equipo", "id", 99L);
        assertTrue(exception instanceof BusinessException);
    }
}
