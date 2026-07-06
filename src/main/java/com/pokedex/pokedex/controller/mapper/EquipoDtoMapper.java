package com.pokedex.pokedex.controller.mapper;

import com.pokedex.pokedex.controller.dto.request.EquipoRequest;
import com.pokedex.pokedex.controller.dto.response.EquipoResponse;
import com.pokedex.pokedex.controller.dto.response.PokemonResponse;
import com.pokedex.pokedex.core.model.Equipo;
import com.pokedex.pokedex.core.model.Pokemon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = PokemonDtoMapper.class)
public interface EquipoDtoMapper {

    EquipoResponse toResponse(Equipo equipo);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "analisisCompetitivo", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "usuarioId", ignore = true)
    @Mapping(target = "pokemons", ignore = true)
    Equipo toDomain(EquipoRequest request);

    default Equipo toDomainWithIds(EquipoRequest request, Long usuarioId, List<Pokemon> pokemons) {
        return Equipo.builder()
                .nombre(request.nombre())
                .usuarioId(usuarioId)
                .pokemons(pokemons)
                .build();
    }
}
