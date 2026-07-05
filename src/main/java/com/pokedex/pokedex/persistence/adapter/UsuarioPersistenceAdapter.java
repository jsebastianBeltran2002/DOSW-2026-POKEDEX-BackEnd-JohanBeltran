package com.pokedex.pokedex.persistence.adapter;

import com.pokedex.pokedex.core.model.Usuario;
import com.pokedex.pokedex.core.service.interfaces.UsuarioPersistencePort;
import com.pokedex.pokedex.persistence.entity.relational.UserEntity;
import com.pokedex.pokedex.persistence.entity.relational.UserJpaRepository;
import com.pokedex.pokedex.persistence.mapper.UsuarioPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UsuarioPersistenceAdapter implements UsuarioPersistencePort {

    private final UserJpaRepository repository;
    private final UsuarioPersistenceMapper mapper;

    @Override
    public List<Usuario> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Usuario> findByCorreo(String correo) {
        return repository.findByCorreo(correo).map(mapper::toDomain);
    }

    @Override
    public boolean existsByCorreo(String correo) {
        return repository.existsByCorreo(correo);
    }

    @Override
    public Usuario save(Usuario usuario) {
        UserEntity entity = mapper.toEntity(usuario);
        UserEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}