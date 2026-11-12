package co.edu.unicartagena.cea.dominio.evaluacion;

import static org.junit.jupiter.api.Assertions.*;

import co.edu.unicartagena.cea.dominio.compartido.Nota;
import co.edu.unicartagena.cea.dominio.compartido.NumeroMatricula;
import co.edu.unicartagena.cea.dominio.compartido.PeriodoAcademico;
import co.edu.unicartagena.cea.dominio.evento.EventoDeDominio;
import co.edu.unicartagena.cea.dominio.evento.NotaRegistrada;
import co.edu.unicartagena.cea.dominio.excepcion.ExcepcionDeDominio;
import co.edu.unicartagena.cea.dominio.excepcion.ResultadoDuplicadoException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ControlEscritoTest {

    private final NumeroMatricula ana = new NumeroMatricula("M1");
    private ControlEscrito control;

    @BeforeEach
    void preparar() {
        control = new ControlEscrito("CE-1", LocalDate.of(2026, 4, 10), "COMP2",
                PeriodoAcademico.de(2026, 1), Set.of("T1", "T2"), 1, 10,
                PonderacionControlEscrito.porDefecto());
    }

    @Test
    @DisplayName("Registra el resultado y el dominio calcula la nota final (80/20)")
    void registraResultado() {
        ResultadoEvaluacion r = control.registrarResultado(ana, Nota.de(6.0), Nota.de(5.0));
        assertEquals(5.8, r.notaFinal().valor(), 1e-9);
        assertEquals(5.8, control.notaDe(ana).orElseThrow().valor(), 1e-9);
    }

    @Test
    @DisplayName("Un alumno sin resultado no tiene nota")
    void alumnoSinResultado() {
        assertTrue(control.notaDe(ana).isEmpty());
    }

    @Test
    @DisplayName("Invariante: un alumno no puede tener dos resultados en el mismo control")
    void rechazaResultadoDuplicado() {
        control.registrarResultado(ana, Nota.de(6.0), Nota.de(5.0));
        assertThrows(ResultadoDuplicadoException.class,
                () -> control.registrarResultado(ana, Nota.de(7.0), Nota.de(7.0)));
    }

    @Test
    @DisplayName("Invariante: una evaluacion debe abarcar al menos un tema")
    void exigeAlMenosUnTema() {
        assertThrows(ExcepcionDeDominio.class, () -> new ControlEscrito("CE-2", LocalDate.of(2026, 4, 10),
                "COMP2", PeriodoAcademico.de(2026, 1), Set.of(), 2, 10, PonderacionControlEscrito.porDefecto()));
    }

    @Test
    @DisplayName("Invariante: pertenece a una asignatura y tiene numero de preguntas positivo")
    void camposObligatorios() {
        assertThrows(ExcepcionDeDominio.class, () -> new ControlEscrito("CE-2", LocalDate.of(2026, 4, 10),
                " ", PeriodoAcademico.de(2026, 1), Set.of("T1"), 2, 10, PonderacionControlEscrito.porDefecto()));
        assertThrows(ExcepcionDeDominio.class, () -> new ControlEscrito("CE-2", LocalDate.of(2026, 4, 10),
                "COMP2", PeriodoAcademico.de(2026, 1), Set.of("T1"), 2, 0, PonderacionControlEscrito.porDefecto()));
    }

    @Test
    @DisplayName("Conoce su ponderacion total, sus temas, su fecha comun y su periodo")
    void consultas() {
        assertEquals(100, control.ponderacionTotal());
        assertTrue(control.abarcaTema("T1"));
        assertFalse(control.abarcaTema("T9"));
        assertEquals(LocalDate.of(2026, 4, 10), control.fecha());
        assertEquals(1, control.periodo().semestre());
        assertEquals(TipoEvaluacion.ESCRITO, control.tipo());
    }

    @Test
    @DisplayName("Registrar una nota genera el evento NotaRegistrada (una sola vez)")
    void generaEvento() {
        control.registrarResultado(ana, Nota.de(6.0), Nota.de(5.0));
        List<EventoDeDominio> eventos = control.extraerEventos();
        assertEquals(1, eventos.size());
        NotaRegistrada evento = (NotaRegistrada) eventos.get(0);
        assertEquals("CE-1", evento.idEvaluacion());
        assertEquals(5.8, evento.notaFinal().valor(), 1e-9);
        assertTrue(control.extraerEventos().isEmpty());
    }
}
