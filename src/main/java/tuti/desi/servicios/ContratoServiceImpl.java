package tuti.desi.servicios;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tuti.desi.accesoDatos.ContratoRepo;
import tuti.desi.accesoDatos.HistorialEstadoContratoRepo;
import tuti.desi.accesoDatos.PersonaRepo;
import tuti.desi.accesoDatos.PropiedadRepo;
import tuti.desi.entidades.Contrato;
import tuti.desi.entidades.EstadoContrato;
import tuti.desi.entidades.EstadoDisponibilidad;
import tuti.desi.entidades.HistorialEstadoContrato;
import tuti.desi.entidades.Persona;
import tuti.desi.entidades.Propiedad;
import tuti.desi.excepciones.EntidadNoEncontradaException;
import tuti.desi.excepciones.Excepcion;
import tuti.desi.presentacion.contratos.ContratoForm;
import tuti.desi.presentacion.contratos.ContratosBuscarForm;
import java.math.BigDecimal;

@Service
public class ContratoServiceImpl implements ContratoService {
	@Autowired
	ContratoRepo repo;
	@Autowired
	PropiedadRepo propiedadRepo;
	@Autowired
	PersonaRepo personaRepo;
	@Autowired
	HistorialEstadoContratoRepo historialRepo;
	
	@Override
	public List<Contrato> buscar(ContratosBuscarForm form) {
		return repo.filter(
				form.getIdPropiedadSeleccionada(),
				form.getIdInquilinoSeleccionado(),
				form.getEstado(),
				form.getFechaInicio());
	}

	@Override
	public Contrato buscarPorId(Long id) {
		return repo.findById(id)
				.orElseThrow(() -> new EntidadNoEncontradaException("el contrato", id));
	}

	@Override
	public void guardar(ContratoForm form) throws Excepcion {	
		//validar que los campos esten completos
		if (form.getFechaInicio() == null)
		    throw new Excepcion("La fecha de inicio es obligatoria", "fechaInicio");

		if (form.getDuracionMeses() == null)
		    throw new Excepcion("La duración es obligatoria", "duracionMeses");

		if (form.getImporteMensual() == null)
		    throw new Excepcion("El importe mensual es obligatorio", "importeMensual");

		if (form.getDiaVencimientoMensual() == null)
		    throw new Excepcion("El día de vencimiento es obligatorio", "diaVencimientoMensual");
		
		Propiedad propiedad = propiedadRepo.findById(form.getIdPropiedadSeleccionada())
				.orElseThrow(() -> new EntidadNoEncontradaException("la propiedad", form.getIdPropiedadSeleccionada()));
		
		Persona inquilino = personaRepo.findById(form.getIdInquilinoSeleccionado())
				.orElseThrow(() -> new EntidadNoEncontradaException("el inquilino", form.getIdInquilinoSeleccionado()));
		
		Contrato contrato;
		EstadoContrato estadoAnterior = null;
		
		if(form.getId() == null) {
			contrato = new Contrato();
			contrato.setEliminado(false);
		} else {
			contrato = buscarPorId(form.getId());
			estadoAnterior = contrato.getEstado();
		}
		//validacion para que un contrato dno pueda volver de rescindido o finalizado a activo
		if (estadoAnterior != null
		        && (estadoAnterior == EstadoContrato.FINALIZADO
		            || estadoAnterior == EstadoContrato.RESCINDIDO)
		        && form.getEstado() == EstadoContrato.ACTIVO) {
		    throw new Excepcion("No se puede cambiar un contrato finalizado o rescindido nuevamente a Activo.");
		}
		
		validarContrato(form, propiedad);
		contrato.setPropiedad(propiedad);
		contrato.setInquilino(inquilino);
		contrato.setFechaInicio(form.getFechaInicio());
		contrato.setDuracionMeses(form.getDuracionMeses());
		contrato.setImporteMensual(form.getImporteMensual());
		contrato.setDiaVencimientoMensual(form.getDiaVencimientoMensual());
		contrato.setDescripcion(form.getDescripcion());
		contrato.setEstado(form.getEstado());
		
		actualizarEstadoPropiedad(contrato);
		
		repo.save(contrato);
		if(estadoAnterior == null || estadoAnterior != contrato.getEstado()) {
			guardarHistorialEstado(contrato);
		}
	}

