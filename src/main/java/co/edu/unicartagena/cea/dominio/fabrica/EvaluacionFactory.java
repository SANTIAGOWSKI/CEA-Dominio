package co.edu.unicartagena.cea.dominio.fabrica;

import co.edu.unicartagena.cea.dominio.compartido.PeriodoAcademico;
import co.edu.unicartagena.cea.dominio.evaluacion.ControlEscrito;
import co.edu.unicartagena.cea.dominio.evaluacion.ExamenPractico;
import co.edu.unicartagena.cea.dominio.evaluacion.PonderacionControlEscrito;
import co.edu.unicartagena.cea.dominio.evaluacion.PonderacionExamenPractico;
import java.time.LocalDate;
import java.util.Set;

/**
 * Patron FACTORY: sabe construir evaluaciones con las ponderaciones oficiales del CEA
 * (80/20 para controles escritos, 70/30 para examenes practicos), asi nadie las repite ni se equivoca.
 */
public final class EvaluacionFactory {

    private EvaluacionFactory() {
    }

    public static ControlEscrito crearControlEscrito(String id, LocalDate fecha, String codigoAsignatura,
                                                     PeriodoAcademico periodo, Set<String> temas,
                                                     int numeroControl, int numeroPreguntas) {
        return new ControlEscrito(id, fecha, codigoAsignatura, periodo, temas, numeroControl,
                numeroPreguntas, PonderacionControlEscrito.porDefecto());
    }

    public static ExamenPractico crearExamenPractico(String id, LocalDate fecha, String codigoAsignatura,
                                                     PeriodoAcademico periodo, Set<String> temas) {
        return new ExamenPractico(id, fecha, codigoAsignatura, periodo, temas,
                PonderacionExamenPractico.porDefecto());
    }
}
