package tuti.desi.servicios;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import tuti.desi.accesoDatos.ContratoRepo;
import tuti.desi.accesoDatos.HistorialEstadoPropiedadRepo;
import tuti.desi.accesoDatos.PropiedadRepo;
import tuti.desi.entidades.EstadoContrato;
import tuti.desi.entidades.EstadoDisponibilidad;
import tuti.desi.entidades.HistorialEstadoPropiedad;
import tuti.desi.entidades.Propiedad;
import tuti.desi.excepciones.EntidadNoEncontradaException;
import tuti.desi.excepciones.Excepcion;
import tuti.desi.presentacion.propiedades.PropiedadesBuscarForm;

public class PropiedadServiceImpl implements PropiedadService {
	
	@Autowired
	PropiedadRepo pRepo;
	@Autowired
	HistorialEstadoPropiedadRepo historialRepo;
	@Autowired
	ContratoRepo contratoRepo;

	//traigo las propiedades que no esten eliminadas(eliminacion logica)
	@Override
	public List<Propiedad> getAllActivas() {
		return pRepo.findByEliminadoFalse();
	}

	//busco las propiedades por id
	@Override
	public Propiedad getById(Long idPropiedad) {
		return pRepo.findById(idPropiedad).orElseThrow(() -> new EntidadNoEncontradaException("la propiedad", idPropiedad));
	}
	
	//como en el repo ya filtro los nulos no hace falta que los filtre aca, asi que solo evaulo cuando hay algun filtro
	@Override
	public List<Propiedad> filtrar(PropiedadesBuscarForm filtrar) throws Excepcion {
	    return pRepo.filtrar(filtrar.getDireccion(), filtrar.getCiudadSeleccionada(), filtrar.getTipoSeleccionado(),filtrar.getEstadoSeleccionado());
	}

	@Override
	public void guardar(Propiedad propiedad) throws Excepcion {
		//primero verifico si es una propiedad nueva o no
		if(propiedad.getId() == null) {
			//responde al alta de HU, si el usuario no elige otro estado queda disponible
			if(propiedad.getEstadoDisponibilidad() == null) {
				propiedad.setEstadoDisponibilidad(EstadoDisponibilidad.DISPONIBLE);
			}
			//responde a alta de HU, setea cuando es nueva el eliminado en false
			propiedad.setEliminado(false);
			//responde a alta de HU, uso la query que cree en repo para validar si existe mas de una propiedad con misma direccion y ciudad
			if(pRepo.existsByDireccionAndCiudadIdAndEliminadoFalse(propiedad.getDireccion(), propiedad.getCiudad().getId())) {
				throw new Excepcion("Ya existe una propiedad en esa ciudad con esa direccion");
			}
			Propiedad propiedadGuardada = pRepo.save(propiedad);
			guardarHistorial(propiedadGuardada);
		}else {// si no es nueva la modifico
			
			Propiedad estaPropiedad = getById(propiedad.getId());
			if(estaPropiedad.getEliminado() == true) {
				throw new Excepcion("Esa propiedad no se puede modificar porque fue eliminada");
			}
			//responde a modificacion de HU, no pueden quedar dos propiedades con en la misma ciudad con la misma direccion, evita duplicados
			if(pRepo.existsByDireccionAndCiudadIdAndIdNotAndEliminadoFalse(propiedad.getDireccion(),propiedad.getCiudad().getId(), propiedad.getId())) {
				throw new Excepcion("Ya existe una propiedad en esa ciudad con esa direccion");
			}
			//defino una variable que sirve para validar si la propiedad tiene un contrato activo
			boolean contratoEstaActivo = contratoRepo.existsByPropiedadIdAndEstadoAndEliminadoFalse(propiedad.getId(), EstadoContrato.ACTIVO);
			//responde a Modificacion HU, si tiene un contrato activo, no se puede cambiar el estado a disponible o a inactivo
			if(contratoEstaActivo && (propiedad.getEstadoDisponibilidad() == EstadoDisponibilidad.DISPONIBLE || propiedad.getEstadoDisponibilidad() == EstadoDisponibilidad.INACTIVA )) {
				throw new Excepcion("No se puede cambiar el estado de la propiedad porque tiene un contrato activo");
			}
			//verifico que el estado haya cambiado o no
			boolean cambioEstado = estaPropiedad.getEstadoDisponibilidad() != propiedad.getEstadoDisponibilidad();
			propiedad.setEliminado(false);
			pRepo.save(propiedad);
			
			if(cambioEstado) {
				guardarHistorial(propiedad);
			}
		}
		
	}
	
	//creo un meotodo para guardar el estado de la propiedad en alta y en modificacion
	private void guardarHistorial(Propiedad propiedad) {
		HistorialEstadoPropiedad historial = new HistorialEstadoPropiedad();
		historial.setPropiedad(propiedad);
		historial.setEstado(propiedad.getEstadoDisponibilidad());
		historial.setFechaHora(LocalDateTime.now());
		
	}
	
	@Override
	public void eliminarById(Long id) throws Excepcion {
		//traigo la propiedad que corresponde al id pasado
		Propiedad propiedad = getById(id);
		if(contratoRepo.existsByPropiedadIdAndEstadoAndEliminadoFalse(propiedad.getId(), EstadoContrato.ACTIVO)) {
			throw new Excepcion("No se puede eliminar la propiedad porque tiene un contrato activo");
		}else {
			propiedad.setEliminado(true);
			propiedad.setEstadoDisponibilidad(EstadoDisponibilidad.INACTIVA);
			pRepo.save(propiedad);
			guardarHistorial(propiedad);
			
		}
		
	}

}
