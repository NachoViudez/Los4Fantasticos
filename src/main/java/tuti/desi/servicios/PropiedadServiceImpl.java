package tuti.desi.servicios;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

@Service
public class PropiedadServiceImpl implements PropiedadService {

	@Autowired
	private PropiedadRepo pRepo;

	@Autowired
	private HistorialEstadoPropiedadRepo historialRepo;

	@Autowired
	private ContratoRepo contratoRepo;

	// Traigo las propiedades que no estén eliminadas (eliminación lógica)
	@Override
	public List<Propiedad> getAllActivas() {
		return pRepo.findByEliminadoFalse();
	}

	// Busco una propiedad por id
	@Override
	public Propiedad getById(Long idPropiedad) {
		return pRepo.findById(idPropiedad)
				.orElseThrow(() -> new EntidadNoEncontradaException("la propiedad", idPropiedad));
	}

	// Filtro de búsqueda
	@Override
	public List<Propiedad> filtrar(PropiedadesBuscarForm filtrar) throws Excepcion {
		return pRepo.filtrar(
				filtrar.getDireccion(),
				filtrar.getCiudadSeleccionada(),
				filtrar.getTipoSeleccionado(),
				filtrar.getEstadoSeleccionado());
	}

	@Override
	public void guardar(Propiedad propiedad) throws Excepcion {

		// Alta
		if (propiedad.getId() == null) {

			// Estado por defecto
			if (propiedad.getEstadoDisponibilidad() == null) {
				propiedad.setEstadoDisponibilidad(EstadoDisponibilidad.DISPONIBLE);
			}

			propiedad.setEliminado(false);

			// No permitir duplicados
			if (pRepo.existsByDireccionAndCiudadIdAndEliminadoFalse(
					propiedad.getDireccion(),
					propiedad.getCiudad().getId())) {

				throw new Excepcion("Ya existe una propiedad en esa ciudad con esa direccion");
			}

			Propiedad propiedadGuardada = pRepo.save(propiedad);

			guardarHistorial(propiedadGuardada);

		} else {

			// Modificación
			Propiedad estaPropiedad = getById(propiedad.getId());

			if (estaPropiedad.getEliminado()) {
				throw new Excepcion("Esa propiedad no se puede modificar porque fue eliminada");
			}

			// Evitar duplicados
			if (pRepo.existsByDireccionAndCiudadIdAndIdNotAndEliminadoFalse(
					propiedad.getDireccion(),
					propiedad.getCiudad().getId(),
					propiedad.getId())) {

				throw new Excepcion("Ya existe una propiedad en esa ciudad con esa direccion");
			}

			// Verificar contrato activo
			boolean contratoEstaActivo =
					contratoRepo.existsByPropiedadIdAndEstadoAndEliminadoFalse(
							propiedad.getId(),
							EstadoContrato.ACTIVO);

			if (contratoEstaActivo &&
					(propiedad.getEstadoDisponibilidad() == EstadoDisponibilidad.DISPONIBLE
					|| propiedad.getEstadoDisponibilidad() == EstadoDisponibilidad.INACTIVA)) {

				throw new Excepcion(
						"No se puede cambiar el estado de la propiedad porque tiene un contrato activo");
			}

			boolean cambioEstado =
					estaPropiedad.getEstadoDisponibilidad() != propiedad.getEstadoDisponibilidad();

			propiedad.setEliminado(false);

			pRepo.save(propiedad);

			if (cambioEstado) {
				guardarHistorial(propiedad);
			}
		}
	}

	// Guarda el historial de cambios de estado
	private void guardarHistorial(Propiedad propiedad) {

		HistorialEstadoPropiedad historial = new HistorialEstadoPropiedad();

		historial.setPropiedad(propiedad);
		historial.setEstado(propiedad.getEstadoDisponibilidad());
		historial.setFechaHora(LocalDateTime.now());

		historialRepo.save(historial);
	}

	@Override
	public void eliminarById(Long id) throws Excepcion {

		// Traigo la propiedad correspondiente
		Propiedad propiedad = getById(id);

		// No puede eliminarse si tiene un contrato activo
		if (contratoRepo.existsByPropiedadIdAndEstadoAndEliminadoFalse(
				propiedad.getId(),
				EstadoContrato.ACTIVO)) {

			throw new Excepcion(
					"No se puede eliminar la propiedad porque tiene un contrato activo");
		}

		// Eliminación lógica
		propiedad.setEliminado(true);

		// Se marca como inactiva
		propiedad.setEstadoDisponibilidad(EstadoDisponibilidad.INACTIVA);

		pRepo.save(propiedad);

		// Se registra el cambio de estado
		guardarHistorial(propiedad);
	}
}
