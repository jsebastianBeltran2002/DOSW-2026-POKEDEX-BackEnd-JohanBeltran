package com.pokedex.pokedex.core.service.impl;

import com.pokedex.pokedex.core.exception.DuplicateResourceException;
import com.pokedex.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.pokedex.core.model.Pokemon;
import com.pokedex.pokedex.core.service.interfaces.PokemonPersistencePort;
import com.pokedex.pokedex.core.service.interfaces.PokemonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PokemonServiceImpl implements PokemonService {

    private final PokemonPersistencePort pokemonPort;

    @Override
    public List<Pokemon> findAll() {
        return pokemonPort.findAll();
    }

    @Override
    public Pokemon findById(Long id) {
        log.debug("Buscando Pokemon con id: {}", id);
        return pokemonPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pokemon", "id", id));
    }

    @Override
    public Pokemon findByNumero(Integer numero) {
        return pokemonPort.findByNumero(numero)
                .orElseThrow(() -> new ResourceNotFoundException("Pokemon", "numero", numero));
    }

    @Override
    @Transactional
    public Pokemon create(Pokemon pokemon) {
        if (pokemonPort.existsByNumero(pokemon.getNumero())) {
            throw new DuplicateResourceException("Pokemon", "numero", pokemon.getNumero());
        }
        log.info("Creando Pokemon: {}", pokemon.getNombre());
        return pokemonPort.save(pokemon);
    }

    @Override
    @Transactional
    public Pokemon update(Long id, Pokemon pokemon) {
        Pokemon existente = pokemonPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pokemon", "id", id));

        Pokemon actualizado = existente.toBuilder()
                .numero(pokemon.getNumero())
                .nombre(pokemon.getNombre())
                .descripcion(pokemon.getDescripcion())
                .altura(pokemon.getAltura())
                .peso(pokemon.getPeso())
                .sprite(pokemon.getSprite())
                .tipos(pokemon.getTipos())
                .region(pokemon.getRegion())
                .generacion(pokemon.getGeneracion())
                .legendario(pokemon.getLegendario())
                .tieneMega(pokemon.getTieneMega())
                .hp(pokemon.getHp())
                .ataque(pokemon.getAtaque())
                .defensa(pokemon.getDefensa())
                .ataqueEspecial(pokemon.getAtaqueEspecial())
                .defensaEspecial(pokemon.getDefensaEspecial())
                .velocidad(pokemon.getVelocidad())
                .build();

        return pokemonPort.save(actualizado);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!pokemonPort.existsById(id)) {
            throw new ResourceNotFoundException("Pokemon", "id", id);
        }
        pokemonPort.deleteById(id);
    }

    @Override
    public List<Pokemon> filtrar(String region, String tipo, String generacion,
                                 String evolucion, String habilidad, String ataque,
                                 Boolean tieneMega, Boolean legendario) {
        return pokemonPort.findAll().stream()
                .filter(p -> region == null || region.equalsIgnoreCase(p.getRegion()))
                .filter(p -> tipo == null || (p.getTipos() != null && p.getTipos().stream()
                        .anyMatch(t -> t.equalsIgnoreCase(tipo))))
                .filter(p -> generacion == null || generacion.equals(String.valueOf(p.getGeneracion())))
                .filter(p -> tieneMega == null || tieneMega.equals(p.getTieneMega()))
                .filter(p -> legendario == null || legendario.equals(p.getLegendario()))
                .toList();
    }

    @Override
    public List<Pokemon> ordenar(String criterio, String direccion) {
        Comparator<Pokemon> comparator = switch (criterio == null ? "" : criterio) {
            case "nombre" -> Comparator.comparing(Pokemon::getNombre, Comparator.nullsLast(String::compareTo));
            case "altura" -> Comparator.comparing(Pokemon::getAltura, Comparator.nullsLast(Double::compareTo));
            case "peso" -> Comparator.comparing(Pokemon::getPeso, Comparator.nullsLast(Double::compareTo));
            default -> Comparator.comparing(Pokemon::getNumero, Comparator.nullsLast(Integer::compareTo));
        };

        if ("desc".equalsIgnoreCase(direccion)) {
            comparator = comparator.reversed();
        }

        return pokemonPort.findAll().stream().sorted(comparator).toList();
    }
}