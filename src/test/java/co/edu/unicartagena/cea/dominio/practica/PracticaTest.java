package co.edu.unicartagena.cea.dominio.practica;

import static org.junit.jupiter.api.Assertions.*;

import co.edu.unicartagena.cea.dominio.compartido.Dni;
import co.edu.unicartagena.cea.dominio.compartido.NivelDificultad;
import co.edu.unicartagena.cea.dominio.compartido.Nota;
import co.edu.unicartagena.cea.dominio.compartido.NumeroMatricula;
import co.edu.unicartagena.cea.dominio.compartido.PeriodoAcademico;
import co.edu.unicartagena.cea.dominio.evento.PracticaPresentada;
import co.edu.unicartagena.cea.dominio.excepcion.ExcepcionDeDominio;
import co.edu.unicartagena.cea.dominio.excepcion.PracticaSinProfesorException;
import co.edu.unicartagena.cea.dominio.excepcion.ResultadoDuplicadoException;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PracticaTest {

    private final Dni carlos = new Dni("11111111");
    private final Dni lucia = new Dni("22222222");
    private Practica practica;

    @BeforeEach
    void preparar() {
        practica = new Practica("P1", "Listas enlazadas", NivelDificultad.MEDIO, ModalidadPractica.INDIVIDUAL,
                PeriodoAcademico.de(2026, 1), List.of(new DisenoDePractica(carlos, LocalDate.of(2026, 1, 15))));
    }

    @Test
    @DisplayName("Invariante: una practica no puede quedar sin profesor disenador")
    void exigeProfesor() {
        assertThrows(PracticaSinProfesorException.class, () -> new Practica("P2", "Pilas", NivelDificultad.BAJO,
                ModalidadPractica.GRUPAL, PeriodoAcademico.de(2026, 1), List.of()));
    }

    @Test
    @DisplayName("Pueden colaborar varios profesores, cada uno con su fecha de diseno")
    void colaboracion() {
        assertFalse(practica.esColaborativa());
        practica.agregarDiseno(new DisenoDePractica(lucia, LocalDate.of(2026, 2, 1)));
        assertTrue(practica.esColaborativa());
        assertEquals(2, practica.profesoresDisenadores().size());
    }

    @Test
    @DisplayName("El mismo profesor no puede aparecer dos veces como disenador")
    void sinDisenadoresRepetidos() {
        assertThrows(ExcepcionDeDominio.class,
                () -> practica.agregarDiseno(new DisenoDePractica(carlos, LocalDate.of(2026, 3, 1))));
    }

    @Test
    @DisplayName("Consulta si fue disenada por un profesor, y dentro de un rango de fechas")
    void consultasDeDiseno() {
        assertTrue(practica.disenadaPor(carlos));
        assertFalse(practica.disenadaPor(lucia));
        assertTrue(practica.disenadaPorEntre(carlos, LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31)));
        assertFalse(practica.disenadaPorEntre(carlos, LocalDate.of(2026, 2, 1), LocalDate.of(2026, 2, 28)));
    }

    @Test
    @DisplayName("Un alumno registra fecha y nota al presentar la practica")
    void registraParticipacion() {
        NumeroMatricula ana = new NumeroMatricula("M1");
        Participacion p = practica.registrarParticipacion(ana, LocalDate.of(2026, 3, 10), Nota.de(6.5));
        assertEquals(6.5, p.nota().valor());
        assertEquals(LocalDate.of(2026, 3, 10), practica.participaciones().get(ana).fecha());
    }

    @Test
    @DisplayName("Invariante: un alumno solo presenta una vez la misma practica")
    void rechazaParticipacionDuplicada() {
        NumeroMatricula ana = new NumeroMatricula("M1");
        practica.registrarParticipacion(ana, LocalDate.of(2026, 3, 10), Nota.de(6.5));
        assertThrows(ResultadoDuplicadoException.class,
                () -> practica.registrarParticipacion(ana, LocalDate.of(2026, 3, 11), Nota.de(7.0)));
    }

    @Test
    @DisplayName("Presentar una practica genera el evento PracticaPresentada")
    void generaEvento() {
        practica.registrarParticipacion(new NumeroMatricula("M1"), LocalDate.of(2026, 3, 10), Nota.de(6.5));
        var eventos = practica.extraerEventos();
        assertEquals(1, eventos.size());
        assertInstanceOf(PracticaPresentada.class, eventos.get(0));
    }
}
