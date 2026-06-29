package tuti.desi.presentacion.propiedades;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import tuti.desi.entidades.Ciudad;
import tuti.desi.entidades.EstadoDisponibilidad;
import tuti.desi.entidades.Persona;
import tuti.desi.entidades.Propiedad;
import tuti.desi.entidades.TipoPropiedad;
import tuti.desi.excepciones.Excepcion;
import tuti.desi.presentacion.ciudades.CiudadForm;
import tuti.desi.servicios.CiudadService;
import tuti.desi.servicios.PersonaService;
import tuti.desi.servicios.PropiedadService;

@Controller
@RequestMapping("/propiedadEditar")
public class PropiedadRegistrarEditarController {

	@Autowired
	private CiudadService servicioCiudad;
	@Autowired
	private PropiedadService servicioPropiedad;
	@Autowired
	private PersonaService servicioPersona;

	@RequestMapping(path = { "", "/{id}" }, method = RequestMethod.GET)
	public String preparaForm(Model modelo, @PathVariable("id") Optional<Long> id) throws Exception {
		if (id.isPresent()) {
			Propiedad entity = servicioPropiedad.getById(id.get());
			PropiedadForm form = new PropiedadForm(entity);
			modelo.addAttribute("formBean", form);
		} else {
			modelo.addAttribute("formBean", new PropiedadForm());
		}
		return "propiedadEditar";
	}

	@ModelAttribute("allCiudades")
	public List<Ciudad> getAllCiudades() {
		return this.servicioCiudad.getAll();
	}

	@ModelAttribute("allPropietarios")
	public List<Persona> getAllPropietarios() {
		return this.servicioPersona.getAll();
	}
	
	@ModelAttribute("tiposPropiedad")
	public TipoPropiedad[] getTipoPropiedad() {
		return TipoPropiedad.values();
	}
	@ModelAttribute("estadosDisponibilidad")
	public EstadoDisponibilidad[] getEstadoDisponibilidad() {
		return EstadoDisponibilidad.values();
	}

	@RequestMapping(method = RequestMethod.POST)
	public String submit(@ModelAttribute("formBean") @Valid PropiedadForm formBean, BindingResult result,
			ModelMap modelo, @RequestParam String action) {

		if (action.equals("actionAceptar")) {
			if (result.hasErrors()) {
				modelo.addAttribute("formBean", formBean);
				return "propiedadEditar";
			} else {
				try {
					Propiedad propiedad = formBean.toPojo();
					propiedad.setCiudad(servicioCiudad.getById(formBean.getIdCiudad()));
					propiedad.setPropietario(servicioPersona.getPersonaById(formBean.getIdPropietario()));
					servicioPropiedad.guardar(propiedad);
					return "redirect:/propiedadesBuscar";
				} catch (Excepcion e) {
					if (e.getAtributo() == null) {
						ObjectError error = new ObjectError("globalError", e.getMessage());
						result.addError(error);
					} else {
						FieldError error1 = new FieldError("formBean", e.getAtributo(), e.getMessage());
						result.addError(error1);

					}
					 modelo.addAttribute("formBean",formBean);
		    			return "propiedadEditar";
				}
			}
		}
		else if(action.equals("actionCancelar"))
    	{
    		modelo.clear();
    		return "redirect:/propiedadesBuscar";
    	}
    		
    	return "redirect:/";
	}
	
	@RequestMapping(path = "/delete/{id}", method = RequestMethod.POST)
	public String deleteById(Model model, @PathVariable("id") Long id, RedirectAttributes redirectAttributes)
	{
		try {
			servicioPropiedad.eliminarById(id);
		} catch (Excepcion e) {
			redirectAttributes.addFlashAttribute("errorEliminar", e.getMessage());
		}
		return "redirect:/propiedadesBuscar";
	}

}
