package tuti.desi.accesoDatos;
import java.util.Optional;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

	    @Query("""
	        SELECT p
	        FROM Publicacion p
	        WHERE p.eliminado = false
	          AND (:idPropiedad IS NULL OR p.propiedad.id = :idPropiedad)
	          AND (:idCiudad IS NULL OR p.propiedad.ciudad.id = :idCiudad)
	          AND (:estado IS NULL OR p.estado = :estado)
	          AND (:precioMin IS NULL OR p.precioMensual >= :precioMin)
	          AND (:precioMax IS NULL OR p.precioMensual <= :precioMax)
	    """)
	    List<Publicacion> filter(
	            @Param("idPropiedad") Long idPropiedad,
	            @Param("idCiudad") Long idCiudad,
	            @Param("estado") EstadoPublicacion estado,
	            @Param("precioMin") BigDecimal precioMin,
	            @Param("precioMax") BigDecimal precioMax);
}
