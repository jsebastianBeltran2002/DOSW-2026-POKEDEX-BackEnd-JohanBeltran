package com.pokedex.pokedex.core.service.impl;

import com.pokedex.pokedex.controller.dto.request.IntercambiarRequest;
import com.pokedex.pokedex.controller.dto.response.IntercambiarResponse;
import com.pokedex.pokedex.core.exception.BusinessException;
import com.pokedex.pokedex.core.exception.DuplicateResourceException;
import com.pokedex.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.pokedex.core.model.Usuario;
import com.pokedex.pokedex.core.service.interfaces.UsuarioPersistencePort;
import com.pokedex.pokedex.core.service.interfaces.UsuarioService;
import com.pokedex.pokedex.persistence.entity.relational.PokemonJpaRepository;
import com.pokedex.pokedex.persistence.entity.relational.TeamJpaRepository;
import com.pokedex.pokedex.persistence.entity.relational.UserJpaRepository;
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
    private final UserJpaRepository userRepo;
    private final PokemonJpaRepository pokemonRepo;
    private final TeamJpaRepository teamRepo;

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
    @Transactional(readOnly = true)
    public Usuario findById(Long id) {
        return usuarioPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario findByCorreo(String correo) {
        return usuarioPort.findByCorreo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "correo", correo));
    }

    @Override
    @Transactional(readOnly = true)
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

    @Override
    @Transactional
    public IntercambiarResponse intercambiar(IntercambiarRequest request) {
        if (request.ofertanteId().equals(request.receptorId())) {
            throw new BusinessException("No puedes intercambiar Pokémon contigo mismo", "SELF_TRADE");
        }

        if (request.pokemonOfertadoId().equals(request.pokemonSolicitadoId())) {
            throw new BusinessException("No puedes ofrecer y recibir el mismo Pokémon", "SAME_POKEMON");
        }

        var ofertante = userRepo.findById(request.ofertanteId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", request.ofertanteId()));
        var receptor = userRepo.findById(request.receptorId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", request.receptorId()));

        var pokemonOfertado = pokemonRepo.findById(request.pokemonOfertadoId())
                .orElseThrow(() -> new ResourceNotFoundException("Pokemon", "id", request.pokemonOfertadoId()));
        var pokemonSolicitado = pokemonRepo.findById(request.pokemonSolicitadoId())
                .orElseThrow(() -> new ResourceNotFoundException("Pokemon", "id", request.pokemonSolicitadoId()));

        var equiposOfertante = teamRepo.findByUsuarioId(request.ofertanteId());
        if (equiposOfertante.isEmpty()) {
            throw new BusinessException("El usuario ofertante no tiene Pokémon registrados", "NO_POKEMON");
        }

        var equipoOfertante = teamRepo.findByUsuarioIdAndPokemonId(request.ofertanteId(), request.pokemonOfertadoId())
                .orElseThrow(() -> new BusinessException("El Pokémon ofrecido no te pertenece", "NOT_OWNER"));

        var equipoReceptor = teamRepo.findByUsuarioIdAndPokemonId(request.receptorId(), request.pokemonSolicitadoId())
                .orElseThrow(() -> new BusinessException("El Pokémon solicitado no pertenece al otro usuario", "NOT_OWNER"));

        equipoOfertante.getPokemons().remove(pokemonOfertado);
        equipoOfertante.getPokemons().add(pokemonSolicitado);
        equipoReceptor.getPokemons().remove(pokemonSolicitado);
        equipoReceptor.getPokemons().add(pokemonOfertado);

        teamRepo.save(equipoOfertante);
        teamRepo.save(equipoReceptor);

        var pokemonIdsOfertante = teamRepo.findByUsuarioId(request.ofertanteId()).stream()
                .flatMap(t -> t.getPokemons().stream())
                .map(p -> p.getId())
                .distinct()
                .toList();
        var pokemonIdsReceptor = teamRepo.findByUsuarioId(request.receptorId()).stream()
                .flatMap(t -> t.getPokemons().stream())
                .map(p -> p.getId())
                .distinct()
                .toList();

        log.info("Intercambio realizado: usuario {} <-> usuario {}, pokemon {} <-> pokemon {}",
                request.ofertanteId(), request.receptorId(),
                request.pokemonOfertadoId(), request.pokemonSolicitadoId());

        return new IntercambiarResponse("Intercambio realizado exitosamente",
                pokemonIdsOfertante, pokemonIdsReceptor);
    }
}