package com.pokedex.pokedex.core.service.impl;

import com.pokedex.pokedex.core.exception.DuplicateResourceException;
import com.pokedex.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.pokedex.core.model.Equipo;
import com.pokedex.pokedex.core.service.interfaces.EquipoService;
import com.pokedex.pokedex.persistence.entity.relational.PokemonEntity;
import com.pokedex.pokedex.persistence.entity.relational.PokemonJpaRepository;
import com.pokedex.pokedex.persistence.entity.relational.TeamEntity;
import com.pokedex.pokedex.persistence.entity.relational.TeamJpaRepository;
import com.pokedex.pokedex.persistence.entity.relational.UserJpaRepository;
import com.pokedex.pokedex.persistence.mapper.EquipoPersistenceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EquipoServiceImpl implements EquipoService {

    private final TeamJpaRepository equipoRepository;
    private final UserJpaRepository userRepository;
    private final PokemonJpaRepository pokemonRepository;
    private final EquipoPersistenceMapper mapper;

    @Override
    @Transactional
    public Equipo crear(Equipo equipo) {
        if (equipoRepository.existsByNombreAndUsuarioId(equipo.getNombre(), equipo.getUsuarioId())) {
            throw new DuplicateResourceException("Equipo", "nombre", equipo.getNombre());
        }

        var usuario = userRepository.findById(equipo.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", equipo.getUsuarioId()));

        List<PokemonEntity> pokemonEntities = equipo.getPokemons().stream()
                .map(p -> pokemonRepository.findById(p.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Pokemon", "id", p.getId())))
                .toList();

        TeamEntity entity = TeamEntity.builder()
                .nombre(equipo.getNombre())
                .analisisCompetitivo(equipo.getAnalisisCompetitivo())
                .fechaCreacion(LocalDateTime.now())
                .usuario(usuario)
                .pokemons(pokemonEntities)
                .build();

        TeamEntity saved = equipoRepository.save(entity);
        log.info("Equipo creado: {} para usuario {}", saved.getNombre(), equipo.getUsuarioId());
        return mapper.toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Equipo findById(Long id) {
        return equipoRepository.findById(id)
                .map(mapper::toDomain)
                .orElseThrow(() -> new ResourceNotFoundException("Equipo", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Equipo> findByUsuarioId(Long usuarioId) {
        return equipoRepository.findByUsuarioId(usuarioId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!equipoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Equipo", "id", id);
        }
        equipoRepository.deleteById(id);
        log.info("Equipo eliminado: {}", id);
    }
}
