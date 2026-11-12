package co.edu.unicartagena.cea.dominio.evento;

import co.edu.unicartagena.cea.dominio.compartido.Nota;
import co.edu.unicartagena.cea.dominio.compartido.NumeroMatricula;
import java.time.LocalDateTime;

/** EVENTO: se registro la nota final de un alumno en una evaluacion. Es inmutable (un hecho no cambia). */
public record NotaRegistrada(String idEvaluacion, NumeroMatricula matricula, Nota notaFinal,
                             LocalDateTime ocurridoEn) implements EventoDeDominio {

    public NotaRegistrada(String idEvaluacion, NumeroMatricula matricula, Nota notaFinal) {
        this(idEvaluacion, matricula, notaFinal, LocalDateTime.now());
    }
}
