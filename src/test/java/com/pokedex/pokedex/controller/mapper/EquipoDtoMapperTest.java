package com.pokedex.pokedex.controller.mapper;

import com.pokedex.pokedex.controller.dto.request.EquipoRequest;
import com.pokedex.pokedex.core.model.Pokemon;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EquipoDtoMapperTest {

    private final EquipoDtoMapper mapper = new EquipoDtoMapperImpl();

    @Test
    void toDomainWithIds_mapsCorrectly() {
        var request = new EquipoRequest("Mi Equipo", List.of(1L, 2L));
        var pokemons = List.of(
                Pokemon.builder().id(1L).numero(25).nombre("Pikachu").build(),
                Pokemon.builder().id(2L).numero(4).nombre("Charmander").build()
        );

        var result = mapper.toDomainWithIds(request, 10L, pokemons);

        assertNotNull(result);
        assertEquals("Mi Equipo", result.getNombre());
        assertEquals(10L, result.getUsuarioId());
        assertEquals(2, result.getPokemons().size());
        assertEquals("Pikachu", result.getPokemons().get(0).getNombre());
        assertEquals("Charmander", result.getPokemons().get(1).getNombre());
    }
}
