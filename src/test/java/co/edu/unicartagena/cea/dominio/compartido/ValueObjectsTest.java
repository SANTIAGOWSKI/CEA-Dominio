package co.edu.unicartagena.cea.dominio.compartido;

import static org.junit.jupiter.api.Assertions.*;

import co.edu.unicartagena.cea.dominio.excepcion.ExcepcionDeDominio;
import co.edu.unicartagena.cea.dominio.excepcion.PonderacionInvalidaException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ValueObjectsTest {

    @Test
    @DisplayName("Correo valido se normaliza a minusculas")
    void correoSeNormaliza() {
        assertEquals("ana@unicartagena.edu.co", new Correo("  Ana@UniCartagena.edu.co ").valor());
    }

    @Test
    @DisplayName("Correo sin arroba o vacio es invalido")
    void correoInvalido() {
        assertThrows(ExcepcionDeDominio.class, () -> new Correo("sin-arroba.com"));
        assertThrows(ExcepcionDeDominio.class, () -> new Correo(" "));
        assertThrows(ExcepcionDeDominio.class, () -> new Correo(null));
    }

    @Test
    @DisplayName("DNI debe tener entre 5 y 15 caracteres alfanumericos")
    void dniValidoEInvalido() {
        assertEquals("12345678", new Dni("12345678").valor());
        assertThrows(ExcepcionDeDominio.class, () -> new Dni("123"));
        assertThrows(ExcepcionDeDominio.class, () -> new Dni("12 345 678"));
    }

    @Test
    @DisplayName("Porcentaje debe estar entre 0 y 100")
    void porcentajeLimites() {
        assertEquals(0.8, Porcentaje.de(80).fraccion(), 1e-9);
        assertThrows(PonderacionInvalidaException.class, () -> Porcentaje.de(-1));
        assertThrows(PonderacionInvalidaException.class, () -> Porcentaje.de(101));
    }

    @Test
    @DisplayName("Periodo academico solo admite semestre 1 o 2")
    void periodoAcademico() {
        assertEquals(2026, PeriodoAcademico.de(2026, 1).anio());
        assertThrows(ExcepcionDeDominio.class, () -> PeriodoAcademico.de(2026, 3));
        assertThrows(ExcepcionDeDominio.class, () -> PeriodoAcademico.de(1800, 1));
    }

    @Test
    @DisplayName("Matricula y grupo no pueden estar vacios")
    void matriculaYGrupo() {
        assertThrows(ExcepcionDeDominio.class, () -> new NumeroMatricula(""));
        assertThrows(ExcepcionDeDominio.class, () -> new GrupoAcademico(null));
    }
}
