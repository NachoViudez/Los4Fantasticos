package tuti.desi.accesoDatos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tuti.desi.entidades.Propiedad;
import tuti.desi.entidades.TipoPropiedad;
import tuti.desi.entidades.EstadoDisponibilidad;
@Repository
public interface PropiedadRepo extends JpaRepository<Propiedad, Long> {

	@Query("""
		    SELECT p FROM Propiedad p
		    WHERE p.eliminado = false
		      AND (:direccion IS NULL OR LOWER(p.direccion) LIKE LOWER(CONCAT('%', :direccion, '%')))
		      AND (:idCiudad IS NULL OR p.ciudad.id = :idCiudad)
		      AND (:tipo IS NULL OR p.tipo = :tipo)
		      AND (:estado IS NULL OR p.estadoDisponibilidad = :estado)
		""")
	List<Propiedad> filtrar(
			@Param("direccion") String direccion,
			@Param("idCiudad") Long idCiudad,
			@Param("tipo") TipoPropiedad tipo,
			@Param("estado") EstadoDisponibilidad estado);
	
	boolean existsByDireccionAndCiudadIdAndEliminadoFalse(String direccion, Long idCiudad);
	
	boolean existsByDireccionAndCiudadIdAndIdNotAndEliminadoFalse(String direccion, Long idCiudad, Long id);
		


	
}
