package co.edu.unicartagena.cea.dominio.evento;

import co.edu.unicartagena.cea.dominio.compartido.Nota;
import co.edu.unicartagena.cea.dominio.compartido.NumeroMatricula;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** EVENTO: un alumno presento una practica y obtuvo una nota. */
public record PracticaPresentada(String codigoPractica, NumeroMatricula matricula, LocalDate fecha, Nota nota,
                                 LocalDateTime ocurridoEn) implements EventoDeDominio {

    public PracticaPresentada(String codigoPractica, NumeroMatricula matricula, LocalDate fecha, Nota nota) {
        this(codigoPractica, matricula, fecha, nota, LocalDateTime.now());
    }
}
