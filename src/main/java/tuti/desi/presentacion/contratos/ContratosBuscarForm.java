package tuti.desi.presentacion.contratos;
import java.time.LocalDate;
import tuti.desi.entidades.EstadoContrato;

public class ContratosBuscarForm {
	private Long idPropiedadSeleccionada;
	private Long idInquilinoSeleccionado;
	private EstadoContrato estado;
	private LocalDate fechaInicio;

	public Long getIdPropiedadSeleccionada() {
		return idPropiedadSeleccionada;
	}

	public void setIdPropiedadSeleccionada(Long idPropiedadSeleccionada) {
		this.idPropiedadSeleccionada = idPropiedadSeleccionada;
	}

	public Long getIdInquilinoSeleccionado() {
		return idInquilinoSeleccionado;
	}

	public void setIdInquilinoSeleccionado(Long idInquilinoSeleccionado) {
		this.idInquilinoSeleccionado = idInquilinoSeleccionado;
	}

	public EstadoContrato getEstado() {
		return estado;
	}

	public void setEstado(EstadoContrato estado) {
		this.estado = estado;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
}