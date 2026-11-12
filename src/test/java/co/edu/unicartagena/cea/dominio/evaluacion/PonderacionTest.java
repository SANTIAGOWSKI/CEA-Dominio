package co.edu.unicartagena.cea.dominio.evaluacion;

import static org.junit.jupiter.api.Assertions.*;

import co.edu.unicartagena.cea.dominio.compartido.Nota;
import co.edu.unicartagena.cea.dominio.compartido.Porcentaje;
import co.edu.unicartagena.cea.dominio.excepcion.PonderacionInvalidaException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PonderacionTest {

    @Test
    @DisplayName("Control escrito: 80% teorico + 20% redaccion")
    void calculoControlEscrito() {
        Nota nota = PonderacionControlEscrito.porDefecto().calcular(Nota.de(6.0), Nota.de(5.0));
        assertEquals(5.8, nota.valor(), 1e-9);
    }

    @Test
    @DisplayName("Examen practico: 70% laboratorio + 30% eficiencia y tiempo")
    void calculoExamenPractico() {
        Nota nota = PonderacionExamenPractico.porDefecto().calcular(Nota.de(7.0), Nota.de(4.0));
        assertEquals(6.1, nota.valor(), 1e-9);
    }

    @Test
    @DisplayName("Los valores por defecto cumplen 80/20 y 70/30")
    void valoresPorDefecto() {
        assertEquals(80, PonderacionControlEscrito.porDefecto().teoricoConceptual().valor());
        assertEquals(20, PonderacionControlEscrito.porDefecto().redaccionOrtografia().valor());
        assertEquals(70, PonderacionExamenPractico.porDefecto().laboratorio().valor());
        assertEquals(30, PonderacionExamenPractico.porDefecto().eficienciaYTiempo().valor());
    }

    @Test
    @DisplayName("Invariante: los pesos de un control escrito deben sumar 100")
    void controlEscritoNoSuma100() {
        assertThrows(PonderacionInvalidaException.class,
                () -> new PonderacionControlEscrito(Porcentaje.de(80), Porcentaje.de(30)));
    }

    @Test
    @DisplayName("Invariante: los pesos de un examen practico deben sumar 100")
    void examenPracticoNoSuma100() {
        assertThrows(PonderacionInvalidaException.class,
                () -> new PonderacionExamenPractico(Porcentaje.de(60), Porcentaje.de(30)));
    }

    @Test
    @DisplayName("Se pueden definir otras ponderaciones validas, por ejemplo 50/50")
    void otraPonderacionValida() {
        PonderacionControlEscrito mitad = new PonderacionControlEscrito(Porcentaje.de(50), Porcentaje.de(50));
        assertEquals(5.0, mitad.calcular(Nota.de(6.0), Nota.de(4.0)).valor(), 1e-9);
        assertEquals(100, mitad.pesoTotal());
    }
}
