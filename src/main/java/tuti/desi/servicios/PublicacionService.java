package tuti.desi.servicios;
import java.util.List;

import tuti.desi.entidades.Publicacion;
import tuti.desi.excepciones.Excepcion;

public interface PublicacionService {
/*Es una interfaz
 * Estas son las operaciones que una publicación puede hacer.*/
	
	List<Publicacion> getAll();

	Publicacion getById(Long idPublicacion);

	void deleteById(Long id) throws Excepcion;

	void save(Publicacion p) throws Excepcion;
}
