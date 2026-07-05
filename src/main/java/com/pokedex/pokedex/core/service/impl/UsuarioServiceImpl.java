package com.pokedex.pokedex.core.service.impl;

import com.pokedex.pokedex.core.exception.DuplicateResourceException;
import com.pokedex.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.pokedex.core.model.Usuario;
import com.pokedex.pokedex.core.service.interfaces.UsuarioPersistencePort;
import com.pokedex.pokedex.core.service.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioPersistencePort usuarioPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Usuario crear(Usuario usuario) {
        if (usuarioPort.existsByCorreo(usuario.getCorreo())) {
            throw new DuplicateResourceException("Usuario", "correo", usuario.getCorreo());
        }

        Usuario nuevoUsuario = usuario.toBuilder()
                .password(passwordEncoder.encode(usuario.getPassword()))
                .activo(true)
                .rol(usuario.getRol() == null ? "USUARIO" : usuario.getRol())
                .fechaRegistro(LocalDateTime.now())
                .build();

        log.info("Creando usuario con correo: {}", nuevoUsuario.getCorreo());
        return usuarioPort.save(nuevoUsuario);
    }

    @Override
    public Usuario findById(Long id) {
        return usuarioPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
    }

    @Override
    public Usuario findByCorreo(String correo) {
        return usuarioPort.findByCorreo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "correo", correo));
    }

    @Override
    public List<Usuario> findAll() {
        return usuarioPort.findAll();
    }

    @Override
    @Transactional
    public Usuario actualizar(Long id, Usuario usuario) {
        Usuario existente = findById(id);

        Usuario actualizado = existente.toBuilder()
                .nombre(usuario.getNombre())
                .correo(usuario.getCorreo())
                .build();

        return usuarioPort.save(actualizado);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!usuarioPort.existsById(id)) {
            throw new ResourceNotFoundException("Usuario", "id", id);
        }
        usuarioPort.deleteById(id);
    }

    @Override
    @Transactional
    public Usuario activarDesactivar(Long id, Boolean activo) {
        Usuario existente = findById(id);
        Usuario actualizado = existente.toBuilder().activo(activo).build();
        return usuarioPort.save(actualizado);
    }
}