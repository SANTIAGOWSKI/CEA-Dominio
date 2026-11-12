package co.edu.unicartagena.cea.dominio.evaluacion;

import co.edu.unicartagena.cea.dominio.compartido.Nota;
import co.edu.unicartagena.cea.dominio.compartido.Porcentaje;
import co.edu.unicartagena.cea.dominio.excepcion.PonderacionInvalidaException;

/**
 * VALUE OBJECT + STRATEGY: componente teorico y conceptual (80%) + redaccion y ortografia (20%).
 * Invariante: los dos pesos suman exactamente 100.
 */
public record PonderacionControlEscrito(Porcentaje teoricoConceptual, Porcentaje redaccionOrtografia)
        implements PoliticaDeCalificacion {

    public PonderacionControlEscrito {
        if (teoricoConceptual.valor() + redaccionOrtografia.valor() != 100) {
            throw new PonderacionInvalidaException("Los pesos de un control escrito deben sumar 100");
        }
    }

    public static PonderacionControlEscrito porDefecto() {
        return new PonderacionControlEscrito(Porcentaje.de(80), Porcentaje.de(20));
    }

    @Override
    public Nota calcular(Nota teorico, Nota redaccion) {
        double final_ = teorico.valor() * teoricoConceptual.fraccion()
                + redaccion.valor() * redaccionOrtografia.fraccion();
        return Nota.de(Math.round(final_ * 100.0) / 100.0);
    }

    @Override
    public int pesoTotal() {
        return teoricoConceptual.valor() + redaccionOrtografia.valor();
    }
}
