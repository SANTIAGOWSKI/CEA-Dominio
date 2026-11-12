package co.edu.unicartagena.cea.dominio.asignatura;

import co.edu.unicartagena.cea.dominio.compartido.Validar;
import co.edu.unicartagena.cea.dominio.excepcion.ExcepcionDeDominio;
import java.time.DayOfWeek;
import java.time.LocalTime;

/** VALUE OBJECT: un bloque de clase, por ejemplo lunes de 8:00 a 10:00. */
public record FranjaHoraria(DayOfWeek dia, LocalTime inicio, LocalTime fin) {

    public FranjaHoraria {
        Validar.noNulo(dia, "El dia");
        Validar.noNulo(inicio, "La hora de inicio");
        Validar.noNulo(fin, "La hora de fin");
        if (!fin.isAfter(inicio)) {
            throw new ExcepcionDeDominio("La hora de fin debe ser posterior a la de inicio");
        }
    }
}
