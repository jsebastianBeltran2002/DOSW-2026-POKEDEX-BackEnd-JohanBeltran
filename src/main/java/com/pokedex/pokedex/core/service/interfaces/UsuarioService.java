package com.pokedex.pokedex.core.service.interfaces;

import com.pokedex.pokedex.core.model.Usuario;
import java.util.List;

public interface UsuarioService {
    Usuario crear(Usuario usuario);
    Usuario findById(Long id);
    Usuario findByCorreo(String correo);
    List<Usuario> findAll();
    Usuario actualizar(Long id, Usuario usuario);
    void eliminar(Long id);
    Usuario activarDesactivar(Long id, Boolean activo);
}