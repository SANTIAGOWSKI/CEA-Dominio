package co.edu.unicartagena.cea.dominio.compartido;

import co.edu.unicartagena.cea.dominio.excepcion.ExcepcionDeDominio;

/** Validaciones reutilizables para que los objetos del dominio nunca nazcan en estado invalido. */
public final class Validar {

    private Validar() {
    }

    public static <T> T noNulo(T valor, String campo) {
        if (valor == null) {
            throw new ExcepcionDeDominio(campo + " es obligatorio");
        }
        return valor;
    }

    public static String noVacio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new ExcepcionDeDominio(campo + " no puede estar vacio");
        }
        return valor.trim();
    }

    public static int positivo(int valor, String campo) {
        if (valor <= 0) {
            throw new ExcepcionDeDominio(campo + " debe ser mayor que cero");
        }
        return valor;
    }
}
