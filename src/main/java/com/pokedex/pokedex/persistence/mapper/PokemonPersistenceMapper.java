package com.pokedex.pokedex.persistence.mapper;

import com.pokedex.pokedex.core.model.Pokemon;
import com.pokedex.pokedex.persistence.entity.relational.PokemonEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PokemonPersistenceMapper {

    Pokemon toDomain(PokemonEntity entity);

    PokemonEntity toEntity(Pokemon pokemon);
}