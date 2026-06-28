package tuti.desi.presentacion.contratos;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import tuti.desi.entidades.Contrato;
import tuti.desi.entidades.EstadoContrato;
import tuti.desi.entidades.Persona;
import tuti.desi.entidades.Propiedad;
import tuti.desi.excepciones.Excepcion;
import tuti.desi.servicios.ContratoService;

@Controller
@RequestMapping("/contratosBuscar")
public class ContratosBuscarController {
	@Autowired
	private ContratoService servicioContrato;

	@ModelAttribute("allContratos")
	public List<Contrato> getAllContratos() {
		return this.servicioContrato.buscar(new ContratosBuscarForm());
	}

	@ModelAttribute("allPropiedades")
	public List<Propiedad> getAllPropiedades() {
		return servicioContrato.buscarPropiedades();
	}

	@ModelAttribute("allInquilinos")
	public List<Persona> getAllInquilinos() {
		return servicioContrato.buscarInquilinos();
	}

	@ModelAttribute("estadosContrato")
	public EstadoContrato[] getEstadosContrato() {
		return servicioContrato.buscarEstados();
	}

	@RequestMapping(method = RequestMethod.GET)
	public String preparaForm(Model modelo) {
		ContratosBuscarForm form = new ContratosBuscarForm();
		modelo.addAttribute("formBean", form);
		modelo.addAttribute("resultados", servicioContrato.buscar(form));
		return "contratosBuscar";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String submit(@ModelAttribute("formBean") @Valid ContratosBuscarForm formBean, BindingResult result,
			ModelMap modelo, @RequestParam String action) throws Excepcion {

		if (action.equals("actionBuscar")) {
			try {
				List<Contrato> contratos = servicioContrato.buscar(formBean);
				modelo.addAttribute("resultados", contratos);
			} catch (Exception e) {
				ObjectError error = new ObjectError("globalError", e.getMessage());
				result.addError(error);
			}

			modelo.addAttribute("formBean", formBean);
			return "contratosBuscar";

		} else if (action.equals("actionCancelar")) {
			modelo.clear();
			return "redirect:/";

		} else if (action.equals("actionRegistrar")) {
			modelo.clear();
			return "redirect:/contratoEditar";
		}
		return "redirect:/";
	}
}
