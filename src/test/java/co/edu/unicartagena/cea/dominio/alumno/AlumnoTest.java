package co.edu.unicartagena.cea.dominio.alumno;

import static org.junit.jupiter.api.Assertions.*;

import co.edu.unicartagena.cea.dominio.compartido.GrupoAcademico;
import co.edu.unicartagena.cea.dominio.compartido.NumeroMatricula;
import co.edu.unicartagena.cea.dominio.excepcion.ExcepcionDeDominio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AlumnoTest {

    private Alumno alumno(String matricula, String nombre, String grupo) {
        return new Alumno(new NumeroMatricula(matricula), nombre, new GrupoAcademico(grupo));
    }

    @Test
    @DisplayName("Dos alumnos con la misma matricula son la misma entidad aunque cambie el nombre")
    void identidadPorMatricula() {
        assertEquals(alumno("M1", "Ana", "G1"), alumno("M1", "Ana Maria", "G2"));
        assertNotEquals(alumno("M1", "Ana", "G1"), alumno("M2", "Ana", "G1"));
    }

    @Test
    @DisplayName("Un alumno puede cambiar de grupo sin cambiar su identidad")
    void cambiaDeGrupo() {
        Alumno a = alumno("M1", "Ana", "G1");
        a.cambiarDeGrupo(new GrupoAcademico("G2"));
        assertEquals("G2", a.grupo().codigo());
        assertEquals("M1", a.matricula().valor());
    }

    @Test
    @DisplayName("No se crea un alumno sin nombre")
    void nombreObligatorio() {
        assertThrows(ExcepcionDeDominio.class, () -> alumno("M1", " ", "G1"));
    }
}
