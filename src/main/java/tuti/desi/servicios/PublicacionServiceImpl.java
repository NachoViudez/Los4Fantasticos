package tuti.desi.servicios;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tuti.desi.accesoDatos.HistorialEstadoPublicacionRepo;
import tuti.desi.accesoDatos.PublicacionRepo;
import tuti.desi.entidades.EstadoDisponibilidad;
import tuti.desi.entidades.EstadoPublicacion;
import tuti.desi.entidades.HistorialEstadoPublicacion;
import tuti.desi.entidades.Publicacion;
import tuti.desi.excepciones.EntidadNoEncontradaException;
import tuti.desi.excepciones.Excepcion;

@Service
public class PublicacionServiceImpl implements PublicacionService {

	 @Autowired
	    private PublicacionRepo repo;

	    @Autowired
	    private HistorialEstadoPublicacionRepo historialRepo;

	    @Override
	    public List<Publicacion> getAll() {
	        return repo.findByEliminadoFalse();
	    }

	    @Override
	    public Publicacion getById(Long idPublicacion) {

	        return repo.findByIdAndEliminadoFalse(idPublicacion)
	                .orElseThrow(() ->
	                        new EntidadNoEncontradaException("la publicación", idPublicacion));
	    }

	    @Override
	    public void deleteById(Long id) throws Excepcion {

	        Publicacion p = getById(id);

	        p.setEliminado(true);

	        repo.save(p);
	    }

	    @Override
	    public void save(Publicacion p) throws Excepcion {

	        // =====================================
	        // ALTA
	        // =====================================

	        if (p.getId() == null) {

	            // Solo puede existir una publicación ACTIVA por propiedad
	            if (p.getEstado() == EstadoPublicacion.ACTIVA &&
	                    repo.existsByPropiedadIdAndEstadoAndEliminadoFalse(
	                            p.getPropiedad().getId(),
	                            EstadoPublicacion.ACTIVA)) {

	                throw new Excepcion("La propiedad ya posee una publicación activa.");
	            }

	            // Solo puede activarse una publicación si la propiedad está disponible
	            if (p.getEstado() == EstadoPublicacion.ACTIVA &&
	                    p.getPropiedad().getEstadoDisponibilidad() != EstadoDisponibilidad.DISPONIBLE) {

	                throw new Excepcion("Solo pueden activarse publicaciones de propiedades disponibles.");
	            }

	            p.setEliminado(false);

	            Publicacion guardada = repo.save(p);

	            guardarHistorial(guardada);
	        }

	        // =====================================
	        // EDICIÓN
	        // =====================================

	        else {

	            Publicacion actual = getById(p.getId());

	            // La propiedad no puede modificarse
	            if (!actual.getPropiedad().getId().equals(p.getPropiedad().getId())) {
	                throw new Excepcion("No puede modificarse la propiedad asociada.");
	            }

	            // No puede haber otra publicación activa para la misma propiedad
	            if (p.getEstado() == EstadoPublicacion.ACTIVA &&
	                    repo.existsByPropiedadIdAndEstadoAndEliminadoFalseAndIdNot(
	                            p.getPropiedad().getId(),
	                            EstadoPublicacion.ACTIVA,
	                            p.getId())) {

	                throw new Excepcion("La propiedad ya posee otra publicación activa.");
	            }

	            // Solo puede activarse si la propiedad está disponible
	            if (p.getEstado() == EstadoPublicacion.ACTIVA &&
	                    p.getPropiedad().getEstadoDisponibilidad() != EstadoDisponibilidad.DISPONIBLE) {

	                throw new Excepcion("Solo pueden activarse publicaciones de propiedades disponibles.");
	            }

	            // Las condiciones no pueden modificarse si la publicación está finalizada
	            if (actual.getEstado() == EstadoPublicacion.FINALIZADA &&
	                    !Objects.equals(actual.getCondiciones(), p.getCondiciones())) {

	                throw new Excepcion("No pueden modificarse las condiciones de una publicación finalizada.");
	            }
	            

	            boolean cambioEstado = actual.getEstado() != p.getEstado();

	            Publicacion guardada = repo.save(p);

	            if (cambioEstado) {
	                guardarHistorial(guardada);
	            }
	        }
	    }

	    private void guardarHistorial(Publicacion publicacion) {

	        HistorialEstadoPublicacion historial = new HistorialEstadoPublicacion();

	        historial.setPublicacion(publicacion);
	        historial.setEstado(publicacion.getEstado());
	        historial.setFechaHora(LocalDateTime.now());

	        historialRepo.save(historial);
	    }
}
