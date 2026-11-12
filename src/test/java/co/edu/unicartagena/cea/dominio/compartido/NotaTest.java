package co.edu.unicartagena.cea.dominio.compartido;

import static org.junit.jupiter.api.Assertions.*;

import co.edu.unicartagena.cea.dominio.excepcion.NotaInvalidaException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NotaTest {

    @Test
    @DisplayName("Acepta los limites de la escala 1.0 y 7.0")
    void aceptaLimites() {
        assertEquals(1.0, Nota.de(1.0).valor());
        assertEquals(7.0, Nota.de(7.0).valor());
    }

    @Test
    @DisplayName("Rechaza notas por debajo del minimo")
    void rechazaNotaMenorAlMinimo() {
        assertThrows(NotaInvalidaException.class, () -> Nota.de(0.9));
    }

    @Test
    @DisplayName("Rechaza notas por encima del maximo")
    void rechazaNotaMayorAlMaximo() {
        assertThrows(NotaInvalidaException.class, () -> Nota.de(7.1));
    }

    @Test
    @DisplayName("Rechaza NaN")
    void rechazaNaN() {
        assertThrows(NotaInvalidaException.class, () -> Nota.de(Double.NaN));
    }

    @Test
    @DisplayName("Dos notas con el mismo valor son iguales (igualdad por valor)")
    void igualdadPorValor() {
        assertEquals(Nota.de(5.5), Nota.de(5.5));
        assertTrue(Nota.de(6.0).esMayorQue(Nota.de(5.0)));
        assertFalse(Nota.de(5.0).esMayorQue(Nota.de(5.0)));
    }
}
