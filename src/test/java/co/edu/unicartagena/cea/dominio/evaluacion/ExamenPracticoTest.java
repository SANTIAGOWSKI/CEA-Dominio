package co.edu.unicartagena.cea.dominio.evaluacion;

import static org.junit.jupiter.api.Assertions.*;

import co.edu.unicartagena.cea.dominio.compartido.Nota;
import co.edu.unicartagena.cea.dominio.compartido.NumeroMatricula;
import co.edu.unicartagena.cea.dominio.compartido.PeriodoAcademico;
import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ExamenPracticoTest {

    private ExamenPractico examen;

    @BeforeEach
    void preparar() {
        examen = new ExamenPractico("EP-1", LocalDate.of(2026, 5, 20), "COMP2",
                PeriodoAcademico.de(2026, 1), Set.of("T3"), PonderacionExamenPractico.porDefecto());
    }

    @Test
    @DisplayName("Calcula la nota final con 70% laboratorio y 30% eficiencia")
    void calculaNotaFinal() {
        ResultadoEvaluacion r = examen.registrarResultado(new NumeroMatricula("M1"), Nota.de(7.0), Nota.de(4.0));
        assertEquals(6.1, r.notaFinal().valor(), 1e-9);
        assertEquals(TipoEvaluacion.PRACTICO, examen.tipo());
    }

    @Test
    @DisplayName("Puede asociarse a practicas por su codigo (sin duplicarlas)")
    void asociaPracticas() {
        examen.asociarPractica("P1");
        examen.asociarPractica("P1");
        examen.asociarPractica("P2");
        assertEquals(2, examen.codigosPracticas().size());
    }

    @Test
    @DisplayName("Expone el peso del componente de eficiencia y tiempo y el total")
    void exponePesos() {
        assertEquals(30, examen.ponderacion().eficienciaYTiempo().valor());
        assertEquals(70, examen.ponderacion().laboratorio().valor());
        assertEquals(100, examen.ponderacionTotal());
    }
}