	@Override
	public void eliminar(Long id) throws Excepcion {
		
		Contrato contrato = buscarPorId(id);
		
		if(contrato.getEstado() != EstadoContrato.BORRADOR) {
			throw new Excepcion("Solo se pueden eliminar contratos en estado borrador");
		}
		
		contrato.setEliminado(true);
		repo.save(contrato);
	}

	@Override
	public List<Propiedad> buscarPropiedades() {
		return propiedadRepo.findByEliminadoFalse();
	}

	@Override
	public List<Persona> buscarInquilinos() {
		return personaRepo.findAll();
	}

	@Override
	public EstadoContrato[] buscarEstados() {
		return EstadoContrato.values();
	}
	
	private void validarContrato(ContratoForm form, Propiedad propiedad) throws Excepcion {
		if(form.getEstado() == EstadoContrato.ACTIVO) {
			if(form.getId() == null && propiedad.getEstadoDisponibilidad() != EstadoDisponibilidad.DISPONIBLE) {
				throw new Excepcion("No se puede activar el contrato porque la propiedad no está disponible");
			}
			if(form.getId() != null) {
				Contrato contratoActual = buscarPorId(form.getId());
				if(contratoActual.getEstado() != EstadoContrato.ACTIVO 
						&& propiedad.getEstadoDisponibilidad() != EstadoDisponibilidad.DISPONIBLE) {
					throw new Excepcion("No se puede activar el contrato porque la propiedad no está disponible");
				}
			}
			if(form.getId() == null 
					&& !repo.findByPropiedadAndEstado(form.getIdPropiedadSeleccionada(), EstadoContrato.ACTIVO).isEmpty()) {
				throw new Excepcion("La propiedad ya tiene un contrato activo");
			}
			if(form.getId() != null 
					&& !repo.findOtroContratoByPropiedadAndEstado(form.getIdPropiedadSeleccionada(), EstadoContrato.ACTIVO, form.getId()).isEmpty()) {
				throw new Excepcion("Existe otro contrato activo para la misma propiedad");
			}
		}
		//validar numeros negativos
		if (form.getDuracionMeses() != null && form.getDuracionMeses() <= 0) {
		    throw new Excepcion("La duración del contrato debe ser mayor a 0.");
		}

		if (form.getImporteMensual() != null && form.getImporteMensual().compareTo(BigDecimal.ZERO) <= 0) {
		    throw new Excepcion("El importe mensual debe ser mayor a 0.");
		}

		if (form.getDiaVencimientoMensual() != null && form.getDiaVencimientoMensual() <= 0) {
		    throw new Excepcion("El día de vencimiento debe ser mayor a 0.");
		}
	}
	
	private void actualizarEstadoPropiedad(Contrato contrato) {	
		if(contrato.getEstado() == EstadoContrato.ACTIVO) {
			contrato.getPropiedad().setEstadoDisponibilidad(EstadoDisponibilidad.ALQUILADA);
			propiedadRepo.save(contrato.getPropiedad());
		}
		
		if(contrato.getEstado() == EstadoContrato.FINALIZADO 
				|| contrato.getEstado() == EstadoContrato.RESCINDIDO) {
			contrato.getPropiedad().setEstadoDisponibilidad(EstadoDisponibilidad.DISPONIBLE);
			propiedadRepo.save(contrato.getPropiedad());
		}
	}
	
	private void guardarHistorialEstado(Contrato contrato) {	
		HistorialEstadoContrato historial = new HistorialEstadoContrato();
		historial.setContrato(contrato);
		historial.setEstado(contrato.getEstado());
		historial.setFechaHora(LocalDateTime.now());
		historialRepo.save(historial);
	}
}