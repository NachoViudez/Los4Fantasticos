package tuti.desi.entidades;

import java.math.BigDecimal;
import java.time.LocalDate;

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
@Table(name = "facturas")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contrato_id", nullable = false)
    private Contrato contrato;

    private String concepto;

    private LocalDate fechaEmision;

    private LocalDate fechaVencimiento;

    private BigDecimal importe;

    @Enumerated(EnumType.STRING)
    private EstadoFactura estado = EstadoFactura.PENDIENTE;

    private LocalDate fechaPago;

    @Enumerated(EnumType.STRING)
    private MedioPago medioPago;

    private BigDecimal importePagado;

    private BigDecimal interesPagado;

    private boolean eliminado = false;

    public Factura() {
    }

    public Long getId() {
        return id;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public String getConcepto() {
        return concepto;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public EstadoFactura getEstado() {
        return estado;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public MedioPago getMedioPago() {
        return medioPago;
    }

    public BigDecimal getImportePagado() {
        return importePagado;
    }

    public BigDecimal getInteresPagado() {
        return interesPagado;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public void setEstado(EstadoFactura estado) {
        this.estado = estado;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public void setMedioPago(MedioPago medioPago) {
        this.medioPago = medioPago;
    }

    public void setImportePagado(BigDecimal importePagado) {
        this.importePagado = importePagado;
    }

    public void setInteresPagado(BigDecimal interesPagado) {
        this.interesPagado = interesPagado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }
}