package com.pokedex.pokedex.persistence.mapper;

import com.pokedex.pokedex.core.model.Equipo;
import com.pokedex.pokedex.core.model.Pokemon;
import com.pokedex.pokedex.persistence.entity.relational.PokemonEntity;
import com.pokedex.pokedex.persistence.entity.relational.TeamEntity;
import com.pokedex.pokedex.persistence.entity.relational.UserEntity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EquipoPersistenceMapperTest {

    private final EquipoPersistenceMapper mapper = new EquipoPersistenceMapperImpl();

    @Test
    void toDomain_mapsTeamWithPokemons() {
        var pokemonEntity = PokemonEntity.builder().id(1L).numero(25).nombre("Pikachu").build();
        var userEntity = UserEntity.builder().id(1L).nombre("Entrenador").correo("trainer@test.com").build();
        var teamEntity = TeamEntity.builder()
                .id(1L)
                .nombre("Mi Equipo")
                .usuario(userEntity)
                .pokemons(List.of(pokemonEntity))
                .build();

        Equipo result = mapper.toDomain(teamEntity);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Mi Equipo", result.getNombre());
        assertEquals(1L, result.getUsuarioId());
        assertNotNull(result.getPokemons());
        assertEquals(1, result.getPokemons().size());
        Pokemon mappedPokemon = result.getPokemons().get(0);
        assertEquals(1L, mappedPokemon.getId());
        assertEquals(25, mappedPokemon.getNumero());
        assertEquals("Pikachu", mappedPokemon.getNombre());
    }

    @Test
    void toDomain_mapsTeamWithNullPokemons() {
        var userEntity = UserEntity.builder().id(1L).build();
        var teamEntity = TeamEntity.builder()
                .id(1L)
                .nombre("Equipo Vacio")
                .usuario(userEntity)
                .build();

        Equipo result = mapper.toDomain(teamEntity);

        assertNotNull(result);
        assertEquals("Equipo Vacio", result.getNombre());
        assertTrue(result.getPokemons().isEmpty());
    }
}
