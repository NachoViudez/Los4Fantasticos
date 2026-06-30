package tuti.desi.servicios;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tuti.desi.accesoDatos.ContratoRepo;
import tuti.desi.accesoDatos.FacturaRepo;
import tuti.desi.accesoDatos.HistorialEstadoFacturaRepo;
import tuti.desi.entidades.Contrato;
import tuti.desi.entidades.EstadoContrato;
import tuti.desi.entidades.EstadoFactura;
import tuti.desi.entidades.Factura;
import tuti.desi.entidades.HistorialEstadoFactura;

@Service
public class FacturaService {

    private final FacturaRepo facturaRepo;
    private final ContratoRepo contratoRepo;
    private final HistorialEstadoFacturaRepo historialRepo;

    public FacturaService(
            FacturaRepo facturaRepo,
            ContratoRepo contratoRepo,
            HistorialEstadoFacturaRepo historialRepo) {
        this.facturaRepo = facturaRepo;
        this.contratoRepo = contratoRepo;
        this.historialRepo = historialRepo;
    }

    public List<Factura> listarFacturasNoEliminadas() {
        return facturaRepo.findByEliminadoFalse();
    }

    public List<Factura> buscarFacturasConFiltros(
            Long contratoId,
            EstadoFactura estado,
            LocalDate fechaDesde,
            LocalDate fechaHasta) {

        return facturaRepo.buscarConFiltros(contratoId, estado, fechaDesde, fechaHasta);
    }

    public Factura buscarPorId(Long id) {
        return facturaRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe una factura con el id: " + id));
    }

    public List<Contrato> listarContratosActivos() {
        return contratoRepo.findByEstadoAndEliminadoFalse(EstadoContrato.ACTIVO);
    }

    @Transactional
    public Factura crearFactura(Factura factura) {
        validarAlta(factura);

        factura.setEstado(EstadoFactura.PENDIENTE);
        factura.setEliminado(false);

        limpiarDatosPagoSiNoEstaPagada(factura);

        Factura facturaGuardada = facturaRepo.save(factura);

        historialRepo.save(
                new HistorialEstadoFactura(facturaGuardada, null, facturaGuardada.getEstado())
        );

        return facturaGuardada;
    }

    @Transactional
    public Factura modificarFactura(Long id, Factura datosNuevos) {
        Factura facturaExistente = buscarPorId(id);

        if (facturaExistente.isEliminado()) {
            throw new IllegalArgumentException("No se puede modificar una factura eliminada.");
        }

        if (facturaExistente.getEstado() == EstadoFactura.PAGADA) {
            throw new IllegalArgumentException("No se puede modificar una factura pagada.");
        }

        if (facturaExistente.getEstado() == EstadoFactura.ANULADA) {
            throw new IllegalArgumentException("No se puede modificar una factura anulada.");
        }

        EstadoFactura estadoAnterior = facturaExistente.getEstado();
        EstadoFactura estadoNuevo = datosNuevos.getEstado();

        validarModificacion(facturaExistente, datosNuevos);

        facturaExistente.setConcepto(datosNuevos.getConcepto());
        facturaExistente.setFechaEmision(datosNuevos.getFechaEmision());
        facturaExistente.setFechaVencimiento(datosNuevos.getFechaVencimiento());
        facturaExistente.setImporte(datosNuevos.getImporte());
        facturaExistente.setEstado(estadoNuevo);

        facturaExistente.setFechaPago(datosNuevos.getFechaPago());
        facturaExistente.setMedioPago(datosNuevos.getMedioPago());
        facturaExistente.setImportePagado(datosNuevos.getImportePagado());
        facturaExistente.setInteresPagado(datosNuevos.getInteresPagado());

        if (estadoNuevo != EstadoFactura.PAGADA) {
            limpiarDatosPagoSiNoEstaPagada(facturaExistente);
        }

        Factura facturaGuardada = facturaRepo.save(facturaExistente);

        if (estadoAnterior != estadoNuevo) {
            historialRepo.save(
                    new HistorialEstadoFactura(facturaGuardada, estadoAnterior, estadoNuevo)
            );
        }

        return facturaGuardada;
    }

