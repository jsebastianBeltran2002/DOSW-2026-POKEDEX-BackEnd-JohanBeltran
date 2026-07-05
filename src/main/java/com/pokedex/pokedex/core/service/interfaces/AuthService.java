package com.pokedex.pokedex.core.service.interfaces;

import com.pokedex.pokedex.core.model.Usuario;

public interface AuthService {
    Usuario register(Usuario usuario);
    String login(String correo, String password);
}