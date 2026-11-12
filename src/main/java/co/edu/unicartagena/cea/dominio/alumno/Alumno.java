package co.edu.unicartagena.cea.dominio.alumno;

import co.edu.unicartagena.cea.dominio.compartido.GrupoAcademico;
import co.edu.unicartagena.cea.dominio.compartido.NumeroMatricula;
import co.edu.unicartagena.cea.dominio.compartido.Validar;
import java.util.Objects;

/**
 * ENTIDAD: un alumno. Su identidad es el numero de matricula: dos alumnos con la misma
 * matricula son la misma persona aunque cambie su nombre o su grupo.
 */
public class Alumno {

    private final NumeroMatricula matricula;
    private String nombre;
    private GrupoAcademico grupo;

    public Alumno(NumeroMatricula matricula, String nombre, GrupoAcademico grupo) {
        this.matricula = Validar.noNulo(matricula, "La matricula");
        this.nombre = Validar.noVacio(nombre, "El nombre del alumno");
        this.grupo = Validar.noNulo(grupo, "El grupo");
    }

    public void cambiarDeGrupo(GrupoAcademico nuevoGrupo) {
        this.grupo = Validar.noNulo(nuevoGrupo, "El grupo");
    }

    public NumeroMatricula matricula() { return matricula; }
    public String nombre() { return nombre; }
    public GrupoAcademico grupo() { return grupo; }

    @Override
    public boolean equals(Object otro) {
        return otro instanceof Alumno a && matricula.equals(a.matricula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matricula);
    }
}
