package com.pokedex.pokedex.persistence.mapper;

import com.pokedex.pokedex.core.model.Pokemon;
import com.pokedex.pokedex.persistence.entity.relational.PokemonEntity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PokemonPersistenceMapperTest {

    private final PokemonPersistenceMapper mapper = new PokemonPersistenceMapperImpl();

    @Test
    void toDomain_mapsAllFields() {
        var entity = PokemonEntity.builder()
                .id(1L).numero(25).nombre("Pikachu").descripcion("Mouse Pokemon")
                .altura(0.4).peso(6.0).sprite("pikachu.png").tipos(List.of("Electrico"))
                .region("Kanto").generacion(1).legendario(false).tieneMega(false)
                .hp(35).ataque(55).defensa(40).ataqueEspecial(50).defensaEspecial(50).velocidad(90)
                .build();

        Pokemon result = mapper.toDomain(entity);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(25, result.getNumero());
        assertEquals("Pikachu", result.getNombre());
        assertEquals("Mouse Pokemon", result.getDescripcion());
        assertEquals(0.4, result.getAltura());
        assertEquals(6.0, result.getPeso());
        assertEquals("pikachu.png", result.getSprite());
        assertEquals(List.of("Electrico"), result.getTipos());
        assertEquals("Kanto", result.getRegion());
        assertEquals(1, result.getGeneracion());
        assertFalse(result.getLegendario());
        assertFalse(result.getTieneMega());
        assertEquals(35, result.getHp());
        assertEquals(55, result.getAtaque());
        assertEquals(40, result.getDefensa());
        assertEquals(50, result.getAtaqueEspecial());
        assertEquals(50, result.getDefensaEspecial());
        assertEquals(90, result.getVelocidad());
    }

    @Test
    void toDomain_returnsNull_whenNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    void toEntity_mapsAllFields() {
        var pokemon = Pokemon.builder()
                .id(1L).numero(25).nombre("Pikachu").descripcion("Mouse Pokemon")
                .altura(0.4).peso(6.0).sprite("pikachu.png").tipos(List.of("Electrico"))
                .region("Kanto").generacion(1).legendario(false).tieneMega(false)
                .hp(35).ataque(55).defensa(40).ataqueEspecial(50).defensaEspecial(50).velocidad(90)
                .build();

        PokemonEntity entity = mapper.toEntity(pokemon);

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals(25, entity.getNumero());
        assertEquals("Pikachu", entity.getNombre());
        assertEquals(0.4, entity.getAltura());
        assertEquals(6.0, entity.getPeso());
        assertEquals(List.of("Electrico"), entity.getTipos());
        assertEquals("Kanto", entity.getRegion());
    }

    @Test
    void toEntity_returnsNull_whenNull() {
        assertNull(mapper.toEntity(null));
    }
}
