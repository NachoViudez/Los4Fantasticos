package tuti.desi.servicios;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

        // Solo pueden eliminarse publicaciones activas
        if (p.getEstado() != EstadoPublicacion.ACTIVA) {
            throw new Excepcion("Solo pueden eliminarse publicaciones activas.");
        }

        p.setEliminado(true);

        repo.save(p);
    }

    @Override
    public void save(Publicacion p) throws Excepcion {

        // Validar precio
        if (p.getPrecioMensual() == null ||
                p.getPrecioMensual().compareTo(BigDecimal.ZERO) <= 0) {
            throw new Excepcion("El precio mensual debe ser mayor a cero.");
        }

        // Validar propiedad
        if (p.getPropiedad() == null) {
            throw new Excepcion("Debe seleccionar una propiedad.");
        }

        // Solo propiedades disponibles
        if (p.getPropiedad().getEstadoDisponibilidad() != EstadoDisponibilidad.DISPONIBLE) {
            throw new Excepcion("Solo se pueden publicar propiedades disponibles.");
        }

        if (p.getId() == null) {

            // Alta
            if (repo.existsByPropiedadIdAndEstadoAndEliminadoFalse(
                    p.getPropiedad().getId(),
                    EstadoPublicacion.ACTIVA)) {

                throw new Excepcion("La propiedad ya posee una publicación activa.");
            }

            // Valores por defecto
            p.setEstado(EstadoPublicacion.ACTIVA);
            p.setFechaPublicacion(LocalDate.now());
            p.setEliminado(false);

            Publicacion guardada = repo.save(p);
            guardarHistorial(guardada);

        } else {

            // Obtengo la publicación actual
            Publicacion actual = getById(p.getId());

            // No permitir otra publicación activa para la misma propiedad
            if (repo.existsByPropiedadIdAndEstadoAndEliminadoFalseAndIdNot(
                    p.getPropiedad().getId(),
                    EstadoPublicacion.ACTIVA,
                    p.getId())) {

                throw new Excepcion("La propiedad ya posee otra publicación activa.");
            }

            // Detectar cambio de estado
            boolean cambioEstado = actual.getEstado() != p.getEstado();

            p.setEliminado(false);

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
