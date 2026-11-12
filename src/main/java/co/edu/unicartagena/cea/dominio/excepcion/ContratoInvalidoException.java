package co.edu.unicartagena.cea.dominio.excepcion;

/** Se lanza cuando un contrato tiene fechas incoherentes o se solapa con otro. */
public class ContratoInvalidoException extends ExcepcionDeDominio {
    public ContratoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
