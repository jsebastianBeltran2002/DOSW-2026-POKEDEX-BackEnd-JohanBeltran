package com.pokedex.pokedex.controller.handler;

import com.pokedex.pokedex.core.exception.BusinessException;
import com.pokedex.pokedex.core.exception.DuplicateResourceException;
import com.pokedex.pokedex.core.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @RestController
    static class TestController {
        @GetMapping("/test/not-found")
        void notFound() {
            throw new ResourceNotFoundException("Pokemon", "id", 1L);
        }

        @GetMapping("/test/duplicate")
        void duplicate() {
            throw new DuplicateResourceException("Pokemon", "numero", 25);
        }

        @GetMapping("/test/business")
        void business() {
            throw new BusinessException("Correo o contrase\u00f1a incorrectos", "INVALID_CREDENTIALS");
        }

        @GetMapping("/test/generic")
        void generic() {
            throw new RuntimeException("Error inesperado en el servidor");
        }
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void handleNotFound_returns404() throws Exception {
        mockMvc.perform(get("/test/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.errorCode").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Pokemon con id=1 no encontrado"))
                .andExpect(jsonPath("$.path").value("/test/not-found"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.fieldErrors").isArray());
    }

    @Test
    void handleDuplicate_returns409() throws Exception {
        mockMvc.perform(get("/test/duplicate"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.errorCode").value("DUPLICATE"))
                .andExpect(jsonPath("$.message").value("Pokemon con numero=25 ya existe"))
                .andExpect(jsonPath("$.path").value("/test/duplicate"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    void handleBusiness_returns401() throws Exception {
        mockMvc.perform(get("/test/business"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.errorCode").value("INVALID_CREDENTIALS"))
                .andExpect(jsonPath("$.message").value("Correo o contrase\u00f1a incorrectos"))
                .andExpect(jsonPath("$.path").value("/test/business"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    void handleGeneric_returns500() throws Exception {
        mockMvc.perform(get("/test/generic"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.errorCode").value("INTERNAL_ERROR"))
                .andExpect(jsonPath("$.message").value("Ocurri\u00f3 un error inesperado en el servidor"))
                .andExpect(jsonPath("$.path").value("/test/generic"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    void handleValidation_returns400() throws Exception {
        mockMvc.perform(get("/test/not-found")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
