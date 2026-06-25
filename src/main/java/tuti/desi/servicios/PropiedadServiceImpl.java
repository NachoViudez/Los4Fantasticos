package tuti.desi.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import tuti.desi.accesoDatos.PropiedadRepo;
import tuti.desi.entidades.Propiedad;
import tuti.desi.excepciones.Excepcion;
import tuti.desi.presentacion.propiedades.PropiedadesBuscarForm;

public class PropiedadServiceImpl implements PropiedadService {
	
	@Autowired
	PropiedadRepo pRepo;

	@Override
	public List<Propiedad> getAllActivas() {
		return pRepo.findAll();
	}

	@Override
	public Propiedad getById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Propiedad> filtrar(PropiedadesBuscarForm filtrar) throws Excepcion {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void guardar(Propiedad propiedad) throws Excepcion {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eliminarById(Long id) {
		// TODO Auto-generated method stub
		
	}

}
