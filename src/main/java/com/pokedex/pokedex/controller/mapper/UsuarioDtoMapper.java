package com.pokedex.pokedex.controller.mapper;

import com.pokedex.pokedex.controller.dto.request.UsuarioRequest;
import com.pokedex.pokedex.controller.dto.response.UsuarioResponse;
import com.pokedex.pokedex.core.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UsuarioDtoMapper {

    UsuarioResponse toResponse(Usuario usuario);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    Usuario toDomain(UsuarioRequest request);
}