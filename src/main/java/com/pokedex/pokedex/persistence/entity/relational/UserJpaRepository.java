package com.pokedex.pokedex.persistence.entity.relational;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByCorreo(String correo);

    boolean existsByCorreo(String correo);

    boolean existsByNombre(String nombre);
}