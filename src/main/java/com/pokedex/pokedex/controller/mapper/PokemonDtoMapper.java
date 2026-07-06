package com.pokedex.pokedex.controller.mapper;

import com.pokedex.pokedex.controller.dto.request.PokemonRequest;
import com.pokedex.pokedex.controller.dto.response.PokemonResponse;
import com.pokedex.pokedex.core.model.Pokemon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PokemonDtoMapper {

    PokemonResponse toResponse(Pokemon pokemon);

    List<PokemonResponse> toResponseList(List<Pokemon> pokemons);

    @Mapping(target = "id", ignore = true)
    Pokemon toDomain(PokemonRequest request);
}