package co.edu.unicartagena.cea.dominio.evaluacion;

import co.edu.unicartagena.cea.dominio.compartido.Nota;

/**
 * Patron STRATEGY: cada tipo de evaluacion calcula su nota final de forma distinta.
 * Agregar una evaluacion oral nueva = agregar otra implementacion, sin tocar las existentes.
 */
public interface PoliticaDeCalificacion {

    /** Combina la nota del componente principal y la del complementario en la nota final. */
    Nota calcular(Nota componentePrincipal, Nota componenteComplementario);

    /** Suma de los pesos de los componentes (siempre 100 por invariante). */
    int pesoTotal();
}
