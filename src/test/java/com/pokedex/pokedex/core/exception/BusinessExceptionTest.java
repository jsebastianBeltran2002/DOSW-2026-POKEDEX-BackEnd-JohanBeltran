package com.pokedex.pokedex.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BusinessExceptionTest {

    @Test
    void constructor_setsMessageAndErrorCode() {
        var exception = new BusinessException("Error de negocio", "BUSINESS_ERROR");
        assertEquals("Error de negocio", exception.getMessage());
        assertEquals("BUSINESS_ERROR", exception.getErrorCode());
    }

    @Test
    void isRuntimeException() {
        var exception = new BusinessException("test", "TEST");
        assertNotNull(exception);
    }
}
