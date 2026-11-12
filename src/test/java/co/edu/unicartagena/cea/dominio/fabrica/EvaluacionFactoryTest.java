package co.edu.unicartagena.cea.dominio.fabrica;

import static org.junit.jupiter.api.Assertions.*;

import co.edu.unicartagena.cea.dominio.compartido.PeriodoAcademico;
import co.edu.unicartagena.cea.dominio.evaluacion.ControlEscrito;
import co.edu.unicartagena.cea.dominio.evaluacion.ExamenPractico;
import co.edu.unicartagena.cea.dominio.evaluacion.TipoEvaluacion;
import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EvaluacionFactoryTest {

    @Test
    @DisplayName("El control escrito nace con las ponderaciones oficiales 80/20")
    void controlEscrito() {
        ControlEscrito control = EvaluacionFactory.crearControlEscrito("CE-1", LocalDate.of(2026, 3, 1),
                "COMP2", PeriodoAcademico.de(2026, 1), Set.of("T1"), 1, 12);
        assertEquals(TipoEvaluacion.ESCRITO, control.tipo());
        assertEquals(80, control.ponderacion().teoricoConceptual().valor());
        assertEquals(20, control.ponderacion().redaccionOrtografia().valor());
    }

    @Test
    @DisplayName("El examen practico nace con las ponderaciones oficiales 70/30")
    void examenPractico() {
        ExamenPractico examen = EvaluacionFactory.crearExamenPractico("EP-1", LocalDate.of(2026, 5, 1),
                "COMP2", PeriodoAcademico.de(2026, 1), Set.of("T1"));
        assertEquals(TipoEvaluacion.PRACTICO, examen.tipo());
        assertEquals(70, examen.ponderacion().laboratorio().valor());
        assertEquals(30, examen.ponderacion().eficienciaYTiempo().valor());
    }
}
