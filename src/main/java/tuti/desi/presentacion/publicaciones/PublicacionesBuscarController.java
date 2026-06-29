package tuti.desi.presentacion.publicaciones;
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
import tuti.desi.entidades.EstadoPublicacion;
import tuti.desi.entidades.Propiedad;
import tuti.desi.entidades.Publicacion;
import tuti.desi.excepciones.Excepcion;
import tuti.desi.servicios.PropiedadService;
import tuti.desi.servicios.PublicacionService;

@Controller
@RequestMapping("/publicacionesBuscar")
public class PublicacionesBuscarController {
//búsqueda, listado y eliminación.
	 @Autowired
	    private PublicacionService servicioPublicacion;

	    @Autowired
	    private PropiedadService servicioPropiedad;

	    @RequestMapping(method = RequestMethod.GET)
	    public String preparaForm(Model modelo) {

	        PublicacionBuscarForm form = new PublicacionBuscarForm();

	        modelo.addAttribute("formBean", form);

	        return "publicacionesBuscar";
	    }

	    @ModelAttribute("allPropiedades")
	    public List<Propiedad> getAllPropiedades() {
	        return servicioPropiedad.getAllActivas();
	    }

	    @ModelAttribute("allEstados")
	    public EstadoPublicacion[] getAllEstados() {
	        return EstadoPublicacion.values();
	    }

	    @RequestMapping(method = RequestMethod.POST)
	    public String submit(
	            @ModelAttribute("formBean") @Valid PublicacionBuscarForm formBean,
	            BindingResult result,
	            ModelMap modelo,
	            @RequestParam String action) throws Excepcion {

	        if (action.equals("actionBuscar")) {

	            try {

	                List<Publicacion> publicaciones = servicioPublicacion.filter(formBean);

	                modelo.addAttribute("resultados", publicaciones);

	            } catch (Exception e) {

	                ObjectError error = new ObjectError("globalError", e.getMessage());

	                result.addError(error);

	            }

	            modelo.addAttribute("formBean", formBean);

	            return "publicacionesBuscar";
	        }

	        else if (action.equals("actionRegistrar")) {

	            modelo.clear();

	            return "redirect:/publicacionEditar";

	        }

	        else if (action.equals("actionCancelar")) {

	            modelo.clear();

	            return "redirect:/";

	        }

	        return "redirect:/";

	    }

}
