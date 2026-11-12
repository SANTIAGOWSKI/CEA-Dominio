package co.edu.unicartagena.cea.dominio.asignatura;

import co.edu.unicartagena.cea.dominio.compartido.Dni;
import co.edu.unicartagena.cea.dominio.compartido.PeriodoAcademico;
import co.edu.unicartagena.cea.dominio.compartido.Validar;
import co.edu.unicartagena.cea.dominio.excepcion.ExcepcionDeDominio;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * AGREGADO (raiz): una asignatura. Identidad: su codigo.
 * Referencia a otros agregados SOLO por identificador (temas por ID, profesores por DNI).
 */
public class Asignatura {

    private final String codigo;
    private String nombre;
    private String descripcion;
    private final int creditos;
    private final PeriodoAcademico periodo;
    private final List<FranjaHoraria> horario;
    private final Set<String> temas = new LinkedHashSet<>();
    private final Set<Dni> profesores = new LinkedHashSet<>();

    public Asignatura(String codigo, String nombre, String descripcion, int creditos,
                      PeriodoAcademico periodo, List<FranjaHoraria> horario) {
        this.codigo = Validar.noVacio(codigo, "El codigo de la asignatura");
        this.nombre = Validar.noVacio(nombre, "El nombre de la asignatura");
        this.descripcion = Validar.noVacio(descripcion, "La descripcion de la asignatura");
        this.creditos = Validar.positivo(creditos, "Los creditos academicos");
        this.periodo = Validar.noNulo(periodo, "El periodo academico");
        Validar.noNulo(horario, "El horario de clases");
        if (horario.isEmpty()) {
            throw new ExcepcionDeDominio("La asignatura debe tener al menos una franja de horario");
        }
        this.horario = new ArrayList<>(horario);
    }

    public void asociarTema(String idTema) {
        Validar.noVacio(idTema, "El ID del tema");
        if (!temas.add(idTema)) {
            throw new ExcepcionDeDominio("El tema " + idTema + " ya esta asociado a la asignatura " + codigo);
        }
    }

    public void asignarProfesor(Dni dni) {
        Validar.noNulo(dni, "El DNI del profesor");
        if (!profesores.add(dni)) {
            throw new ExcepcionDeDominio("El profesor ya imparte la asignatura " + codigo);
        }
    }

    public boolean contieneTema(String idTema) { return temas.contains(idTema); }
    public boolean imparteProfesor(Dni dni) { return profesores.contains(dni); }

    public String codigo() { return codigo; }
    public String nombre() { return nombre; }
    public String descripcion() { return descripcion; }
    public int creditos() { return creditos; }
    public PeriodoAcademico periodo() { return periodo; }
    public List<FranjaHoraria> horario() { return Collections.unmodifiableList(horario); }
    public Set<String> temas() { return Collections.unmodifiableSet(temas); }
    public Set<Dni> profesores() { return Collections.unmodifiableSet(profesores); }

    @Override
    public boolean equals(Object otro) {
        return otro instanceof Asignatura a && codigo.equals(a.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
