package co.edu.unicartagena.cea.dominio.evaluacion;

import co.edu.unicartagena.cea.dominio.compartido.PeriodoAcademico;
import co.edu.unicartagena.cea.dominio.compartido.Validar;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/** AGREGADO: un examen practico (ponderacion 70/30) que puede asociarse a practicas por su codigo. */
public class ExamenPractico extends Evaluacion {

    private final PonderacionExamenPractico ponderacion;
    private final Set<String> codigosPracticas = new LinkedHashSet<>();

    public ExamenPractico(String id, LocalDate fecha, String codigoAsignatura, PeriodoAcademico periodo,
                          Set<String> temas, PonderacionExamenPractico ponderacion) {
        super(id, TipoEvaluacion.PRACTICO, fecha, codigoAsignatura, periodo, temas);
        this.ponderacion = Validar.noNulo(ponderacion, "La ponderacion");
    }

    public void asociarPractica(String codigoPractica) {
        codigosPracticas.add(Validar.noVacio(codigoPractica, "El codigo de la practica"));
    }

    @Override
    protected PoliticaDeCalificacion politica() {
        return ponderacion;
    }

    public PonderacionExamenPractico ponderacion() { return ponderacion; }
    public Set<String> codigosPracticas() { return Collections.unmodifiableSet(codigosPracticas); }
}
