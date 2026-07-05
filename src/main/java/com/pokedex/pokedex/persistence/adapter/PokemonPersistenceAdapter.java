package com.pokedex.pokedex.persistence.adapter;

import com.pokedex.pokedex.core.model.Pokemon;
import com.pokedex.pokedex.core.service.interfaces.PokemonPersistencePort;
import com.pokedex.pokedex.persistence.entity.relational.PokemonEntity;
import com.pokedex.pokedex.persistence.entity.relational.PokemonJpaRepository;
import com.pokedex.pokedex.persistence.mapper.PokemonPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PokemonPersistenceAdapter implements PokemonPersistencePort {

    private final PokemonJpaRepository repository;
    private final PokemonPersistenceMapper mapper;

    @Override
    public List<Pokemon> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Pokemon> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Pokemon> findByNumero(Integer numero) {
        return repository.findByNumero(numero).map(mapper::toDomain);
    }

    @Override
    public boolean existsByNumero(Integer numero) {
        return repository.existsByNumero(numero);
    }

    @Override
    public Pokemon save(Pokemon pokemon) {
        PokemonEntity entity = mapper.toEntity(pokemon);
        PokemonEntity saved = repository.save(entity);
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