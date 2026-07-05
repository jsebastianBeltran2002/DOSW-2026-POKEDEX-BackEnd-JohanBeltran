package com.pokedex.pokedex.persistence.mapper;

import com.pokedex.pokedex.core.model.Usuario;
import com.pokedex.pokedex.persistence.entity.relational.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UsuarioPersistenceMapper {

    @Mapping(target = "rol", expression = "java(entity.getRol().name())")
    Usuario toDomain(UserEntity entity);

    @Mapping(target = "rol", expression = "java(com.pokedex.pokedex.persistence.entity.relational.UserEntity.Rol.valueOf(usuario.getRol()))")
    UserEntity toEntity(Usuario usuario);
}