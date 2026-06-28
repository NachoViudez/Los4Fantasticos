package tuti.desi.presentacion.contratos;
import java.math.BigDecimal;
import java.time.LocalDate;
import tuti.desi.entidades.EstadoContrato;
import tuti.desi.entidades.Contrato;

public class ContratoForm {
	private Long id;
	private Long idPropiedadSeleccionada;
	private Long idInquilinoSeleccionado;
	private LocalDate fechaInicio;
	private Integer duracionMeses;
	private BigDecimal importeMensual;
	private Integer diaVencimientoMensual;
	private String descripcion;
	private EstadoContrato estado;
	
	public ContratoForm() {
	}

	public ContratoForm(Contrato contrato) {
		this.id = contrato.getId();
		this.idPropiedadSeleccionada = contrato.getPropiedad().getId();
		this.idInquilinoSeleccionado = contrato.getInquilino().getId();
		this.fechaInicio = contrato.getFechaInicio();
		this.duracionMeses = contrato.getDuracionMeses();
		this.importeMensual = contrato.getImporteMensual();
		this.diaVencimientoMensual = contrato.getDiaVencimientoMensual();
		this.descripcion = contrato.getDescripcion();
		this.estado = contrato.getEstado();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Integer getDuracionMeses() {
		return duracionMeses;
	}

	public void setDuracionMeses(Integer duracionMeses) {
		this.duracionMeses = duracionMeses;
	}

	public BigDecimal getImporteMensual() {
		return importeMensual;
	}

	public void setImporteMensual(BigDecimal importeMensual) {
		this.importeMensual = importeMensual;
	}

	public Integer getDiaVencimientoMensual() {
		return diaVencimientoMensual;
	}

	public void setDiaVencimientoMensual(Integer diaVencimientoMensual) {
		this.diaVencimientoMensual = diaVencimientoMensual;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public EstadoContrato getEstado() {
		return estado;
	}

	public void setEstado(EstadoContrato estado) {
		this.estado = estado;
	}
}