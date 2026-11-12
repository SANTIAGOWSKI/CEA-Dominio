package co.edu.unicartagena.cea.dominio.excepcion;

/** Se lanza cuando un alumno ya tiene resultado en esa evaluacion o practica. */
public class ResultadoDuplicadoException extends ExcepcionDeDominio {
    public ResultadoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
