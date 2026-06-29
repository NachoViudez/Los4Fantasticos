package tuti.desi.servicios;
import java.util.List;

import tuti.desi.entidades.Publicacion;
import tuti.desi.excepciones.Excepcion;
import tuti.desi.presentacion.publicaciones.PublicacionBuscarForm;

public interface PublicacionService {
/*Es una interfaz
 * Estas son las operaciones que una publicación puede hacer.*/
	
	List<Publicacion> getAll();

	List<Publicacion> filter(PublicacionBuscarForm filter) throws Excepcion;
	
	Publicacion getById(Long idPublicacion);

	void deleteById(Long id) throws Excepcion;

	void save(Publicacion p) throws Excepcion;
}
