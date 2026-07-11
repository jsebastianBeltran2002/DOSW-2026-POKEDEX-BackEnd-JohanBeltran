package com.pokedex.pokedex.persistence.mapper;

import com.pokedex.pokedex.core.model.Favorito;
import com.pokedex.pokedex.persistence.entity.relational.FavoritoEntity;
import com.pokedex.pokedex.persistence.entity.relational.PokemonEntity;
import com.pokedex.pokedex.persistence.entity.relational.UserEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FavoritoPersistenceMapperTest {

    private final FavoritoPersistenceMapper mapper = new FavoritoPersistenceMapperImpl();

    @Test
    void toDomain_mapsAllFields() {
        var userEntity = UserEntity.builder().id(1L).build();
        var pokemonEntity = PokemonEntity.builder().id(25L).build();
        var entity = FavoritoEntity.builder()
                .id(10L)
                .usuario(userEntity)
                .pokemon(pokemonEntity)
                .build();

        Favorito result = mapper.toDomain(entity);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals(1L, result.getUsuarioId());
        assertEquals(25L, result.getPokemonId());
    }

    @Test
    void toDomain_returnsNull_whenNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    void toEntity_mapsAllFields() {
        var favorito = Favorito.builder()
                .id(10L).usuarioId(1L).pokemonId(25L)
                .build();

        FavoritoEntity entity = mapper.toEntity(favorito);

        assertNotNull(entity);
        assertEquals(10L, entity.getId());
        assertNull(entity.getUsuario());
        assertNull(entity.getPokemon());
    }

    @Test
    void toEntity_returnsNull_whenNull() {
        assertNull(mapper.toEntity(null));
    }
}
