package co.edu.unicartagena.cea.dominio.compartido;

import co.edu.unicartagena.cea.dominio.excepcion.NotaInvalidaException;

/**
 * Value Object: una nota dentro de la escala 1.0 a 7.0 (escala de la Universidad de Chile).
 * Si tu profesor pide otra escala (por ejemplo 0.0 a 5.0) solo cambias MINIMA y MAXIMA.
 */
public record Nota(double valor) implements Comparable<Nota> {

    public static final double MINIMA = 1.0;
    public static final double MAXIMA = 7.0;

    public Nota {
        if (Double.isNaN(valor) || valor < MINIMA || valor > MAXIMA) {
            throw new NotaInvalidaException(
                    "La nota debe estar entre " + MINIMA + " y " + MAXIMA + " pero fue " + valor);
        }
    }

    public static Nota de(double valor) {
        return new Nota(valor);
    }

    public boolean esMayorQue(Nota otra) {
        return this.valor > otra.valor;
    }

    @Override
    public int compareTo(Nota otra) {
        return Double.compare(this.valor, otra.valor);
    }
}
