package co.edu.unicartagena.cea.dominio.evento;

import java.time.LocalDateTime;

/** Algo importante que YA ocurrio en el negocio. Siempre en pasado: NotaRegistrada, PracticaPresentada... */
public interface EventoDeDominio {
    LocalDateTime ocurridoEn();
}
