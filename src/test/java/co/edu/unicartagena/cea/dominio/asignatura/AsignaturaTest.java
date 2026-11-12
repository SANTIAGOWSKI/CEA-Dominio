package co.edu.unicartagena.cea.dominio.asignatura;

import static org.junit.jupiter.api.Assertions.*;

import co.edu.unicartagena.cea.dominio.compartido.Dni;
import co.edu.unicartagena.cea.dominio.compartido.PeriodoAcademico;
import co.edu.unicartagena.cea.dominio.excepcion.ExcepcionDeDominio;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AsignaturaTest {

    private final List<FranjaHoraria> horario =
            List.of(new FranjaHoraria(DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(10, 0)));
    private Asignatura asignatura;

    @BeforeEach
    void preparar() {
        asignatura = new Asignatura("COMP2", "Computacion II", "Estructuras de datos", 4,
                PeriodoAcademico.de(2026, 1), horario);
    }

    @Test
    @DisplayName("Los creditos deben ser positivos")
    void creditosPositivos() {
        assertThrows(ExcepcionDeDominio.class, () -> new Asignatura("X", "N", "D", 0,
                PeriodoAcademico.de(2026, 1), horario));
    }

    @Test
    @DisplayName("Debe tener al menos una franja de horario")
    void horarioObligatorio() {
        assertThrows(ExcepcionDeDominio.class, () -> new Asignatura("X", "N", "D", 3,
                PeriodoAcademico.de(2026, 1), List.of()));
    }

    @Test
    @DisplayName("La franja horaria no puede terminar antes de empezar")
    void franjaInvalida() {
        assertThrows(ExcepcionDeDominio.class,
                () -> new FranjaHoraria(DayOfWeek.FRIDAY, LocalTime.of(10, 0), LocalTime.of(8, 0)));
    }

    @Test
    @DisplayName("Asocia temas por id y no permite repetirlos")
    void asociaTemas() {
        asignatura.asociarTema("T1");
        assertTrue(asignatura.contieneTema("T1"));
        assertThrows(ExcepcionDeDominio.class, () -> asignatura.asociarTema("T1"));
    }

    @Test
    @DisplayName("Una asignatura puede ser impartida por varios profesores, sin repetir")
    void asignaProfesores() {
        asignatura.asignarProfesor(new Dni("11111111"));
        asignatura.asignarProfesor(new Dni("22222222"));
        assertEquals(2, asignatura.profesores().size());
        assertTrue(asignatura.imparteProfesor(new Dni("11111111")));
        assertThrows(ExcepcionDeDominio.class, () -> asignatura.asignarProfesor(new Dni("11111111")));
    }
}
