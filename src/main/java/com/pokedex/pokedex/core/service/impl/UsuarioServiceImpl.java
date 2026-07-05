package com.pokedex.pokedex.core.service.impl;

import com.pokedex.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.pokedex.core.model.Usuario;
import com.pokedex.pokedex.core.service.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioServiceImpl implements UsuarioService {

    @Override
    public Usuario crear(Usuario usuario) {
        return usuario;
    }

    @Override
    public Usuario findById(Long id) {
        throw new ResourceNotFoundException("Usuario", "id", id);
    }

    @Override
    public Usuario findByCorreo(String correo) {
        throw new ResourceNotFoundException("Usuario", "correo", correo);
    }

    @Override
    public List<Usuario> findAll() {
        return List.of();
    }

    @Override
    public Usuario actualizar(Long id, Usuario usuario) {
        throw new ResourceNotFoundException("Usuario", "id", id);
    }

    @Override
    public void eliminar(Long id) {
        throw new ResourceNotFoundException("Usuario", "id", id);
    }

    @Override
    public Usuario activarDesactivar(Long id, Boolean activo) {
        throw new ResourceNotFoundException("Usuario", "id", id);
    }
}