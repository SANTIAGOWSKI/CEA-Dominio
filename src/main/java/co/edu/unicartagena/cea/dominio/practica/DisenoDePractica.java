package co.edu.unicartagena.cea.dominio.practica;

import co.edu.unicartagena.cea.dominio.compartido.Dni;
import co.edu.unicartagena.cea.dominio.compartido.Validar;
import java.time.LocalDate;

/** VALUE OBJECT: que profesor disenio la practica y en que fecha. */
public record DisenoDePractica(Dni profesor, LocalDate fecha) {

    public DisenoDePractica {
        Validar.noNulo(profesor, "El profesor disenador");
        Validar.noNulo(fecha, "La fecha de diseno");
    }
}
