package co.edu.unicartagena.cea.dominio.especificacion;

import co.edu.unicartagena.cea.dominio.profesor.Profesor;
import java.time.LocalDate;

/** Regla: el profesor tiene contrato activo en un departamento en una fecha dada. */
public record ProfesorActivoEnDepartamento(String departamento, LocalDate fecha)
        implements Especificacion<Profesor> {

    @Override
    public boolean esSatisfechaPor(Profesor profesor) {
        return profesor.tieneContratoActivoEnDepartamento(departamento, fecha);
    }
}
