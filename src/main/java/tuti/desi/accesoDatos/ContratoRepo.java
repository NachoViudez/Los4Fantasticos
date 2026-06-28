package tuti.desi.accesoDatos;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tuti.desi.entidades.Contrato;
import tuti.desi.entidades.EstadoContrato;

@Repository
public interface ContratoRepo extends JpaRepository<Contrato, Long> {
	@Query("""
		    SELECT c FROM Contrato c
		    WHERE c.eliminado = false
		      AND (:idPropiedadSeleccionada IS NULL OR c.propiedad.id = :idPropiedadSeleccionada)
		      AND (:idInquilinoSeleccionado IS NULL OR c.inquilino.id = :idInquilinoSeleccionado)
		      AND (:estado IS NULL OR c.estado = :estado)
		      AND (:fechaInicio IS NULL OR c.fechaInicio = :fechaInicio)
		""")
	List<Contrato> filter(Long idPropiedadSeleccionada, Long idInquilinoSeleccionado, EstadoContrato estado, LocalDate fechaInicio);
	
	@Query("""
		    SELECT c FROM Contrato c
		    WHERE c.eliminado = false
		      AND c.propiedad.id = :idPropiedadSeleccionada
		      AND c.estado = :estado
		""")
	List<Contrato> findByPropiedadAndEstado(Long idPropiedadSeleccionada, EstadoContrato estado);
	
	/**
	 * Busca contratos activos para una propiedad, excluyendo el contrato indicado.
	 * Sirve para validar que no exista otro contrato activo sobre la misma propiedad al editar.
	 * @param idPropiedadSeleccionada
	 * @param estado
	 * @param idDistintoDe
	 * @return
	 */
	@Query("""
		    SELECT c FROM Contrato c
		    WHERE c.eliminado = false
		      AND c.propiedad.id = :idPropiedadSeleccionada
		      AND c.estado = :estado
		      AND c.id <> :idDistintoDe
		""")
	List<Contrato> findOtroContratoByPropiedadAndEstado(Long idPropiedadSeleccionada, EstadoContrato estado, Long idDistintoDe);
}