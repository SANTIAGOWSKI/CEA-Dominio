package co.edu.unicartagena.cea.dominio.servicio;

import co.edu.unicartagena.cea.dominio.compartido.Nota;
import co.edu.unicartagena.cea.dominio.compartido.NumeroMatricula;
import co.edu.unicartagena.cea.dominio.evaluacion.ControlEscrito;
import co.edu.unicartagena.cea.dominio.practica.ModalidadPractica;
import co.edu.unicartagena.cea.dominio.practica.Participacion;
import co.edu.unicartagena.cea.dominio.practica.Practica;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * SERVICIO DE DOMINIO: reglas que involucran VARIOS agregados (muchos controles, muchas practicas)
 * y que no pertenecen a ninguno en particular. No guarda estado.
 */
public class ServicioDeDesempenoAcademico {

    /** Promedio de un conjunto de notas, redondeado a 2 decimales. Vacio si no hay notas. */
    public Optional<Nota> promedio(Collection<Nota> notas) {
        if (notas.isEmpty()) {
            return Optional.empty();
        }
        double suma = notas.stream().mapToDouble(Nota::valor).sum();
        return Optional.of(Nota.de(Math.round(suma / notas.size() * 100.0) / 100.0));
    }

    /** Alumnos con nota SUPERIOR al umbral en TODOS los controles escritos dados. */
    public Set<NumeroMatricula> alumnosConNotaSuperiorEnTodos(List<ControlEscrito> controles, Nota umbral) {
        Set<NumeroMatricula> resultado = new LinkedHashSet<>();
        if (controles.isEmpty()) {
            return resultado;
        }
        for (NumeroMatricula matricula : controles.get(0).resultados().keySet()) {
            boolean superaTodos = controles.stream()
                    .allMatch(c -> c.notaDe(matricula).filter(n -> n.esMayorQue(umbral)).isPresent());
            if (superaTodos) {
                resultado.add(matricula);
            }
        }
        return resultado;
    }

    /** Alumnos con nota superior al PROMEDIO de cada control, en todos los controles dados. */
    public Set<NumeroMatricula> alumnosSobreElPromedioEnTodos(List<ControlEscrito> controles) {
        Set<NumeroMatricula> resultado = new LinkedHashSet<>();
        if (controles.isEmpty()) {
            return resultado;
        }
        for (NumeroMatricula matricula : controles.get(0).resultados().keySet()) {
            boolean superaElPromedioEnTodos = controles.stream().allMatch(c -> {
                Optional<Nota> promedio = promedio(c.resultados().values().stream()
                        .map(r -> r.notaFinal()).toList());
                return c.notaDe(matricula).isPresent() && promedio.isPresent()
                        && c.notaDe(matricula).get().esMayorQue(promedio.get());
            });
            if (superaElPromedioEnTodos) {
                resultado.add(matricula);
            }
        }
        return resultado;
    }

    /** Promedio de un grupo (sus matriculas) en las practicas INDIVIDUALES dadas. */
    public Optional<Nota> promedioDelGrupoEnPracticasIndividuales(List<Practica> practicas,
                                                                  Set<NumeroMatricula> miembros) {
        List<Nota> notas = practicas.stream()
                .filter(p -> p.modalidad() == ModalidadPractica.INDIVIDUAL)
                .flatMap(p -> p.participaciones().values().stream())
                .filter(part -> miembros.contains(part.matricula()))
                .map(Participacion::nota)
                .toList();
        return promedio(notas);
    }

    /** Alumnos que presentaron MAS de una practica en grupo. */
    public Set<NumeroMatricula> alumnosConMasDeUnaPracticaGrupal(List<Practica> practicas) {
        Map<NumeroMatricula, Integer> conteo = new HashMap<>();
        practicas.stream()
                .filter(p -> p.modalidad() == ModalidadPractica.GRUPAL)
                .forEach(p -> p.participaciones().keySet().forEach(m -> conteo.merge(m, 1, Integer::sum)));
        Set<NumeroMatricula> resultado = new LinkedHashSet<>();
        conteo.forEach((matricula, veces) -> {
            if (veces > 1) {
                resultado.add(matricula);
            }
        });
        return resultado;
    }
}