    @Transactional
    public void eliminarFactura(Long id) {
        Factura factura = buscarPorId(id);

        if (factura.getEstado() == EstadoFactura.PAGADA) {
            throw new IllegalArgumentException("No se puede eliminar una factura pagada.");
        }

        factura.setEliminado(true);
        facturaRepo.save(factura);
    }

    private void validarAlta(Factura factura) {
        validarDatosBasicos(factura);

        if (factura.getContrato() == null || factura.getContrato().getId() == null) {
            throw new IllegalArgumentException("Debe seleccionar un contrato.");
        }

        Contrato contrato = contratoRepo
                .findByIdAndEstadoAndEliminadoFalse(factura.getContrato().getId(), EstadoContrato.ACTIVO)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Solo se pueden crear facturas para contratos activos y no eliminados."
                ));

        factura.setContrato(contrato);
    }

    private void validarModificacion(Factura facturaExistente, Factura datosNuevos) {
        validarDatosBasicos(datosNuevos);

        if (datosNuevos.getEstado() == null) {
            throw new IllegalArgumentException("Debe indicar el estado de la factura.");
        }

        validarCambioEstado(facturaExistente.getEstado(), datosNuevos.getEstado());

        if (datosNuevos.getEstado() == EstadoFactura.PAGADA) {
            validarDatosPago(datosNuevos);
        }

        if (datosNuevos.getEstado() == EstadoFactura.ANULADA) {
            if (datosNuevos.getFechaPago() != null
                    || datosNuevos.getMedioPago() != null
                    || datosNuevos.getImportePagado() != null
                    || datosNuevos.getInteresPagado() != null) {
                throw new IllegalArgumentException("No se pueden registrar datos de pago si la factura está anulada.");
            }
        }
    }

    private void validarDatosBasicos(Factura factura) {
        if (factura.getConcepto() == null || factura.getConcepto().isBlank()) {
            throw new IllegalArgumentException("El concepto es obligatorio.");
        }

        if (factura.getFechaEmision() == null) {
            throw new IllegalArgumentException("La fecha de emisión es obligatoria.");
        }

        if (factura.getFechaVencimiento() == null) {
            throw new IllegalArgumentException("La fecha de vencimiento es obligatoria.");
        }

        if (factura.getFechaVencimiento().isBefore(factura.getFechaEmision())) {
            throw new IllegalArgumentException("La fecha de vencimiento debe ser igual o posterior a la fecha de emisión.");
        }

        if (factura.getImporte() == null || factura.getImporte().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El importe debe ser un número positivo.");
        }
    }

    private void validarDatosPago(Factura factura) {
        if (factura.getFechaPago() == null) {
            throw new IllegalArgumentException("Debe indicar la fecha de pago.");
        }

        if (factura.getMedioPago() == null) {
            throw new IllegalArgumentException("Debe indicar el medio de pago.");
        }

        if (factura.getImportePagado() == null || factura.getImportePagado().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El importe pagado debe ser un número positivo.");
        }

        if (factura.getInteresPagado() != null
                && factura.getInteresPagado().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El interés pagado no puede ser negativo.");
        }
    }

    private void validarCambioEstado(EstadoFactura estadoActual, EstadoFactura estadoNuevo) {
        if (estadoActual == estadoNuevo) {
            return;
        }

        if (estadoActual == EstadoFactura.PENDIENTE) {
            if (estadoNuevo == EstadoFactura.PAGADA
                    || estadoNuevo == EstadoFactura.VENCIDA
                    || estadoNuevo == EstadoFactura.ANULADA) {
                return;
            }
        }

        if (estadoActual == EstadoFactura.VENCIDA && estadoNuevo == EstadoFactura.PAGADA) {
            return;
        }

        throw new IllegalArgumentException("Cambio de estado no permitido: " + estadoActual + " -> " + estadoNuevo);
    }

    private void limpiarDatosPagoSiNoEstaPagada(Factura factura) {
        if (factura.getEstado() != EstadoFactura.PAGADA) {
            factura.setFechaPago(null);
            factura.setMedioPago(null);
            factura.setImportePagado(null);
            factura.setInteresPagado(null);
        }
    }
}