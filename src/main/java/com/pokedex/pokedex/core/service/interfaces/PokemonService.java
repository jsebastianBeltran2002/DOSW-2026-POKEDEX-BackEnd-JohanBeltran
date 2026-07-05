package com.pokedex.pokedex.core.service.interfaces;

import com.pokedex.pokedex.core.model.Pokemon;
import java.util.List;

public interface PokemonService {
    List<Pokemon> findAll();
    Pokemon findById(Long id);
    Pokemon findByNumero(Integer numero);
    Pokemon create(Pokemon pokemon);
    Pokemon update(Long id, Pokemon pokemon);
    void delete(Long id);
    List<Pokemon> filtrar(String region, String tipo, String generacion,
                          String evolucion, String habilidad, String ataque,
                          Boolean tieneMega, Boolean legendario);
    List<Pokemon> ordenar(String criterio, String direccion);
}