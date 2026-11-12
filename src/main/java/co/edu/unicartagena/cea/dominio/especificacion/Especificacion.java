package co.edu.unicartagena.cea.dominio.especificacion;

/**
 * Patron SPECIFICATION: encapsula UNA regla de negocio que responde si/no sobre un objeto.
 * Se pueden combinar con y(...), o(...), no() sin escribir ifs gigantes.
 */
@FunctionalInterface
public interface Especificacion<T> {

    boolean esSatisfechaPor(T candidato);

    default Especificacion<T> y(Especificacion<T> otra) {
        return c -> this.esSatisfechaPor(c) && otra.esSatisfechaPor(c);
    }

    default Especificacion<T> o(Especificacion<T> otra) {
        return c -> this.esSatisfechaPor(c) || otra.esSatisfechaPor(c);
    }

    default Especificacion<T> no() {
        return c -> !this.esSatisfechaPor(c);
    }
}
