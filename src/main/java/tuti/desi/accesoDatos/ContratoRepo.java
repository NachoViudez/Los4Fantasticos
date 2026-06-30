package tuti.desi.accesoDatos;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tuti.desi.entidades.Contrato;
import tuti.desi.entidades.EstadoContrato;

@Repository
public interface ContratoRepo extends JpaRepository<Contrato, Long> {

	boolean existsByPropiedadIdAndEstadoAndEliminadoFalse(Long idPropiedad, EstadoContrato estado);

	List<Contrato> findByEstadoAndEliminadoFalse(EstadoContrato estado);

	Optional<Contrato> findByIdAndEstadoAndEliminadoFalse(Long id, EstadoContrato estado);

	@Query("""
		    SELECT c FROM Contrato c
		    WHERE c.eliminado = false
		      AND (:idPropiedadSeleccionada IS NULL OR c.propiedad.id = :idPropiedadSeleccionada)
		      AND (:idInquilinoSeleccionado IS NULL OR c.inquilino.id = :idInquilinoSeleccionado)
		      AND (:estado IS NULL OR c.estado = :estado)
		      AND (:fechaInicio IS NULL OR c.fechaInicio = :fechaInicio)
		""")
	List<Contrato> filter(
			@Param("idPropiedadSeleccionada") Long idPropiedadSeleccionada,
			@Param("idInquilinoSeleccionado") Long idInquilinoSeleccionado,
			@Param("estado") EstadoContrato estado,
			@Param("fechaInicio") LocalDate fechaInicio);

	@Query("""
		    SELECT c FROM Contrato c
		    WHERE c.eliminado = false
		      AND c.propiedad.id = :idPropiedadSeleccionada
		      AND c.estado = :estado
		""")
	List<Contrato> findByPropiedadAndEstado(
			@Param("idPropiedadSeleccionada") Long idPropiedadSeleccionada,
			@Param("estado") EstadoContrato estado);

	@Query("""
		    SELECT c FROM Contrato c
		    WHERE c.eliminado = false
		      AND c.propiedad.id = :idPropiedadSeleccionada
		      AND c.estado = :estado
		      AND c.id <> :idDistintoDe
		""")
	List<Contrato> findOtroContratoByPropiedadAndEstado(
			@Param("idPropiedadSeleccionada") Long idPropiedadSeleccionada,
			@Param("estado") EstadoContrato estado,
			@Param("idDistintoDe") Long idDistintoDe);
}