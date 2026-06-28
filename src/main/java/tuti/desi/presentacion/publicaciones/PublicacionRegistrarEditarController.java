package tuti.desi.presentacion.publicaciones;
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

import jakarta.validation.Valid;
import tuti.desi.entidades.Ciudad;
import tuti.desi.entidades.EstadoPublicacion;
import tuti.desi.entidades.Propiedad;
import tuti.desi.entidades.Publicacion;
import tuti.desi.excepciones.Excepcion;
import tuti.desi.servicios.CiudadService;
import tuti.desi.servicios.PropiedadService;
import tuti.desi.servicios.PublicacionService;

@Controller
@RequestMapping("/publicacionEditar")
public class PublicacionRegistrarEditarController {
	//alta y modificación.
	@Autowired
    private PublicacionService servicioPublicacion;

    @Autowired
    private PropiedadService servicioPropiedad;
    
    @Autowired
    private CiudadService servicioCiudad;

    @RequestMapping(path = {"", "/{id}"}, method = RequestMethod.GET)
    public String preparaForm(Model modelo,
            @PathVariable("id") Optional<Long> id) throws Exception {

        if (id.isPresent()) {

            Publicacion entity = servicioPublicacion.getById(id.get());

            PublicacionForm form = new PublicacionForm(entity);

            modelo.addAttribute("formBean", form);

        } else {

            modelo.addAttribute("formBean", new PublicacionForm());

        }

        return "publicacionEditar";
    }

    @ModelAttribute("allPropiedades")
    public List<Propiedad> getAllPropiedades() {
        return servicioPropiedad.getAllActivas();
    }

    @ModelAttribute("allEstados")
    public EstadoPublicacion[] getAllEstados() {
        return EstadoPublicacion.values();
    }
    
    @ModelAttribute("allCiudades")
    public List<Ciudad> getAllCiudades() {
        return servicioCiudad.getAll();
    }

    @RequestMapping(path="/delete/{id}", method=RequestMethod.POST)
    public String deleteById(Model model,
                             @PathVariable("id") Long id) throws Excepcion {

        servicioPublicacion.deleteById(id);

        return "redirect:/publicacionesBuscar";
    }

    @RequestMapping(method=RequestMethod.POST)
    public String submit(

            @ModelAttribute("formBean")
            @Valid PublicacionForm formBean,

            BindingResult result,

            ModelMap modelo,

            @RequestParam String action) {

        if(action.equals("actionAceptar")) {

            if(result.hasErrors()) {

                modelo.addAttribute("formBean", formBean);

                return "publicacionEditar";
            }

            try {
            	
            	if(formBean.getId() == null) {
                    formBean.setEstado(EstadoPublicacion.ACTIVA);
                }

                Publicacion p = formBean.toPojo();

                p.setPropiedad(
                        servicioPropiedad.getById(formBean.getIdPropiedad()));

                servicioPublicacion.save(p);

                return "redirect:/publicacionesBuscar";

            }
            catch (Excepcion e) {

                if(e.getAtributo()==null) {

                    ObjectError error =
                            new ObjectError("globalError",e.getMessage());

                    result.addError(error);

                } else {

                    FieldError error =
                            new FieldError(
                                    "formBean",
                                    e.getAtributo(),
                                    e.getMessage());

                    result.addError(error);

                }

                modelo.addAttribute("formBean", formBean);

                return "publicacionEditar";
            }

        }

        else if(action.equals("actionCancelar")) {

            modelo.clear();

            return "redirect:/publicacionesBuscar";

        }

        return "redirect:/";
    }

}
