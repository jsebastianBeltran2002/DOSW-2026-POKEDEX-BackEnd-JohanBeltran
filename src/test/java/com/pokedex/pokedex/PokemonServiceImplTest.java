package com.pokedex.pokedex;

import com.pokedex.pokedex.core.exception.DuplicateResourceException;
import com.pokedex.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.pokedex.core.model.Pokemon;
import com.pokedex.pokedex.core.service.impl.PokemonServiceImpl;
import com.pokedex.pokedex.core.service.interfaces.PokemonPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PokemonServiceImplTest {

    @Mock
    private PokemonPersistencePort pokemonPort;

    @InjectMocks
    private PokemonServiceImpl pokemonService;

    private Pokemon pikachu;
    private Pokemon charmander;
    private Pokemon mewtwo;

    @BeforeEach
    void setUp() {
        pikachu = Pokemon.builder()
                .id(1L)
                .numero(25)
                .nombre("Pikachu")
                .descripcion("Raton electrico")
                .altura(0.4)
                .peso(6.0)
                .tipos(List.of("Electrico"))
                .region("Kanto")
                .generacion(1)
                .legendario(false)
                .tieneMega(false)
                .hp(35).ataque(55).defensa(40)
                .ataqueEspecial(50).defensaEspecial(50)
                .velocidad(90)
                .build();

        charmander = Pokemon.builder()
                .id(2L)
                .numero(4)
                .nombre("Charmander")
                .tipos(List.of("Fuego"))
                .region("Kanto")
                .generacion(1)
                .legendario(false)
                .tieneMega(false)
                .hp(39).ataque(52).defensa(43)
                .ataqueEspecial(60).defensaEspecial(50)
                .velocidad(65)
                .build();

        mewtwo = Pokemon.builder()
                .id(3L)
                .numero(150)
                .nombre("Mewtwo")
                .tipos(List.of("Psiquico"))
                .region("Kanto")
                .generacion(1)
                .legendario(true)
                .tieneMega(true)
                .hp(106).ataque(110).defensa(90)
                .ataqueEspecial(154).defensaEspecial(90)
                .velocidad(130)
                .build();
    }

    @Nested
    @DisplayName("findAll")
    class FindAll {

        @Test
        @DisplayName("retorna lista vacia cuando no hay pokemon")
        void returnsEmptyList() {
            when(pokemonPort.findAll()).thenReturn(List.of());
            var result = pokemonService.findAll();
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("retorna lista con pokemon cuando existen")
        void returnsListWithPokemon() {
            when(pokemonPort.findAll()).thenReturn(List.of(pikachu, charmander));
            var result = pokemonService.findAll();
            assertEquals(2, result.size());
            assertTrue(result.contains(pikachu));
            assertTrue(result.contains(charmander));
        }
    }

    @Nested
    @DisplayName("findById")
    class FindById {

        @Test
        @DisplayName("retorna pokemon cuando existe")
        void returnsPokemonWhenExists() {
            when(pokemonPort.findById(1L)).thenReturn(Optional.of(pikachu));
            var result = pokemonService.findById(1L);
            assertNotNull(result);
            assertEquals("Pikachu", result.getNombre());
        }

        @Test
        @DisplayName("lanza ResourceNotFoundException cuando no existe")
        void throwsExceptionWhenNotFound() {
            when(pokemonPort.findById(99L)).thenReturn(Optional.empty());
            assertThrows(ResourceNotFoundException.class, () -> pokemonService.findById(99L));
        }
    }

    @Nested
    @DisplayName("findByNumero")
    class FindByNumero {

        @Test
        @DisplayName("retorna pokemon cuando existe")
        void returnsPokemonWhenExists() {
            when(pokemonPort.findByNumero(25)).thenReturn(Optional.of(pikachu));
            var result = pokemonService.findByNumero(25);
            assertNotNull(result);
            assertEquals("Pikachu", result.getNombre());
        }

        @Test
        @DisplayName("lanza ResourceNotFoundException cuando no existe")
        void throwsExceptionWhenNotFound() {
            when(pokemonPort.findByNumero(999)).thenReturn(Optional.empty());
            assertThrows(ResourceNotFoundException.class, () -> pokemonService.findByNumero(999));
        }
    }

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("guarda y retorna pokemon cuando no hay duplicado")
        void createsPokemonWhenNotDuplicate() {
            when(pokemonPort.existsByNumero(25)).thenReturn(false);
            when(pokemonPort.save(pikachu)).thenReturn(pikachu);
            var result = pokemonService.create(pikachu);
            assertNotNull(result);
            assertEquals("Pikachu", result.getNombre());
            verify(pokemonPort).existsByNumero(25);
            verify(pokemonPort).save(pikachu);
        }

        @Test
        @DisplayName("lanza DuplicateResourceException cuando el numero ya existe")
        void throwsExceptionWhenDuplicate() {
            when(pokemonPort.existsByNumero(25)).thenReturn(true);
            assertThrows(DuplicateResourceException.class, () -> pokemonService.create(pikachu));
            verify(pokemonPort).existsByNumero(25);
            verify(pokemonPort, never()).save(any());
        }
    }

    @Nested
    @DisplayName("update")
    class Update {

        @Test
        @DisplayName("actualiza campos y retorna pokemon actualizado")
        void updatesPokemonWhenExists() {
            Pokemon updates = pikachu.toBuilder().nombre("Raichu").build();
            when(pokemonPort.findById(1L)).thenReturn(Optional.of(pikachu));
            when(pokemonPort.save(any(Pokemon.class))).thenAnswer(invocation -> invocation.getArgument(0));

            var result = pokemonService.update(1L, updates);

            assertEquals("Raichu", result.getNombre());
            assertEquals(25, result.getNumero());
            verify(pokemonPort).findById(1L);
            verify(pokemonPort).save(any(Pokemon.class));
        }

        @Test
        @DisplayName("lanza ResourceNotFoundException cuando el id no existe")
        void throwsExceptionWhenNotFound() {
            when(pokemonPort.findById(99L)).thenReturn(Optional.empty());
            assertThrows(ResourceNotFoundException.class, () -> pokemonService.update(99L, pikachu));
            verify(pokemonPort).findById(99L);
            verify(pokemonPort, never()).save(any());
        }
    }

    @Nested
    @DisplayName("delete")
    class Delete {

        @Test
        @DisplayName("elimina pokemon cuando existe")
        void deletesPokemonWhenExists() {
            when(pokemonPort.existsById(1L)).thenReturn(true);
            assertDoesNotThrow(() -> pokemonService.delete(1L));
            verify(pokemonPort).existsById(1L);
            verify(pokemonPort).deleteById(1L);
        }

        @Test
        @DisplayName("lanza ResourceNotFoundException cuando no existe")
        void throwsExceptionWhenNotFound() {
            when(pokemonPort.existsById(99L)).thenReturn(false);
            assertThrows(ResourceNotFoundException.class, () -> pokemonService.delete(99L));
            verify(pokemonPort).existsById(99L);
            verify(pokemonPort, never()).deleteById(any());
        }
    }

    @Nested
    @DisplayName("filtrar")
    class Filtrar {

        @Test
        @DisplayName("retorna todos cuando no hay filtros")
        void returnsAllWhenNoFilters() {
            when(pokemonPort.findAll()).thenReturn(List.of(pikachu, charmander, mewtwo));
            var result = pokemonService.filtrar(null, null, null, null, null, null, null, null);
            assertEquals(3, result.size());
        }

        @Test
        @DisplayName("filtra por region")
        void filtersByRegion() {
            var alola = pikachu.toBuilder().region("Alola").id(4L).build();
            when(pokemonPort.findAll()).thenReturn(List.of(pikachu, alola));
            var result = pokemonService.filtrar("Alola", null, null, null, null, null, null, null);
            assertEquals(1, result.size());
            assertEquals("Alola", result.getFirst().getRegion());
        }

        @Test
        @DisplayName("filtra por tipo")
        void filtersByType() {
            when(pokemonPort.findAll()).thenReturn(List.of(pikachu, charmander));
            var result = pokemonService.filtrar(null, "Fuego", null, null, null, null, null, null);
            assertEquals(1, result.size());
            assertEquals("Charmander", result.getFirst().getNombre());
        }

        @Test
        @DisplayName("filtra por generacion")
        void filtersByGeneracion() {
            var gen2 = pikachu.toBuilder().generacion(2).id(4L).build();
            when(pokemonPort.findAll()).thenReturn(List.of(pikachu, gen2));
            var result = pokemonService.filtrar(null, null, "2", null, null, null, null, null);
            assertEquals(1, result.size());
            assertEquals(2, result.getFirst().getGeneracion());
        }

        @Test
        @DisplayName("filtra por tieneMega")
        void filtersByTieneMega() {
            when(pokemonPort.findAll()).thenReturn(List.of(pikachu, mewtwo));
            var result = pokemonService.filtrar(null, null, null, null, null, null, true, null);
            assertEquals(1, result.size());
            assertTrue(result.getFirst().getTieneMega());
        }

        @Test
        @DisplayName("filtra por legendario")
        void filtersByLegendario() {
            when(pokemonPort.findAll()).thenReturn(List.of(pikachu, mewtwo));
            var result = pokemonService.filtrar(null, null, null, null, null, null, null, true);
            assertEquals(1, result.size());
            assertTrue(result.getFirst().getLegendario());
        }

        @Test
        @DisplayName("filtra por region y legendario combinado")
        void filtersByMultipleCriteria() {
            var hoennLegend = mewtwo.toBuilder().region("Hoenn").id(4L).build();
            when(pokemonPort.findAll()).thenReturn(List.of(pikachu, mewtwo, hoennLegend));
            var result = pokemonService.filtrar("Kanto", null, null, null, null, null, null, true);
            assertEquals(1, result.size());
            assertEquals("Mewtwo", result.getFirst().getNombre());
        }

        @Test
        @DisplayName("retorna lista vacia cuando ninguno coincide")
        void returnsEmptyWhenNoMatch() {
            when(pokemonPort.findAll()).thenReturn(List.of(pikachu));
            var result = pokemonService.filtrar("Alola", null, null, null, null, null, null, null);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("ordenar")
    class Ordenar {

        @Test
        @DisplayName("ordena por numero por defecto ascendente")
        void sortsByNumeroDefaultAsc() {
            when(pokemonPort.findAll()).thenReturn(List.of(charmander, pikachu));
            var result = pokemonService.ordenar(null, null);
            assertEquals(4, result.get(0).getNumero());
            assertEquals(25, result.get(1).getNumero());
        }

        @Test
        @DisplayName("ordena por nombre ascendente")
        void sortsByNombreAsc() {
            when(pokemonPort.findAll()).thenReturn(List.of(pikachu, charmander));
            var result = pokemonService.ordenar("nombre", "asc");
            assertEquals("Charmander", result.get(0).getNombre());
            assertEquals("Pikachu", result.get(1).getNombre());
        }

        @Test
        @DisplayName("ordena por nombre descendente")
        void sortsByNombreDesc() {
            when(pokemonPort.findAll()).thenReturn(List.of(charmander, pikachu));
            var result = pokemonService.ordenar("nombre", "desc");
            assertEquals("Pikachu", result.get(0).getNombre());
            assertEquals("Charmander", result.get(1).getNombre());
        }

        @Test
        @DisplayName("ordena por altura ascendente")
        void sortsByAlturaAsc() {
            var alto = pikachu.toBuilder().altura(2.0).build();
            when(pokemonPort.findAll()).thenReturn(List.of(alto, pikachu));
            var result = pokemonService.ordenar("altura", "asc");
            assertEquals(0.4, result.get(0).getAltura());
            assertEquals(2.0, result.get(1).getAltura());
        }

        @Test
        @DisplayName("ordena por peso ascendente")
        void sortsByPesoAsc() {
            var pesado = pikachu.toBuilder().peso(100.0).build();
            when(pokemonPort.findAll()).thenReturn(List.of(pesado, pikachu));
            var result = pokemonService.ordenar("peso", "asc");
            assertEquals(6.0, result.get(0).getPeso());
            assertEquals(100.0, result.get(1).getPeso());
        }
    }
}
