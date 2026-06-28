package tuti.desi.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;

import tuti.desi.entidades.EstadoContrato;
import tuti.desi.entidades.Contrato;

public interface ContratoRepo extends JpaRepository<Contrato, Long> {
	
	boolean existsByPropiedadIdAndEstadoAndEliminadoFalse(Long idPropiedad, EstadoContrato estado);
	

}
