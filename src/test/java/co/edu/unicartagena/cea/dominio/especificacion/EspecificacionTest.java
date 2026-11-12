package co.edu.unicartagena.cea.dominio.especificacion;

import static org.junit.jupiter.api.Assertions.*;

import co.edu.unicartagena.cea.dominio.compartido.Correo;
import co.edu.unicartagena.cea.dominio.compartido.Dni;
import co.edu.unicartagena.cea.dominio.compartido.NivelDificultad;
import co.edu.unicartagena.cea.dominio.compartido.PeriodoAcademico;
import co.edu.unicartagena.cea.dominio.practica.DisenoDePractica;
import co.edu.unicartagena.cea.dominio.practica.ModalidadPractica;
import co.edu.unicartagena.cea.dominio.practica.Practica;
import co.edu.unicartagena.cea.dominio.profesor.Contrato;
import co.edu.unicartagena.cea.dominio.profesor.Profesor;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EspecificacionTest {

    private Profesor profesor(String dni, String departamento, LocalDate fin) {
        return new Profesor(new Dni(dni), "Prof " + dni, new Correo(dni + "@uni.edu"), "3000000000",
                new Contrato(departamento, "Titular", LocalDate.of(2020, 1, 1), fin));
    }

    @Test
    @DisplayName("Profesor con contrato activo en un departamento y fecha")
    void profesorActivoEnDepartamento() {
        Especificacion<Profesor> spec = new ProfesorActivoEnDepartamento("Sistemas", LocalDate.of(2026, 5, 1));
        assertTrue(spec.esSatisfechaPor(profesor("11111111", "Sistemas", null)));
        assertFalse(spec.esSatisfechaPor(profesor("22222222", "Matematicas", null)));
        assertFalse(spec.esSatisfechaPor(profesor("33333333", "Sistemas", LocalDate.of(2024, 1, 1))));
    }

    @Test
    @DisplayName("Las especificaciones se combinan con y(), o() y no()")
    void combinaciones() {
        Especificacion<Profesor> sistemas = new ProfesorActivoEnDepartamento("Sistemas", LocalDate.of(2026, 5, 1));
        Especificacion<Profesor> matematicas = new ProfesorActivoEnDepartamento("Matematicas", LocalDate.of(2026, 5, 1));
        Profesor deSistemas = profesor("11111111", "Sistemas", null);

        assertTrue(sistemas.o(matematicas).esSatisfechaPor(deSistemas));
        assertFalse(sistemas.y(matematicas).esSatisfechaPor(deSistemas));
        assertFalse(sistemas.no().esSatisfechaPor(deSistemas));
    }

    @Test
    @DisplayName("Practica disenada en colaboracion")
    void practicaColaborativa() {
        Practica sola = new Practica("P1", "T", NivelDificultad.BAJO, ModalidadPractica.INDIVIDUAL,
                PeriodoAcademico.de(2026, 1), List.of(new DisenoDePractica(new Dni("11111111"), LocalDate.of(2026, 1, 1))));
        Practica colaborativa = new Practica("P2", "T", NivelDificultad.BAJO, ModalidadPractica.GRUPAL,
                PeriodoAcademico.de(2026, 1), List.of(
                        new DisenoDePractica(new Dni("11111111"), LocalDate.of(2026, 1, 1)),
                        new DisenoDePractica(new Dni("22222222"), LocalDate.of(2026, 1, 2))));
        Especificacion<Practica> spec = new PracticaDisenadaEnColaboracion();
        assertFalse(spec.esSatisfechaPor(sola));
        assertTrue(spec.esSatisfechaPor(colaborativa));
    }
}
