package com.pokedex.pokedex.controller.mapper;

import com.pokedex.pokedex.controller.dto.response.FavoritoResponse;
import com.pokedex.pokedex.core.model.Favorito;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FavoritoDtoMapperTest {

    private final FavoritoDtoMapper mapper = new FavoritoDtoMapperImpl();

    @Test
    void toResponse_mapsAllFields() {
        var now = LocalDateTime.now();
        var favorito = Favorito.builder()
                .id(1L).usuarioId(10L).pokemonId(25L).fechaGuardado(now)
                .build();

        FavoritoResponse response = mapper.toResponse(favorito);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals(10L, response.usuarioId());
        assertEquals(25L, response.pokemonId());
        assertEquals(now, response.fechaGuardado());
    }

    @Test
    void toResponse_returnsNull_whenNull() {
        assertNull(mapper.toResponse(null));
    }

    @Test
    void toResponseList_mapsList() {
        var favorito = Favorito.builder().id(1L).usuarioId(10L).pokemonId(25L).build();
        var response = mapper.toResponseList(List.of(favorito));

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(10L, response.get(0).usuarioId());
    }

    @Test
    void toResponseList_returnsNull_whenNull() {
        assertNull(mapper.toResponseList(null));
    }
}
