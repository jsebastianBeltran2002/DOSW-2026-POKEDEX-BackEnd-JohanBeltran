package com.pokedex.pokedex.persistence.mapper;

import com.pokedex.pokedex.core.model.Favorito;
import com.pokedex.pokedex.persistence.entity.relational.FavoritoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FavoritoPersistenceMapper {

    @Mapping(target = "usuarioId", source = "usuario.id")
    @Mapping(target = "pokemonId", source = "pokemon.id")
    Favorito toDomain(FavoritoEntity entity);

    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "pokemon", ignore = true)
    FavoritoEntity toEntity(Favorito favorito);
}
