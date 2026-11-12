package co.edu.unicartagena.cea.dominio.servicio;

import static org.junit.jupiter.api.Assertions.*;

import co.edu.unicartagena.cea.dominio.compartido.Dni;
import co.edu.unicartagena.cea.dominio.compartido.NivelDificultad;
import co.edu.unicartagena.cea.dominio.compartido.Nota;
import co.edu.unicartagena.cea.dominio.compartido.NumeroMatricula;
import co.edu.unicartagena.cea.dominio.compartido.PeriodoAcademico;
import co.edu.unicartagena.cea.dominio.evaluacion.ControlEscrito;
import co.edu.unicartagena.cea.dominio.fabrica.EvaluacionFactory;
import co.edu.unicartagena.cea.dominio.practica.DisenoDePractica;
import co.edu.unicartagena.cea.dominio.practica.ModalidadPractica;
import co.edu.unicartagena.cea.dominio.practica.Practica;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ServicioDeDesempenoAcademicoTest {

    private final NumeroMatricula a = new NumeroMatricula("A");
    private final NumeroMatricula b = new NumeroMatricula("B");
    private final NumeroMatricula c = new NumeroMatricula("C");
    private final ServicioDeDesempenoAcademico servicio = new ServicioDeDesempenoAcademico();
    private ControlEscrito control1;
    private ControlEscrito control2;

    /** Registra una nota final igual a "nota" (mismo valor en ambos componentes). */
    private void registrar(ControlEscrito control, NumeroMatricula m, double nota) {
        control.registrarResultado(m, Nota.de(nota), Nota.de(nota));
    }

    private Practica practica(String codigo, ModalidadPractica modalidad) {
        return new Practica(codigo, "Titulo " + codigo, NivelDificultad.BAJO, modalidad,
                PeriodoAcademico.de(2026, 1),
                List.of(new DisenoDePractica(new Dni("11111111"), LocalDate.of(2026, 1, 10))));
    }

    @BeforeEach
    void preparar() {
        PeriodoAcademico periodo = PeriodoAcademico.de(2026, 1);
        control1 = EvaluacionFactory.crearControlEscrito("CE-1", LocalDate.of(2026, 3, 1), "COMP2", periodo, Set.of("T1"), 1, 10);
        control2 = EvaluacionFactory.crearControlEscrito("CE-2", LocalDate.of(2026, 4, 1), "COMP2", periodo, Set.of("T2"), 2, 10);
        registrar(control1, a, 6.0);
        registrar(control1, b, 4.0);
        registrar(control1, c, 5.0);
        registrar(control2, a, 6.5);
        registrar(control2, b, 5.5);
        registrar(control2, c, 3.0);
    }

    @Test
    @DisplayName("Calcula el promedio de notas y devuelve vacio si no hay notas")
    void promedio() {
        assertEquals(5.0, servicio.promedio(List.of(Nota.de(4.0), Nota.de(6.0))).orElseThrow().valor(), 1e-9);
        assertTrue(servicio.promedio(List.of()).isEmpty());
    }

    @Test
    @DisplayName("Alumnos con nota superior al umbral en TODOS los controles")
    void superiorAlUmbralEnTodos() {
        assertEquals(Set.of(a), servicio.alumnosConNotaSuperiorEnTodos(List.of(control1, control2), Nota.de(4.5)));
        assertEquals(Set.of(a, b), servicio.alumnosConNotaSuperiorEnTodos(List.of(control1, control2), Nota.de(3.5)));
    }

    @Test
    @DisplayName("Sin controles no hay alumnos que cumplan")
    void sinControles() {
        assertTrue(servicio.alumnosConNotaSuperiorEnTodos(List.of(), Nota.de(4.0)).isEmpty());
        assertTrue(servicio.alumnosSobreElPromedioEnTodos(List.of()).isEmpty());
    }

    @Test
    @DisplayName("Alumnos por encima del promedio en TODOS los controles")
    void sobreElPromedioEnTodos() {
        assertEquals(Set.of(a), servicio.alumnosSobreElPromedioEnTodos(List.of(control1, control2)));
    }

    @Test
    @DisplayName("Promedio de un grupo solo en practicas individuales")
    void promedioDelGrupoEnIndividuales() {
        Practica p1 = practica("P1", ModalidadPractica.INDIVIDUAL);
        p1.registrarParticipacion(a, LocalDate.of(2026, 3, 1), Nota.de(6.0));
        p1.registrarParticipacion(b, LocalDate.of(2026, 3, 1), Nota.de(4.0));
        Practica grupal = practica("P2", ModalidadPractica.GRUPAL);
        grupal.registrarParticipacion(a, LocalDate.of(2026, 3, 2), Nota.de(7.0));
        Practica p3 = practica("P3", ModalidadPractica.INDIVIDUAL);
        p3.registrarParticipacion(c, LocalDate.of(2026, 3, 3), Nota.de(2.0));

        var promedio = servicio.promedioDelGrupoEnPracticasIndividuales(List.of(p1, grupal, p3), Set.of(a, b));
        assertEquals(5.0, promedio.orElseThrow().valor(), 1e-9);
    }

    @Test
    @DisplayName("Alumnos que presentaron mas de una practica en grupo")
    void masDeUnaPracticaGrupal() {
        Practica g1 = practica("G1", ModalidadPractica.GRUPAL);
        g1.registrarParticipacion(a, LocalDate.of(2026, 3, 1), Nota.de(6.0));
        g1.registrarParticipacion(b, LocalDate.of(2026, 3, 1), Nota.de(6.0));
        Practica g2 = practica("G2", ModalidadPractica.GRUPAL);
        g2.registrarParticipacion(a, LocalDate.of(2026, 3, 8), Nota.de(5.0));
        Practica individual = practica("I1", ModalidadPractica.INDIVIDUAL);
        individual.registrarParticipacion(b, LocalDate.of(2026, 3, 9), Nota.de(5.0));

        assertEquals(Set.of(a), servicio.alumnosConMasDeUnaPracticaGrupal(List.of(g1, g2, individual)));
    }
}
