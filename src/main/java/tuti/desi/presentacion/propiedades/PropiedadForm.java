package tuti.desi.presentacion.propiedades;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import tuti.desi.entidades.EstadoDisponibilidad;
import tuti.desi.entidades.Propiedad;
import tuti.desi.entidades.TipoPropiedad;

public class PropiedadForm {

	private Long id;

	@NotBlank(message = "la direccion es obligatoria")
	@Size(max = 200)
	private String direccion;

	@NotNull(message = "La ciudad es obligatoria")
	private Long idCiudad;

	@NotNull (message = "El tipo de propiedad es obligatorio, seleccione uno")
	private TipoPropiedad tipo;

	@NotNull(message = "La cantidad de ambientes es obligatoria")
	@Min(value=1, message = "L propiedad debe tener minimo 1 ambiente")
	private Integer cantidadAmbientes;

	@NotNull(message = "Los metros cuadrados son obligatorios")
	@Positive(message = "La cantidad debe ser mayor a 0")
	private Double metrosCuadrados;

	@Size(max = 500)
	private String descripcion;

	@Size(max = 300)
	private String comodidades;

	@NotNull (message = "El estado de disponibilidad es obligatorio, seleccione uno")
	private EstadoDisponibilidad estadoDisponibilidad;

	@NotNull(message = "El propietario es obligatorio")
	private Long idPropietario;
	
	public PropiedadForm(){
		
	}
	public PropiedadForm(Propiedad propiedad){
		this.id = propiedad.getId();
    	this.direccion = propiedad.getDireccion();
    	this.idCiudad = propiedad.getCiudad() == null ? null : propiedad.getCiudad().getId();// ternario que pregunta si el idciudad es null o no 
    	this.tipo = propiedad.getTipo();
    	this.cantidadAmbientes = propiedad.getCantidadAmbientes();
    	this.metrosCuadrados = propiedad.getMetrosCuadrados();
    	this.descripcion = propiedad.getDescripcion();
    	this.comodidades = propiedad.getComodidades();
    	this.estadoDisponibilidad = propiedad.getEstadoDisponibilidad();
    	this.idPropietario = propiedad.getPropietario() == null ? null : propiedad.getPropietario().getId();
		
	}
	public Propiedad toPojo() {
     	Propiedad propiedad = new Propiedad();
     	propiedad.setId(id);
     	propiedad.setDireccion(direccion);
     	propiedad.setTipo(tipo);
     	propiedad.setCantidadAmbientes(cantidadAmbientes);
     	propiedad.setMetrosCuadrados(metrosCuadrados);
     	propiedad.setDescripcion(descripcion);
    	propiedad.setComodidades(comodidades);
     	propiedad.setEstadoDisponibilidad(estadoDisponibilidad);
     return propiedad;
}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Long getIdCiudad() {
		return idCiudad;
	}

	public void setIdCiudad(Long idCiudad) {
		this.idCiudad = idCiudad;
	}

	public TipoPropiedad getTipo() {
		return tipo;
	}

	public void setTipo(TipoPropiedad tipo) {
		this.tipo = tipo;
	}

	public Integer getCantidadAmbientes() {
		return cantidadAmbientes;
	}

	public void setCantidadAmbientes(Integer cantidadAmbientes) {
		this.cantidadAmbientes = cantidadAmbientes;
	}

	public Double getMetrosCuadrados() {
		return metrosCuadrados;
	}

	public void setMetrosCuadrados(Double metrosCuadrados) {
		this.metrosCuadrados = metrosCuadrados;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getComodidades() {
		return comodidades;
	}

	public void setComodidades(String comodidades) {
		this.comodidades = comodidades;
	}

	public EstadoDisponibilidad getEstadoDisponibilidad() {
		return estadoDisponibilidad;
	}

	public void setEstadoDisponibilidad(EstadoDisponibilidad estadoDisponibilidad) {
		this.estadoDisponibilidad = estadoDisponibilidad;
	}

	public Long getIdPropietario() {
		return idPropietario;
	}

	public void setIdPropietario(Long idPropietario) {
		this.idPropietario = idPropietario;
	}
	
	
}
