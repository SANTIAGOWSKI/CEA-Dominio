package co.edu.unicartagena.cea.dominio.evaluacion;

import co.edu.unicartagena.cea.dominio.compartido.PeriodoAcademico;
import co.edu.unicartagena.cea.dominio.compartido.Validar;
import java.time.LocalDate;
import java.util.Set;

/** AGREGADO: un control escrito (numero de control, numero de preguntas, ponderacion 80/20). */
public class ControlEscrito extends Evaluacion {

    private final int numeroControl;
    private final int numeroPreguntas;
    private final PonderacionControlEscrito ponderacion;

    public ControlEscrito(String id, LocalDate fecha, String codigoAsignatura, PeriodoAcademico periodo,
                          Set<String> temas, int numeroControl, int numeroPreguntas,
                          PonderacionControlEscrito ponderacion) {
        super(id, TipoEvaluacion.ESCRITO, fecha, codigoAsignatura, periodo, temas);
        this.numeroControl = Validar.positivo(numeroControl, "El numero de control");
        this.numeroPreguntas = Validar.positivo(numeroPreguntas, "El numero de preguntas");
        this.ponderacion = Validar.noNulo(ponderacion, "La ponderacion");
    }

    @Override
    protected PoliticaDeCalificacion politica() {
        return ponderacion;
    }

    public int numeroControl() { return numeroControl; }
    public int numeroPreguntas() { return numeroPreguntas; }
    public PonderacionControlEscrito ponderacion() { return ponderacion; }
}
