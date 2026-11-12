package co.edu.unicartagena.cea.dominio.profesor;

import co.edu.unicartagena.cea.dominio.compartido.Correo;
import co.edu.unicartagena.cea.dominio.compartido.Dni;
import co.edu.unicartagena.cea.dominio.compartido.Validar;
import co.edu.unicartagena.cea.dominio.excepcion.ContratoInvalidoException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * ENTIDAD: un profesor, identificado por su DNI.
 * Guarda su historial de contratos; invariante: dos contratos del mismo profesor no se solapan.
 */
public class Profesor {

    private final Dni dni;
    private String nombre;
    private Correo correo;
    private String telefono;
    private final List<Contrato> contratos = new ArrayList<>();

    public Profesor(Dni dni, String nombre, Correo correo, String telefono, Contrato contratoInicial) {
        this.dni = Validar.noNulo(dni, "El DNI");
        this.nombre = Validar.noVacio(nombre, "El nombre del profesor");
        this.correo = Validar.noNulo(correo, "El correo");
        this.telefono = Validar.noVacio(telefono, "El telefono");
        this.contratos.add(Validar.noNulo(contratoInicial, "El contrato inicial"));
    }

    public void agregarContrato(Contrato nuevo) {
        Validar.noNulo(nuevo, "El contrato");
        for (Contrato existente : contratos) {
            if (existente.seSolapaCon(nuevo)) {
                throw new ContratoInvalidoException("El nuevo contrato se solapa con un contrato existente");
            }
        }
        contratos.add(nuevo);
    }

    public boolean tieneContratoActivoEn(LocalDate fecha) {
        return contratos.stream().anyMatch(c -> c.estaActivoEn(fecha));
    }

    public boolean tieneContratoActivoEnDepartamento(String departamento, LocalDate fecha) {
        return contratos.stream()
                .anyMatch(c -> c.departamento().equalsIgnoreCase(departamento) && c.estaActivoEn(fecha));
    }

    public boolean tieneContratoActivoEntre(LocalDate desde, LocalDate hasta) {
        return contratos.stream().anyMatch(c -> c.estuvoActivoEntre(desde, hasta));
    }

    public Optional<Contrato> contratoVigenteEn(LocalDate fecha) {
        return contratos.stream().filter(c -> c.estaActivoEn(fecha)).findFirst();
    }

    public boolean tieneContratoQueFinalizaEnAnio(int anio) {
        return contratos.stream().anyMatch(c -> c.finalizaEnAnio(anio));
    }

    public Set<String> departamentosEnLosQueHaTrabajado() {
        Set<String> departamentos = new LinkedHashSet<>();
        contratos.forEach(c -> departamentos.add(c.departamento()));
        return departamentos;
    }

    public boolean haTrabajadoEnMasDeUnDepartamento() {
        return departamentosEnLosQueHaTrabajado().size() > 1;
    }

    public Dni dni() { return dni; }
    public String nombre() { return nombre; }
    public Correo correo() { return correo; }
    public String telefono() { return telefono; }
    public List<Contrato> contratos() { return Collections.unmodifiableList(contratos); }

    @Override
    public boolean equals(Object otro) {
        return otro instanceof Profesor p && dni.equals(p.dni);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dni);
    }
}
