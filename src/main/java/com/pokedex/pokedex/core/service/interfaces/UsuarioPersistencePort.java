package com.pokedex.pokedex.core.service.interfaces;

import com.pokedex.pokedex.core.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioPersistencePort {

    List<Usuario> findAll();

    Optional<Usuario> findById(Long id);

    Optional<Usuario> findByCorreo(String correo);

    boolean existsByCorreo(String correo);

    Usuario save(Usuario usuario);

    void deleteById(Long id);

    boolean existsById(Long id);
}