package tuti.desi.presentacion.contratos;
import java.util.List;
import java.util.Optional;
import org.springframework.validation.FieldError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import tuti.desi.entidades.Contrato;
import tuti.desi.entidades.EstadoContrato;
import tuti.desi.entidades.Persona;
import tuti.desi.entidades.Propiedad;
import tuti.desi.excepciones.Excepcion;
import tuti.desi.servicios.ContratoService;

@Controller
@RequestMapping("/contratoEditar")
public class ContratoRegistrarEditarController {
	@Autowired
	private ContratoService servicioContrato;
	@RequestMapping(path = { "", "/{id}" }, method = RequestMethod.GET)
	public String preparaForm(Model modelo, @PathVariable("id") Optional<Long> id) throws Exception {

		if (id.isPresent()) {
			Contrato entity = servicioContrato.buscarPorId(id.get());
			ContratoForm form = new ContratoForm(entity);
			modelo.addAttribute("formBean", form);
		} else {
			modelo.addAttribute("formBean", new ContratoForm());
		}
		return "contratoEditar";
	}

	@ModelAttribute("allPropiedades")
	public List<Propiedad> getAllPropiedades() {
		return this.servicioContrato.buscarPropiedades();
	}

	@ModelAttribute("allInquilinos")
	public List<Persona> getAllInquilinos() {
		return this.servicioContrato.buscarInquilinos();
	}

	@ModelAttribute("estadosContrato")
	public EstadoContrato[] getEstadosContrato() {
		return servicioContrato.buscarEstados();
	}

	@RequestMapping(method = RequestMethod.POST)
	public String submit(@ModelAttribute("formBean") @Valid ContratoForm formBean, BindingResult result,
			ModelMap modelo, @RequestParam String action) {

		if (action.equals("actionAceptar")) {

			if (result.hasErrors()) {
				modelo.addAttribute("formBean", formBean);
				return "contratoEditar";
			} else {
				try {
					servicioContrato.guardar(formBean);
					return "redirect:/contratosBuscar";
				} catch (Excepcion e) {

					if (e.getAtributo() == null) {
						ObjectError error = new ObjectError("globalError", e.getMessage());
						result.addError(error);
					} else {
						FieldError error = new FieldError("formBean", e.getAtributo(), e.getMessage());
						result.addError(error);
					}

					modelo.addAttribute("formBean", formBean);
					return "contratoEditar";
				}
			}

		} else if (action.equals("actionCancelar")) {
			modelo.clear();
			return "redirect:/contratosBuscar";
		}

		return "redirect:/";
	}

	@RequestMapping(path = "/delete/{id}", method = RequestMethod.POST)
	public String deleteById(Model model, @PathVariable("id") Long id, RedirectAttributes redirectAttributes) {

		try {
			servicioContrato.eliminar(id);
		} catch (Excepcion e) {
			redirectAttributes.addFlashAttribute("errorEliminar", e.getMessage());
		}

		return "redirect:/contratosBuscar";
	}
}
