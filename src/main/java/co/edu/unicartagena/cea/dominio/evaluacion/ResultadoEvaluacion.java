package co.edu.unicartagena.cea.dominio.evaluacion;

import co.edu.unicartagena.cea.dominio.compartido.Nota;
import co.edu.unicartagena.cea.dominio.compartido.NumeroMatricula;

/**
 * VALUE OBJECT: el resultado de un alumno en una evaluacion.
 * Componente principal = teorico (escrito) o laboratorio (practico).
 * Componente complementario = redaccion (escrito) o eficiencia y tiempo (practico).
 */
public record ResultadoEvaluacion(NumeroMatricula matricula, Nota notaComponentePrincipal,
                                  Nota notaComponenteComplementario, Nota notaFinal) {
}
