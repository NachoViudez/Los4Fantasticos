package tuti.desi.presentacion.propiedades;
import tuti.desi.entidades.EstadoDisponibilidad;
import tuti.desi.entidades.TipoPropiedad;

public class PropiedadesBuscarForm {
	private String direccion;
	private Long ciudadSeleccionada;
	private TipoPropiedad tipoSeleccionado;
	private EstadoDisponibilidad estadoSeleccionado;
	
	//si el usuario deja vacia la direccion, no filtra por direccion
	public String getDireccion() {
		return direccion == null || direccion.isBlank() ? null : direccion.trim();
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public Long getCiudadSeleccionada() {
		return ciudadSeleccionada;
	}
	public void setCiudadSeleccionada(Long ciudadSeleccionada) {
		this.ciudadSeleccionada = ciudadSeleccionada;
	}
	public TipoPropiedad getTipoSeleccionado() {
		return tipoSeleccionado;
	}
	public void setTipoSeleccionado(TipoPropiedad tipoSeleccionado) {
		this.tipoSeleccionado = tipoSeleccionado;
	}
	public EstadoDisponibilidad getEstadoSeleccionado() {
		return estadoSeleccionado;
	}
	public void setEstadoSeleccionado(EstadoDisponibilidad estadoSeleccionado) {
		this.estadoSeleccionado = estadoSeleccionado;
	}
}
