package co.edu.unicartagena.cea.dominio.evaluacion;

import co.edu.unicartagena.cea.dominio.compartido.Nota;
import co.edu.unicartagena.cea.dominio.compartido.NumeroMatricula;
import co.edu.unicartagena.cea.dominio.compartido.PeriodoAcademico;
import co.edu.unicartagena.cea.dominio.compartido.Validar;
import co.edu.unicartagena.cea.dominio.evento.EventoDeDominio;
import co.edu.unicartagena.cea.dominio.evento.NotaRegistrada;
import co.edu.unicartagena.cea.dominio.excepcion.ExcepcionDeDominio;
import co.edu.unicartagena.cea.dominio.excepcion.ResultadoDuplicadoException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * AGREGADO (raiz abstracta): una evaluacion. Identidad: su id.
 *
 * Invariantes que protege:
 *  - pertenece a UNA sola asignatura (un solo codigo);
 *  - abarca al menos un tema;
 *  - la fecha es unica y comun para todos los alumnos;
 *  - un alumno solo puede tener un resultado por evaluacion.
 */
public abstract class Evaluacion {

    private final String id;
    private final TipoEvaluacion tipo;
    private final LocalDate fecha;
    private final String codigoAsignatura;
    private final PeriodoAcademico periodo;
    private final Set<String> temas = new LinkedHashSet<>();
    private final Map<NumeroMatricula, ResultadoEvaluacion> resultados = new LinkedHashMap<>();
    private final List<EventoDeDominio> eventos = new ArrayList<>();

    protected Evaluacion(String id, TipoEvaluacion tipo, LocalDate fecha, String codigoAsignatura,
                         PeriodoAcademico periodo, Set<String> temas) {
        this.id = Validar.noVacio(id, "El ID de la evaluacion");
        this.tipo = Validar.noNulo(tipo, "El tipo de evaluacion");
        this.fecha = Validar.noNulo(fecha, "La fecha de la evaluacion");
        this.codigoAsignatura = Validar.noVacio(codigoAsignatura, "La asignatura de la evaluacion");
        this.periodo = Validar.noNulo(periodo, "El periodo academico");
        Validar.noNulo(temas, "Los temas");
        if (temas.isEmpty()) {
            throw new ExcepcionDeDominio("Una evaluacion debe abarcar al menos un tema");
        }
        this.temas.addAll(temas);
    }

    /** La politica con la que se calcula la nota final (la define cada subclase). */
    protected abstract PoliticaDeCalificacion politica();

    /** Registra el resultado de un alumno; la nota final la calcula el dominio, no quien llama. */
    public ResultadoEvaluacion registrarResultado(NumeroMatricula matricula, Nota notaComponentePrincipal,
                                                  Nota notaComponenteComplementario) {
        Validar.noNulo(matricula, "La matricula");
        Validar.noNulo(notaComponentePrincipal, "La nota del componente principal");
        Validar.noNulo(notaComponenteComplementario, "La nota del componente complementario");
        if (resultados.containsKey(matricula)) {
            throw new ResultadoDuplicadoException(
                    "El alumno " + matricula.valor() + " ya tiene resultado en la evaluacion " + id);
        }
        Nota notaFinal = politica().calcular(notaComponentePrincipal, notaComponenteComplementario);
        ResultadoEvaluacion resultado = new ResultadoEvaluacion(
                matricula, notaComponentePrincipal, notaComponenteComplementario, notaFinal);
        resultados.put(matricula, resultado);
        eventos.add(new NotaRegistrada(id, matricula, notaFinal));
        return resultado;
    }

    public Optional<Nota> notaDe(NumeroMatricula matricula) {
        return Optional.ofNullable(resultados.get(matricula)).map(ResultadoEvaluacion::notaFinal);
    }

    public int ponderacionTotal() {
        return politica().pesoTotal();
    }

    public boolean abarcaTema(String idTema) {
        return temas.contains(idTema);
    }

    /** Entrega los eventos pendientes y los limpia, para que quien los publique no los repita. */
    public List<EventoDeDominio> extraerEventos() {
        List<EventoDeDominio> copia = List.copyOf(eventos);
        eventos.clear();
        return copia;
    }

    public String id() { return id; }
    public TipoEvaluacion tipo() { return tipo; }
    public LocalDate fecha() { return fecha; }
    public String codigoAsignatura() { return codigoAsignatura; }
    public PeriodoAcademico periodo() { return periodo; }
    public Set<String> temas() { return Collections.unmodifiableSet(temas); }
    public Map<NumeroMatricula, ResultadoEvaluacion> resultados() { return Collections.unmodifiableMap(resultados); }

    @Override
    public boolean equals(Object otro) {
        return otro instanceof Evaluacion e && id.equals(e.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
