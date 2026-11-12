package co.edu.unicartagena.cea.dominio.compartido;

/** Value Object: numero de matricula del alumno (su identidad). */
public record NumeroMatricula(String valor) {

    public NumeroMatricula {
        valor = Validar.noVacio(valor, "El numero de matricula");
    }
}
