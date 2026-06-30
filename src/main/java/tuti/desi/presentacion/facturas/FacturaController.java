package tuti.desi.presentacion.facturas;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import tuti.desi.entidades.Contrato;
import tuti.desi.entidades.EstadoFactura;
import tuti.desi.entidades.Factura;
import tuti.desi.entidades.MedioPago;
import tuti.desi.servicios.FacturaService;

@Controller
public class FacturaController {

    private final FacturaService facturaService;

    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    @GetMapping("/facturas")
    public String listarFacturas(
            @RequestParam(required = false) Long contratoId,
            @RequestParam(required = false) EstadoFactura estado,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            Model model) {

        model.addAttribute("facturas",
                facturaService.buscarFacturasConFiltros(contratoId, estado, fechaDesde, fechaHasta));

        model.addAttribute("contratos", facturaService.listarContratosActivos());
        model.addAttribute("estados", EstadoFactura.values());

        model.addAttribute("contratoIdSeleccionado", contratoId);
        model.addAttribute("estadoSeleccionado", estado);
        model.addAttribute("fechaDesde", fechaDesde);
        model.addAttribute("fechaHasta", fechaHasta);

        return "facturasBuscar";
    }

    @GetMapping("/facturas/nueva")
    public String mostrarFormularioAlta(Model model) {
        Factura factura = new Factura();
        factura.setContrato(new Contrato());

        model.addAttribute("factura", factura);
        model.addAttribute("contratos", facturaService.listarContratosActivos());

        return "facturaRegistrar";
    }

    @PostMapping("/facturas/guardar")
    public String guardarFactura(Factura factura, RedirectAttributes redirectAttributes) {
        try {
            facturaService.crearFactura(factura);
            redirectAttributes.addFlashAttribute("mensajeExito", "Factura creada correctamente.");
            return "redirect:/facturas";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/facturas/nueva";
        }
    }

    @GetMapping("/facturas/editar/{id}")
    public String mostrarFormularioEdicion(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            Factura factura = facturaService.buscarPorId(id);

            model.addAttribute("factura", factura);
            model.addAttribute("estados", EstadoFactura.values());
            model.addAttribute("mediosPago", MedioPago.values());

            return "facturaEditar";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/facturas";
        }
    }

    @PostMapping("/facturas/actualizar/{id}")
    public String actualizarFactura(
            @PathVariable Long id,
            Factura factura,
            RedirectAttributes redirectAttributes) {

        try {
            facturaService.modificarFactura(id, factura);
            redirectAttributes.addFlashAttribute("mensajeExito", "Factura modificada correctamente.");
            return "redirect:/facturas";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/facturas/editar/" + id;
        }
    }

    @GetMapping("/facturas/eliminar/{id}")
    public String eliminarFactura(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            facturaService.eliminarFactura(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Factura eliminada correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }

        return "redirect:/facturas";
    }
}