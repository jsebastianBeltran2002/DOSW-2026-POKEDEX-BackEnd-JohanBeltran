package com.pokedex.pokedex.security;

import com.pokedex.pokedex.persistence.entity.relational.UserEntity;
import com.pokedex.pokedex.persistence.entity.relational.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserJpaRepository userJpaRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        UserEntity entity = userJpaRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + correo));

        return User.builder()
                .username(entity.getCorreo())
                .password(entity.getPassword())
                .disabled(!entity.isActivo())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + entity.getRol().name())))
                .build();
    }
}