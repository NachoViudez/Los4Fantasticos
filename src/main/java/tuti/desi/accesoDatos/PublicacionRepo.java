package tuti.desi.accesoDatos;
import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tuti.desi.entidades.EstadoPublicacion;
import tuti.desi.entidades.Publicacion;

@Repository
public interface PublicacionRepo extends JpaRepository<Publicacion, Long> {

    boolean existsByPropiedadIdAndEstadoAndEliminadoFalse(
            Long idPropiedad,
            EstadoPublicacion estado);
    
    boolean existsByPropiedadIdAndEstadoAndEliminadoFalseAndIdNot(
            Long idPropiedad,
            EstadoPublicacion estado,
            Long id);
    Optional<Publicacion> findByIdAndEliminadoFalse(Long id);
    
    List<Publicacion> findByEliminadoFalse();
}
