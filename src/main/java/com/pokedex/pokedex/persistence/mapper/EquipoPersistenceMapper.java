package com.pokedex.pokedex.persistence.mapper;

import com.pokedex.pokedex.core.model.Equipo;
import com.pokedex.pokedex.core.model.Pokemon;
import com.pokedex.pokedex.persistence.entity.relational.PokemonEntity;
import com.pokedex.pokedex.persistence.entity.relational.TeamEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EquipoPersistenceMapper {

    @Mapping(target = "usuarioId", source = "usuario.id")
    @Mapping(target = "pokemons", ignore = true)
    Equipo toDomain(TeamEntity entity);

    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "pokemons", ignore = true)
    TeamEntity toEntity(Equipo equipo);

    @AfterMapping
    default void mapPokemonsToDomain(TeamEntity entity, @MappingTarget Equipo.EquipoBuilder equipo) {
        if (entity.getPokemons() != null) {
            List<Pokemon> pokemons = entity.getPokemons().stream()
                    .map(this::toPokemonDomain)
                    .collect(Collectors.toList());
            equipo.pokemons(pokemons);
        }
    }

    @Mapping(target = "tipos", ignore = true)
    Pokemon toPokemonDomain(PokemonEntity entity);
}
