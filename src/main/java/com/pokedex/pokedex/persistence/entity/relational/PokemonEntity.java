package com.pokedex.pokedex.persistence.entity.relational;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pokemon")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PokemonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "numero", nullable = false, unique = true)
    private Integer numero;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(length = 1000)
    private String descripcion;

    private Double altura;

    private Double peso;

    private String sprite;

    private Integer hp;
    private Integer ataque;
    private Integer defensa;
    private Integer ataqueEspecial;
    private Integer defensaEspecial;
    private Integer velocidad;

    private Boolean legendario;
    private Boolean tieneMega;

    @ElementCollection
    @CollectionTable(name = "pokemon_tipos", joinColumns = @JoinColumn(name = "pokemon_id"))
    @Column(name = "tipo")
    @Builder.Default
    private List<String> tipos = new ArrayList<>();

    private String region;
    private Integer generacion;
}