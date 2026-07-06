package com.pokedex.pokedex.persistence.entity.relational;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoritoJpaRepository extends JpaRepository<FavoritoEntity, Long> {

    @Query("SELECT f FROM FavoritoEntity f WHERE f.usuario.id = :usuarioId")
    List<FavoritoEntity> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT f FROM FavoritoEntity f WHERE f.usuario.id = :usuarioId AND f.pokemon.id = :pokemonId")
    Optional<FavoritoEntity> findByUsuarioIdAndPokemonId(@Param("usuarioId") Long usuarioId, @Param("pokemonId") Long pokemonId);

    @Query("SELECT COUNT(f) > 0 FROM FavoritoEntity f WHERE f.usuario.id = :usuarioId AND f.pokemon.id = :pokemonId")
    boolean existsByUsuarioIdAndPokemonId(@Param("usuarioId") Long usuarioId, @Param("pokemonId") Long pokemonId);
}
