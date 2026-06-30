package tuti.desi.entidades;

import java.time.LocalDateTime;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "historial_estados_factura")
public class HistorialEstadoFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "factura_id", nullable = false)
    private Factura factura;

    @Enumerated(EnumType.STRING)
    private EstadoFactura estadoAnterior;

    @Enumerated(EnumType.STRING)
    private EstadoFactura estadoNuevo;

    private LocalDateTime fechaCambio;

    public HistorialEstadoFactura() {
    }

    public HistorialEstadoFactura(Factura factura, EstadoFactura estadoAnterior, EstadoFactura estadoNuevo) {
        this.factura = factura;
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.fechaCambio = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Factura getFactura() {
        return factura;
    }

    public EstadoFactura getEstadoAnterior() {
        return estadoAnterior;
    }

    public EstadoFactura getEstadoNuevo() {
        return estadoNuevo;
    }

    public LocalDateTime getFechaCambio() {
        return fechaCambio;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public void setEstadoAnterior(EstadoFactura estadoAnterior) {
        this.estadoAnterior = estadoAnterior;
    }

    public void setEstadoNuevo(EstadoFactura estadoNuevo) {
        this.estadoNuevo = estadoNuevo;
    }

    public void setFechaCambio(LocalDateTime fechaCambio) {
        this.fechaCambio = fechaCambio;
    }
}