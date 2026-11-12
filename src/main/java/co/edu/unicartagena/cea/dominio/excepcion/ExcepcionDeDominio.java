package co.edu.unicartagena.cea.dominio.excepcion;

/**
 * Excepcion base de TODAS las reglas de negocio del CEA.
 * Es distinta de una excepcion tecnica (SQL, red, archivo): esta significa
 * "el negocio no permite esto", y se expresa con palabras del dominio.
 */
public class ExcepcionDeDominio extends RuntimeException {
    public ExcepcionDeDominio(String mensaje) {
        super(mensaje);
    }
}
