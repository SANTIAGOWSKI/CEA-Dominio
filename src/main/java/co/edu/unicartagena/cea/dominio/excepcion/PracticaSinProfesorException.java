package co.edu.unicartagena.cea.dominio.excepcion;

/** Se lanza cuando una practica quedaria sin ningun profesor disenador. */
public class PracticaSinProfesorException extends ExcepcionDeDominio {
    public PracticaSinProfesorException(String mensaje) {
        super(mensaje);
    }
}
