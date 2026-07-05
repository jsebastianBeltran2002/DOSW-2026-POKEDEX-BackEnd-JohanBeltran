package com.pokedex.pokedex.core.service.impl;

import com.pokedex.pokedex.core.exception.DuplicateResourceException;
import com.pokedex.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.pokedex.core.model.Pokemon;
import com.pokedex.pokedex.core.service.interfaces.PokemonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PokemonServiceImpl implements PokemonService {

    @Override
    public List<Pokemon> findAll() {
        return List.of();
    }

    @Override
    public Pokemon findById(Long id) {
        throw new ResourceNotFoundException("Pokemon", "id", id);
    }

    @Override
    public Pokemon findByNumero(Integer numero) {
        throw new ResourceNotFoundException("Pokemon", "numero", numero);
    }

    @Override
    public Pokemon create(Pokemon pokemon) {
        throw new DuplicateResourceException("Pokemon", "numero", pokemon.getNumero());
    }

    @Override
    public Pokemon update(Long id, Pokemon pokemon) {
        throw new ResourceNotFoundException("Pokemon", "id", id);
    }

    @Override
    public void delete(Long id) {
        throw new ResourceNotFoundException("Pokemon", "id", id);
    }

    @Override
    public List<Pokemon> filtrar(String region, String tipo, String generacion,
                                 String evolucion, String habilidad, String ataque,
                                 Boolean tieneMega, Boolean legendario) {
        return List.of();
    }

    @Override
    public List<Pokemon> ordenar(String criterio, String direccion) {
        return List.of();
    }
}