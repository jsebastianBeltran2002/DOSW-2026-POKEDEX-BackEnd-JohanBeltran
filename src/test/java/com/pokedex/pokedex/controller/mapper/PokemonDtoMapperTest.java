package com.pokedex.pokedex.controller.mapper;

import com.pokedex.pokedex.controller.dto.request.PokemonRequest;
import com.pokedex.pokedex.controller.dto.response.PokemonResponse;
import com.pokedex.pokedex.core.model.Pokemon;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PokemonDtoMapperTest {

    private final PokemonDtoMapper mapper = new PokemonDtoMapperImpl();

    private final Pokemon pikachu = Pokemon.builder()
            .id(1L).numero(25).nombre("Pikachu").descripcion("Mouse Pokemon")
            .altura(0.4).peso(6.0).sprite("pikachu.png")
            .tipos(List.of("Electrico"))
            .region("Kanto").generacion(1).legendario(false).tieneMega(false)
            .hp(35).ataque(55).defensa(40).ataqueEspecial(50).defensaEspecial(50).velocidad(90)
            .build();

    @Test
    void toResponse_mapsAllFields() {
        PokemonResponse response = mapper.toResponse(pikachu);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals(25, response.numero());
        assertEquals("Pikachu", response.nombre());
        assertEquals("Mouse Pokemon", response.descripcion());
        assertEquals(0.4, response.altura());
        assertEquals(6.0, response.peso());
        assertEquals("pikachu.png", response.sprite());
        assertEquals(List.of("Electrico"), response.tipos());
        assertEquals("Kanto", response.region());
        assertEquals(1, response.generacion());
        assertFalse(response.legendario());
        assertFalse(response.tieneMega());
        assertEquals(35, response.hp());
        assertEquals(55, response.ataque());
        assertEquals(40, response.defensa());
        assertEquals(50, response.ataqueEspecial());
        assertEquals(50, response.defensaEspecial());
        assertEquals(90, response.velocidad());
    }

    @Test
    void toResponse_returnsNull_whenNull() {
        assertNull(mapper.toResponse(null));
    }

    @Test
    void toResponseList_mapsList() {
        var response = mapper.toResponseList(List.of(pikachu));

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Pikachu", response.get(0).nombre());
    }

    @Test
    void toResponseList_returnsNull_whenNull() {
        assertNull(mapper.toResponseList(null));
    }

    @Test
    void toDomain_mapsAllFields() {
        var request = new PokemonRequest(25, "Pikachu", "Mouse Pokemon",
                List.of("Electrico"), 0.4, 6.0, "pikachu.png",
                "Kanto", 1, false, false, 35, 55, 40, 50, 50, 90);

        Pokemon result = mapper.toDomain(request);

        assertNotNull(result);
        assertNull(result.getId());
        assertEquals(25, result.getNumero());
        assertEquals("Pikachu", result.getNombre());
        assertEquals(List.of("Electrico"), result.getTipos());
        assertEquals("Kanto", result.getRegion());
        assertEquals(1, result.getGeneracion());
        assertEquals(90, result.getVelocidad());
    }

    @Test
    void toDomain_returnsNull_whenNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    void toDomain_handlesNullTipos() {
        var request = new PokemonRequest(25, "Pikachu", null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null);

        Pokemon result = mapper.toDomain(request);

        assertNotNull(result);
        assertNull(result.getTipos());
    }
}
