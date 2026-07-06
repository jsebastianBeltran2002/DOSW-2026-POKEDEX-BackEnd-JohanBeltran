package com.pokedex.pokedex.controller.mapper;

import com.pokedex.pokedex.controller.dto.response.FavoritoResponse;
import com.pokedex.pokedex.core.model.Favorito;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FavoritoDtoMapper {

    FavoritoResponse toResponse(Favorito favorito);

    List<FavoritoResponse> toResponseList(List<Favorito> favoritos);
}
