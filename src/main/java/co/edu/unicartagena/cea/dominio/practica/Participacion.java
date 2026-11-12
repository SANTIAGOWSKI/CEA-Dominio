package co.edu.unicartagena.cea.dominio.practica;

import co.edu.unicartagena.cea.dominio.compartido.Nota;
import co.edu.unicartagena.cea.dominio.compartido.NumeroMatricula;
import co.edu.unicartagena.cea.dominio.compartido.Validar;
import java.time.LocalDate;

/** VALUE OBJECT: un alumno se presento a la practica en cierta fecha y obtuvo una nota. */
public record Participacion(NumeroMatricula matricula, LocalDate fecha, Nota nota) {

    public Participacion {
        Validar.noNulo(matricula, "La matricula");
        Validar.noNulo(fecha, "La fecha de la practica");
        Validar.noNulo(nota, "La nota");
    }
}
