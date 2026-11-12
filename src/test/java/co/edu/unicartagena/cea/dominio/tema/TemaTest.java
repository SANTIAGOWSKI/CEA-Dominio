package co.edu.unicartagena.cea.dominio.tema;

import static org.junit.jupiter.api.Assertions.*;

import co.edu.unicartagena.cea.dominio.compartido.NivelDificultad;
import co.edu.unicartagena.cea.dominio.excepcion.ExcepcionDeDominio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TemaTest {

    @Test
    @DisplayName("Un tema puede tener recursos asociados (opcionales)")
    void recursosOpcionales() {
        Tema tema = new Tema("T1", "Recursion", "Funciones que se llaman a si mismas", NivelDificultad.MEDIO);
        assertTrue(tema.recursos().isEmpty());
        tema.agregarRecurso("https://ejemplo.edu/recursion.pdf");
        assertEquals(1, tema.recursos().size());
    }

    @Test
    @DisplayName("El tema exige id, nombre, descripcion y nivel")
    void camposObligatorios() {
        assertThrows(ExcepcionDeDominio.class, () -> new Tema("", "N", "D", NivelDificultad.BAJO));
        assertThrows(ExcepcionDeDominio.class, () -> new Tema("T1", "N", "D", null));
    }
}
