package tuti.desi.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
public class Propiedad {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "La dirección es obligatoria")
	@Size(min = 1, max = 200, message = "La dirección es obligatoria")
	private String direccion;

	@NotNull(message = "El tipo de propiedad es obligatorio")
	@Enumerated(EnumType.STRING)
	private TipoPropiedad tipo;

	@NotNull(message = "La cantidad de ambientes es obligatoria")
	@Min(value = 1, message = "Debe tener al menos 1 ambiente")
	private Integer cantidadAmbientes;

	@NotNull(message = "Los metros cuadrados son obligatorios")
	@Positive(message = "Los metros cuadrados deben ser mayores a 0")
	private Double metrosCuadrados;

	@Size(max = 500)
	private String descripcion;

	@Size(max = 300)
	private String comodidades;
	
	@NotNull(message = "El estado de disponibilidad es obligatorio")
	@Enumerated(EnumType.STRING)
	private EstadoDisponibilidad estadoDisponibilidad;

	private Boolean eliminado;

	@NotNull(message = "El propietario es obligatorio")
	@ManyToOne(optional = false)
	private Persona propietario;

	@NotNull(message = "La ciudad es obligatoria")
	@ManyToOne(optional = false)
	private Ciudad ciudad;

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

	public Boolean getEliminada() {
		return eliminado;
	}

	public void setEliminada(Boolean eliminado) {
		this.eliminado = eliminado;
	}

	public Persona getPropietario() {
		return propietario;
	}

	public void setPropietario(Persona propietario) {
		this.propietario = propietario;
	}

	public Ciudad getCiudad() {
		return ciudad;
	}

	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
	}

	
}
