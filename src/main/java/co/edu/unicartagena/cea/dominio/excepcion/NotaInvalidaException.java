package co.edu.unicartagena.cea.dominio.excepcion;

/** Se lanza cuando una nota esta fuera de la escala permitida. */
public class NotaInvalidaException extends ExcepcionDeDominio {
    public NotaInvalidaException(String mensaje) {
        super(mensaje);
    }
}
