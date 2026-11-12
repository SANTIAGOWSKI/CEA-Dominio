package co.edu.unicartagena.cea.dominio.profesor;

import static org.junit.jupiter.api.Assertions.*;

import co.edu.unicartagena.cea.dominio.compartido.Correo;
import co.edu.unicartagena.cea.dominio.compartido.Dni;
import co.edu.unicartagena.cea.dominio.excepcion.ContratoInvalidoException;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProfesorTest {

    private Profesor profesor;

    @BeforeEach
    void preparar() {
        Contrato sistemas = new Contrato("Sistemas", "Catedratico",
                LocalDate.of(2018, 2, 1), LocalDate.of(2020, 12, 31));
        profesor = new Profesor(new Dni("12345678"), "Carlos Perez",
                new Correo("carlos@uni.edu"), "3001234567", sistemas);
    }

    @Test
    @DisplayName("Un contrato no puede terminar antes de empezar")
    void contratoConFechasIncoherentes() {
        assertThrows(ContratoInvalidoException.class, () ->
                new Contrato("Sistemas", "Titular", LocalDate.of(2024, 1, 1), LocalDate.of(2023, 1, 1)));
    }

    @Test
    @DisplayName("La fecha de fin del contrato es opcional")
    void contratoIndefinido() {
        Contrato indefinido = new Contrato("Matematicas", "Titular", LocalDate.of(2021, 1, 1), null);
        assertTrue(indefinido.fechaFin().isEmpty());
        assertTrue(indefinido.estaActivoEn(LocalDate.of(2050, 1, 1)));
    }

    @Test
    @DisplayName("Invariante: no se permiten contratos solapados")
    void rechazaContratosSolapados() {
        Contrato solapado = new Contrato("Matematicas", "Titular",
                LocalDate.of(2020, 6, 1), LocalDate.of(2022, 1, 1));
        assertThrows(ContratoInvalidoException.class, () -> profesor.agregarContrato(solapado));
    }

    @Test
    @DisplayName("Un contrato consecutivo (sin solaparse) si se acepta")
    void aceptaContratoConsecutivo() {
        profesor.agregarContrato(new Contrato("Matematicas", "Titular", LocalDate.of(2021, 1, 1), null));
        assertEquals(2, profesor.contratos().size());
    }

    @Test
    @DisplayName("Detecta contrato activo en una fecha y en un departamento")
    void contratoActivo() {
        assertTrue(profesor.tieneContratoActivoEn(LocalDate.of(2019, 5, 1)));
        assertFalse(profesor.tieneContratoActivoEn(LocalDate.of(2017, 1, 1)));
        assertTrue(profesor.tieneContratoActivoEnDepartamento("sistemas", LocalDate.of(2019, 5, 1)));
        assertFalse(profesor.tieneContratoActivoEnDepartamento("Matematicas", LocalDate.of(2019, 5, 1)));
    }

    @Test
    @DisplayName("Detecta contrato activo dentro de un rango de fechas")
    void contratoActivoEnRango() {
        assertTrue(profesor.tieneContratoActivoEntre(LocalDate.of(2020, 12, 1), LocalDate.of(2021, 6, 1)));
        assertFalse(profesor.tieneContratoActivoEntre(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 6, 1)));
    }

    @Test
    @DisplayName("Sabe si un contrato finaliza en un ano dado")
    void finalizaEnAnio() {
        assertTrue(profesor.tieneContratoQueFinalizaEnAnio(2020));
        assertFalse(profesor.tieneContratoQueFinalizaEnAnio(2021));
    }

    @Test
    @DisplayName("Detecta profesores que han trabajado en mas de un departamento")
    void masDeUnDepartamento() {
        assertFalse(profesor.haTrabajadoEnMasDeUnDepartamento());
        profesor.agregarContrato(new Contrato("Matematicas", "Titular", LocalDate.of(2021, 1, 1), null));
        assertTrue(profesor.haTrabajadoEnMasDeUnDepartamento());
        assertEquals(2, profesor.departamentosEnLosQueHaTrabajado().size());
    }
}
