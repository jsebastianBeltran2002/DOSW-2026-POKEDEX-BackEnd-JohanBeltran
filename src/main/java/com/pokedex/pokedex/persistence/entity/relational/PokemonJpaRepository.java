package com.pokedex.pokedex.persistence.entity.relational;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PokemonJpaRepository extends JpaRepository<PokemonEntity, Long> {

    boolean existsByNumero(Integer numero);

    Optional<PokemonEntity> findByNumero(Integer numero);

    boolean existsByNombreIgnoreCase(String nombre);
}