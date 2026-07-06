package com.pokedex.pokedex.core.service.impl;

import com.pokedex.pokedex.core.exception.DuplicateResourceException;
import com.pokedex.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.pokedex.core.model.Favorito;
import com.pokedex.pokedex.core.service.interfaces.FavoritoService;
import com.pokedex.pokedex.persistence.entity.relational.FavoritoEntity;
import com.pokedex.pokedex.persistence.entity.relational.FavoritoJpaRepository;
import com.pokedex.pokedex.persistence.entity.relational.PokemonJpaRepository;
import com.pokedex.pokedex.persistence.entity.relational.UserJpaRepository;
import com.pokedex.pokedex.persistence.mapper.FavoritoPersistenceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavoritoServiceImpl implements FavoritoService {

    private final FavoritoJpaRepository favoritoRepository;
    private final UserJpaRepository userRepository;
    private final PokemonJpaRepository pokemonRepository;
    private final FavoritoPersistenceMapper mapper;

    @Override
    @Transactional
    public Favorito guardar(Long usuarioId, Long pokemonId) {
        if (favoritoRepository.existsByUsuarioIdAndPokemonId(usuarioId, pokemonId)) {
            throw new DuplicateResourceException("Favorito", "usuarioId y pokemonId",
                    usuarioId + " - " + pokemonId);
        }

        var usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuarioId));
        var pokemon = pokemonRepository.findById(pokemonId)
                .orElseThrow(() -> new ResourceNotFoundException("Pokemon", "id", pokemonId));

        FavoritoEntity entity = FavoritoEntity.builder()
                .usuario(usuario)
                .pokemon(pokemon)
                .fechaGuardado(LocalDateTime.now())
                .build();

        FavoritoEntity saved = favoritoRepository.save(entity);
        log.info("Favorito guardado: usuario {} -> pokemon {}", usuarioId, pokemonId);
        return mapper.toDomain(saved);
    }

    @Override
    @Transactional
    public void eliminar(Long usuarioId, Long pokemonId) {
        FavoritoEntity entity = favoritoRepository
                .findByUsuarioIdAndPokemonId(usuarioId, pokemonId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorito", "usuarioId y pokemonId",
                        usuarioId + " - " + pokemonId));
        favoritoRepository.delete(entity);
        log.info("Favorito eliminado: usuario {} -> pokemon {}", usuarioId, pokemonId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Favorito> findByUsuarioId(Long usuarioId) {
        return favoritoRepository.findByUsuarioId(usuarioId).stream()
                .map(mapper::toDomain)
                .toList();
    }
}
