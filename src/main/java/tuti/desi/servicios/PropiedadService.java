package tuti.desi.servicios;

import java.util.List;

import tuti.desi.entidades.Propiedad;
import tuti.desi.excepciones.Excepcion;
import tuti.desi.presentacion.propiedades.PropiedadesBuscarForm;

public interface PropiedadService {

    List<Propiedad> getAllActivas();

    Propiedad getById(Long id);

    List<Propiedad> filtrar(PropiedadesBuscarForm filtrar) throws Excepcion;

    void guardar(Propiedad propiedad) throws Excepcion;

    void eliminarById(Long id) throws Excepcion;
}
