package co.edu.unicartagena.cea.dominio.compartido;

import co.edu.unicartagena.cea.dominio.excepcion.ExcepcionDeDominio;

/** Value Object: ano academico + semestre (1 o 2). */
public record PeriodoAcademico(int anio, int semestre) {

    public PeriodoAcademico {
        if (anio < 1900) {
            throw new ExcepcionDeDominio("El ano academico no es valido: " + anio);
        }
        if (semestre != 1 && semestre != 2) {
            throw new ExcepcionDeDominio("El semestre debe ser 1 o 2 pero fue " + semestre);
        }
    }

    public static PeriodoAcademico de(int anio, int semestre) {
        return new PeriodoAcademico(anio, semestre);
    }
}
