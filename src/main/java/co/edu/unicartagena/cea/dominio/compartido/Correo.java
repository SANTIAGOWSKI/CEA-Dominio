package co.edu.unicartagena.cea.dominio.compartido;

import co.edu.unicartagena.cea.dominio.excepcion.ExcepcionDeDominio;
import java.util.regex.Pattern;

/** Value Object: correo electronico valido, siempre guardado en minusculas. */
public record Correo(String valor) {

    private static final Pattern PATRON = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");

    public Correo {
        valor = Validar.noVacio(valor, "El correo").toLowerCase();
        if (!PATRON.matcher(valor).matches()) {
            throw new ExcepcionDeDominio("El correo no tiene un formato valido: " + valor);
        }
    }
}
