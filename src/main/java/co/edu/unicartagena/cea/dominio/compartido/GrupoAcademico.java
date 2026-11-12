package co.edu.unicartagena.cea.dominio.compartido;

/** Value Object: grupo al que asiste un alumno (por ejemplo "G1"). */
public record GrupoAcademico(String codigo) {

    public GrupoAcademico {
        codigo = Validar.noVacio(codigo, "El grupo");
    }
}
