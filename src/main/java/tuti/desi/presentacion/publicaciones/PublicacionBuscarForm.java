package tuti.desi.presentacion.publicaciones;

import java.math.BigDecimal;

import tuti.desi.entidades.EstadoPublicacion;

public class PublicacionBuscarForm {
//formulario de búsqueda.
	private Long propiedadSeleccionada;
    private Long ciudadSeleccionada;
    private EstadoPublicacion estadoSeleccionado;
    private BigDecimal precioMinimo;
    private BigDecimal precioMaximo;

    //constructor vacío
    public PublicacionBuscarForm() {

    }

    public Long getPropiedadSeleccionada() {
        return propiedadSeleccionada;
    }

    public void setPropiedadSeleccionada(Long propiedadSeleccionada) {
        this.propiedadSeleccionada = propiedadSeleccionada;
    }

    public Long getCiudadSeleccionada() {
        return ciudadSeleccionada;
    }

    public void setCiudadSeleccionada(Long ciudadSeleccionada) {
        this.ciudadSeleccionada = ciudadSeleccionada;
    }

    public EstadoPublicacion getEstadoSeleccionado() {
        return estadoSeleccionado;
    }

    public void setEstadoSeleccionado(EstadoPublicacion estadoSeleccionado) {
        this.estadoSeleccionado = estadoSeleccionado;
    }

    public BigDecimal getPrecioMinimo() {
        return precioMinimo;
    }

    public void setPrecioMinimo(BigDecimal precioMinimo) {
        this.precioMinimo = precioMinimo;
    }

    public BigDecimal getPrecioMaximo() {
        return precioMaximo;
    }

    public void setPrecioMaximo(BigDecimal precioMaximo) {
        this.precioMaximo = precioMaximo;
    }

}
