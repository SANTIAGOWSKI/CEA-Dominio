package co.edu.unicartagena.cea.dominio.compartido;

import co.edu.unicartagena.cea.dominio.excepcion.PonderacionInvalidaException;

/** Value Object: un peso entre 0 y 100. Se usa entero para evitar errores de redondeo al sumar. */
public record Porcentaje(int valor) {

    public Porcentaje {
        if (valor < 0 || valor > 100) {
            throw new PonderacionInvalidaException("Un porcentaje debe estar entre 0 y 100 pero fue " + valor);
        }
    }

    public static Porcentaje de(int valor) {
        return new Porcentaje(valor);
    }

    /** 80% -> 0.8 */
    public double fraccion() {
        return valor / 100.0;
    }
}
