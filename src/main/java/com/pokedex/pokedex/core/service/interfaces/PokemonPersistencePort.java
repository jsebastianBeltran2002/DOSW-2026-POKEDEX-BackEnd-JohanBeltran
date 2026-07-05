package com.pokedex.pokedex.core.service.interfaces;

import com.pokedex.pokedex.core.model.Pokemon;
import java.util.List;
import java.util.Optional;

public interface PokemonPersistencePort {

    List<Pokemon> findAll();

    Optional<Pokemon> findById(Long id);

    Optional<Pokemon> findByNumero(Integer numero);

    boolean existsByNumero(Integer numero);

    Pokemon save(Pokemon pokemon);

    void deleteById(Long id);

    boolean existsById(Long id);
}