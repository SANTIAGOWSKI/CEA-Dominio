package co.edu.unicartagena.cea.dominio.evaluacion;

import co.edu.unicartagena.cea.dominio.compartido.Nota;
import co.edu.unicartagena.cea.dominio.compartido.Porcentaje;
import co.edu.unicartagena.cea.dominio.excepcion.PonderacionInvalidaException;

/**
 * VALUE OBJECT + STRATEGY: laboratorio (70%) + eficiencia y tiempo (30%).
 * Invariante: los dos pesos suman exactamente 100.
 */
public record PonderacionExamenPractico(Porcentaje laboratorio, Porcentaje eficienciaYTiempo)
        implements PoliticaDeCalificacion {

    public PonderacionExamenPractico {
        if (laboratorio.valor() + eficienciaYTiempo.valor() != 100) {
            throw new PonderacionInvalidaException("Los pesos de un examen practico deben sumar 100");
        }
    }

    public static PonderacionExamenPractico porDefecto() {
        return new PonderacionExamenPractico(Porcentaje.de(70), Porcentaje.de(30));
    }

    @Override
    public Nota calcular(Nota notaLaboratorio, Nota notaEficiencia) {
        double final_ = notaLaboratorio.valor() * laboratorio.fraccion()
                + notaEficiencia.valor() * eficienciaYTiempo.fraccion();
        return Nota.de(Math.round(final_ * 100.0) / 100.0);
    }

    @Override
    public int pesoTotal() {
        return laboratorio.valor() + eficienciaYTiempo.valor();
    }
}
