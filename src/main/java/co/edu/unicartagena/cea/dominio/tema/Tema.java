package co.edu.unicartagena.cea.dominio.tema;

import co.edu.unicartagena.cea.dominio.compartido.NivelDificultad;
import co.edu.unicartagena.cea.dominio.compartido.Validar;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * ENTIDAD (raiz de su propio agregado): un tema de estudio, identificado por su ID.
 * Una asignatura lo referencia solo por su ID, asi un mismo tema puede estar en varias asignaturas.
 */
public class Tema {

    private final String id;
    private String nombre;
    private String descripcion;
    private NivelDificultad nivel;
    private final List<String> recursos = new ArrayList<>(); // opcional

    public Tema(String id, String nombre, String descripcion, NivelDificultad nivel) {
        this.id = Validar.noVacio(id, "El ID del tema");
        this.nombre = Validar.noVacio(nombre, "El nombre del tema");
        this.descripcion = Validar.noVacio(descripcion, "La descripcion del tema");
        this.nivel = Validar.noNulo(nivel, "El nivel de dificultad");
    }

    public void agregarRecurso(String recurso) {
        recursos.add(Validar.noVacio(recurso, "El recurso"));
    }

    public String id() { return id; }
    public String nombre() { return nombre; }
    public String descripcion() { return descripcion; }
    public NivelDificultad nivel() { return nivel; }
    public List<String> recursos() { return Collections.unmodifiableList(recursos); }

    @Override
    public boolean equals(Object otro) {
        return otro instanceof Tema t && id.equals(t.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
