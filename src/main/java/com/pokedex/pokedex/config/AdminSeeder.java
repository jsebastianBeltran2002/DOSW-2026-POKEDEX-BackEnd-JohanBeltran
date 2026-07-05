package com.pokedex.pokedex.config;

import com.pokedex.pokedex.persistence.entity.relational.UserEntity;
import com.pokedex.pokedex.persistence.entity.relational.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminSeeder implements CommandLineRunner {

    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String correoAdmin = "admin@pokedex.com";

        if (userJpaRepository.existsByCorreo(correoAdmin)) {
            log.info("Admin ya existe, no se crea de nuevo.");
            return;
        }

        UserEntity admin = UserEntity.builder()
                .nombre("Admin")
                .correo(correoAdmin)
                .password(passwordEncoder.encode("Admin1234"))
                .activo(true)
                .rol(UserEntity.Rol.ADMINISTRADOR)
                .fechaRegistro(LocalDateTime.now())
                .build();

        userJpaRepository.save(admin);
        log.info("Usuario ADMIN creado -> correo: {} / password: Admin1234", correoAdmin);
    }
}