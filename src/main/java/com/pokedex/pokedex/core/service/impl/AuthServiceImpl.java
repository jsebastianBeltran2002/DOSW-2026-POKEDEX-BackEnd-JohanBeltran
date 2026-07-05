package com.pokedex.pokedex.core.service.impl;

import com.pokedex.pokedex.core.exception.BusinessException;
import com.pokedex.pokedex.core.model.Usuario;
import com.pokedex.pokedex.core.service.interfaces.AuthService;
import com.pokedex.pokedex.core.service.interfaces.UsuarioService;
import com.pokedex.pokedex.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioService usuarioService;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public Usuario register(Usuario usuario) {
        return usuarioService.crear(usuario);
    }

    @Override
    public String login(String correo, String password) {
        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(correo);
        } catch (Exception e) {
            throw new BusinessException("Correo o contraseña incorrectos", "INVALID_CREDENTIALS");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BusinessException("Correo o contraseña incorrectos", "INVALID_CREDENTIALS");
        }

        return jwtService.generateToken(userDetails);
    }
}