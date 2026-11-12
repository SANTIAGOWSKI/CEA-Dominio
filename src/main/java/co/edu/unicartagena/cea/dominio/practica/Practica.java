package co.edu.unicartagena.cea.dominio.practica;

import co.edu.unicartagena.cea.dominio.compartido.Dni;
import co.edu.unicartagena.cea.dominio.compartido.Nota;
import co.edu.unicartagena.cea.dominio.compartido.NivelDificultad;
import co.edu.unicartagena.cea.dominio.compartido.NumeroMatricula;
import co.edu.unicartagena.cea.dominio.compartido.PeriodoAcademico;
import co.edu.unicartagena.cea.dominio.compartido.Validar;
import co.edu.unicartagena.cea.dominio.evento.EventoDeDominio;
import co.edu.unicartagena.cea.dominio.evento.PracticaPresentada;
import co.edu.unicartagena.cea.dominio.excepcion.ExcepcionDeDominio;
import co.edu.unicartagena.cea.dominio.excepcion.PracticaSinProfesorException;
import co.edu.unicartagena.cea.dominio.excepcion.ResultadoDuplicadoException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * AGREGADO (raiz): una practica. Identidad: su codigo.
 *
 * Invariantes:
 *  - siempre tiene al menos un profesor disenador (pueden ser varios, sin repetirse);
 *  - un alumno solo tiene una participacion por practica.
 */
public class Practica {

    private final String codigo;
    private final String titulo;
    private final NivelDificultad nivel;
    private final ModalidadPractica modalidad;
    private final PeriodoAcademico periodo;
    private final List<DisenoDePractica> disenos = new ArrayList<>();
    private final Map<NumeroMatricula, Participacion> participaciones = new LinkedHashMap<>();
    private final List<EventoDeDominio> eventos = new ArrayList<>();

    public Practica(String codigo, String titulo, NivelDificultad nivel, ModalidadPractica modalidad,
                    PeriodoAcademico periodo, List<DisenoDePractica> disenosIniciales) {
        this.codigo = Validar.noVacio(codigo, "El codigo de la practica");
        this.titulo = Validar.noVacio(titulo, "El titulo de la practica");
        this.nivel = Validar.noNulo(nivel, "El nivel de dificultad");
        this.modalidad = Validar.noNulo(modalidad, "La modalidad");
        this.periodo = Validar.noNulo(periodo, "El periodo academico");
        Validar.noNulo(disenosIniciales, "Los disenadores");
        if (disenosIniciales.isEmpty()) {
            throw new PracticaSinProfesorException("La practica " + codigo + " necesita al menos un profesor disenador");
        }
        disenosIniciales.forEach(this::agregarDiseno);
    }

    public void agregarDiseno(DisenoDePractica diseno) {
        Validar.noNulo(diseno, "El diseno");
        if (disenadaPor(diseno.profesor())) {
            throw new ExcepcionDeDominio(
                    "El profesor " + diseno.profesor().valor() + " ya disenio la practica " + codigo);
        }
        disenos.add(diseno);
    }

    public Participacion registrarParticipacion(NumeroMatricula matricula, LocalDate fecha, Nota nota) {
        Participacion participacion = new Participacion(matricula, fecha, nota);
        if (participaciones.containsKey(matricula)) {
            throw new ResultadoDuplicadoException(
                    "El alumno " + matricula.valor() + " ya presento la practica " + codigo);
        }
        participaciones.put(matricula, participacion);
        eventos.add(new PracticaPresentada(codigo, matricula, fecha, nota));
        return participacion;
    }

    public boolean disenadaPor(Dni profesor) {
        return disenos.stream().anyMatch(d -> d.profesor().equals(profesor));
    }

    /** Verdadero si ese profesor la disenio en una fecha dentro de [desde, hasta]. */
    public boolean disenadaPorEntre(Dni profesor, LocalDate desde, LocalDate hasta) {
        return disenos.stream().anyMatch(d -> d.profesor().equals(profesor)
                && !d.fecha().isBefore(desde) && !d.fecha().isAfter(hasta));
    }

    public boolean esColaborativa() {
        return disenos.size() > 1;
    }

    public Set<Dni> profesoresDisenadores() {
        Set<Dni> profesores = new LinkedHashSet<>();
        disenos.forEach(d -> profesores.add(d.profesor()));
        return profesores;
    }

    public List<EventoDeDominio> extraerEventos() {
        List<EventoDeDominio> copia = List.copyOf(eventos);
        eventos.clear();
        return copia;
    }

    public String codigo() { return codigo; }
    public String titulo() { return titulo; }
    public NivelDificultad nivel() { return nivel; }
    public ModalidadPractica modalidad() { return modalidad; }
    public PeriodoAcademico periodo() { return periodo; }
    public List<DisenoDePractica> disenos() { return Collections.unmodifiableList(disenos); }
    public Map<NumeroMatricula, Participacion> participaciones() { return Collections.unmodifiableMap(participaciones); }

    @Override
    public boolean equals(Object otro) {
        return otro instanceof Practica p && codigo.equals(p.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
