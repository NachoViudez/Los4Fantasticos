package tuti.desi.presentacion.publicaciones;
import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import tuti.desi.entidades.EstadoPublicacion;
import tuti.desi.entidades.Propiedad;
import tuti.desi.entidades.Publicacion;

public class PublicacionForm {
/*formulario de alta/edición.
 * no es @ENTITY esto no representa una tabla de la base de datos. Es solamente un objeto 
 * auxiliar para transportar los datos del formulario HTML al Controller y del Controller al Service.*/
	
	private Long id;

    @NotNull(message = "La propiedad es obligatoria")
    private Long idPropiedad;

    @NotNull(message = "El precio mensual es obligatorio")
    @Positive(message = "El precio mensual debe ser mayor a 0")
    private BigDecimal precioMensual;

    @Size(max = 500)
    private String condiciones;

    @Size(max = 500)
    private String descripcion;

    @NotNull(message = "La fecha de publicación es obligatoria")
    private LocalDate fechaPublicacion;

    @NotNull(message = "El estado es obligatorio")
    private EstadoPublicacion estado;

    public PublicacionForm() {

    }

    public PublicacionForm(Publicacion p) {

        this.id = p.getId();
        this.idPropiedad = p.getPropiedad() == null ? null : p.getPropiedad().getId();
        this.precioMensual = p.getPrecioMensual();
        this.condiciones = p.getCondiciones();
        this.descripcion = p.getDescripcion();
        this.fechaPublicacion = p.getFechaPublicacion();
        this.estado = p.getEstado();

    }

    public Publicacion toPojo() {

    	Publicacion p = new Publicacion();

        p.setId(id);
        p.setPrecioMensual(precioMensual);
        p.setCondiciones(condiciones);
        p.setDescripcion(descripcion);
        p.setFechaPublicacion(fechaPublicacion);
        p.setEstado(estado);

        return p;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdPropiedad() {
        return idPropiedad;
    }

    public void setIdPropiedad(Long idPropiedad) {
        this.idPropiedad = idPropiedad;
    }

    public BigDecimal getPrecioMensual() {
        return precioMensual;
    }

    public void setPrecioMensual(BigDecimal precioMensual) {
        this.precioMensual = precioMensual;
    }

    public String getCondiciones() {
        return condiciones;
    }

    public void setCondiciones(String condiciones) {
        this.condiciones = condiciones;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDate fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public EstadoPublicacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoPublicacion estado) {
        this.estado = estado;
    }
    
}
