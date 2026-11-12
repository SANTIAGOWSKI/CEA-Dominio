package co.edu.unicartagena.cea.dominio.excepcion;

/** Se lanza cuando los pesos de los componentes no suman 100%. */
public class PonderacionInvalidaException extends ExcepcionDeDominio {
    public PonderacionInvalidaException(String mensaje) {
        super(mensaje);
    }
}
