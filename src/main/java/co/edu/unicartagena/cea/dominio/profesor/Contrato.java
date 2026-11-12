package co.edu.unicartagena.cea.dominio.profesor;

import co.edu.unicartagena.cea.dominio.compartido.Validar;
import co.edu.unicartagena.cea.dominio.excepcion.ContratoInvalidoException;
import java.time.LocalDate;
import java.util.Optional;

/**
 * VALUE OBJECT: un contrato del profesor con un departamento academico.
 * "fin" puede ser null cuando el contrato es indefinido (fecha de fin opcional).
 */
public record Contrato(String departamento, String cargo, LocalDate inicio, LocalDate fin) {

    public Contrato {
        departamento = Validar.noVacio(departamento, "El departamento academico");
        cargo = Validar.noVacio(cargo, "El cargo");
        Validar.noNulo(inicio, "La fecha de inicio del contrato");
        if (fin != null && fin.isBefore(inicio)) {
            throw new ContratoInvalidoException("La fecha de fin no puede ser anterior a la de inicio");
        }
    }

    public Optional<LocalDate> fechaFin() {
        return Optional.ofNullable(fin);
    }

    public boolean estaActivoEn(LocalDate fecha) {
        return !fecha.isBefore(inicio) && (fin == null || !fecha.isAfter(fin));
    }

    /** Verdadero si el contrato estuvo activo en algun dia del rango [desde, hasta]. */
    public boolean estuvoActivoEntre(LocalDate desde, LocalDate hasta) {
        boolean empiezaAntesDeQueTermineElRango = !inicio.isAfter(hasta);
        boolean terminaDespuesDeQueEmpieceElRango = fin == null || !fin.isBefore(desde);
        return empiezaAntesDeQueTermineElRango && terminaDespuesDeQueEmpieceElRango;
    }

    public boolean finalizaEnAnio(int anio) {
        return fin != null && fin.getYear() == anio;
    }

    public boolean seSolapaCon(Contrato otro) {
        LocalDate finDeOtro = otro.fin == null ? LocalDate.MAX : otro.fin;
        return estuvoActivoEntre(otro.inicio, finDeOtro);
    }
}
