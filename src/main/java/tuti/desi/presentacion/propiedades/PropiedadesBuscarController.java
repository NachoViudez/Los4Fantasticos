package tuti.desi.presentacion.propiedades;

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
import tuti.desi.entidades.Ciudad;
import tuti.desi.entidades.EstadoDisponibilidad;
import tuti.desi.entidades.Propiedad;
import tuti.desi.entidades.TipoPropiedad;
import tuti.desi.excepciones.Excepcion;
import tuti.desi.servicios.CiudadService;
import tuti.desi.servicios.PropiedadService;

@Controller
@RequestMapping("/propiedadesBuscar")
public class PropiedadesBuscarController {

	@Autowired
	private PropiedadService servicioPropiedad;

	@Autowired
	private CiudadService servicioCiudad;

	@ModelAttribute("allPropiedades")
	public List<Propiedad> getAllPropiedades() {
		return this.servicioPropiedad.getAllActivas();
	}
//cargo las ciudades, los tipos de propiedad y los estado para poder usarlos de filtro en el controller
	
	@ModelAttribute("allCiudades")
	public List<Ciudad> getAllCiudades() {
		return servicioCiudad.getAll();
	}
	
	@ModelAttribute("tiposPropiedad")
	public TipoPropiedad[] getTipoPropiedad() {
		return TipoPropiedad.values();
	}
	@ModelAttribute("estadosDisponibilidad")
	public EstadoDisponibilidad[] getEstadoDisponibilidad() {
		return EstadoDisponibilidad.values();
	}

	@RequestMapping(method = RequestMethod.GET)
	public String preparaForm(Model modelo) {
		PropiedadesBuscarForm form = new PropiedadesBuscarForm();
		modelo.addAttribute("formBean", form);
		//esto lo pongo para que ya aparezcan todas las propiedades cuando entras en la pagina
		modelo.addAttribute("resultados", servicioPropiedad.getAllActivas());
		return "propiedadesBuscar";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String submit(@ModelAttribute("formBean") @Valid PropiedadesBuscarForm formBean, BindingResult result,
			ModelMap modelo, @RequestParam String action) throws Excepcion {
		if (action.equals("actionBuscar")) {
			try {
				List<Propiedad> propiedades = servicioPropiedad.filtrar(formBean);
				modelo.addAttribute("resultados", propiedades);
			} catch (Exception e) {
				ObjectError error = new ObjectError("globalError", e.getMessage());
				result.addError(error);
			}

			modelo.addAttribute("formBean", formBean);
			return "propiedadesBuscar";
		} else if (action.equals("actionCancelar")) {
			modelo.clear();
			return "redirect:/";
		} else if (action.equals("actionRegistrar")) {
			modelo.clear();
			return "redirect:/propiedadEditar";
		}
		return "redirect:/";
	}
}
