package com.pokedex.pokedex.persistence.entity.relational;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamJpaRepository extends JpaRepository<TeamEntity, Long> {

    @Query("SELECT t FROM TeamEntity t WHERE t.usuario.id = :usuarioId")
    List<TeamEntity> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT COUNT(t) > 0 FROM TeamEntity t WHERE t.nombre = :nombre AND t.usuario.id = :usuarioId")
    boolean existsByNombreAndUsuarioId(@Param("nombre") String nombre, @Param("usuarioId") Long usuarioId);

    @Query("SELECT t FROM TeamEntity t JOIN t.pokemons p WHERE t.usuario.id = :usuarioId AND p.id = :pokemonId")
    Optional<TeamEntity> findByUsuarioIdAndPokemonId(@Param("usuarioId") Long usuarioId, @Param("pokemonId") Long pokemonId);
}
