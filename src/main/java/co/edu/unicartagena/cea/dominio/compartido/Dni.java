package co.edu.unicartagena.cea.dominio.compartido;

import co.edu.unicartagena.cea.dominio.excepcion.ExcepcionDeDominio;

/** Value Object: documento de identidad del profesor (5 a 15 letras o numeros). */
public record Dni(String valor) {

    public Dni {
        valor = Validar.noVacio(valor, "El DNI");
        if (!valor.matches("[A-Za-z0-9]{5,15}")) {
            throw new ExcepcionDeDominio("El DNI debe tener entre 5 y 15 letras o numeros: " + valor);
        }
    }
}
