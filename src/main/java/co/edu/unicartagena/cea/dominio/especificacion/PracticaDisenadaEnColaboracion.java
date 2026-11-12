package co.edu.unicartagena.cea.dominio.especificacion;

import co.edu.unicartagena.cea.dominio.practica.Practica;

/** Regla: la practica fue disenada por mas de un profesor. */
public class PracticaDisenadaEnColaboracion implements Especificacion<Practica> {

    @Override
    public boolean esSatisfechaPor(Practica practica) {
        return practica.esColaborativa();
    }
}
