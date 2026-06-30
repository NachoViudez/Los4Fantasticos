package tuti.desi.accesoDatos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tuti.desi.entidades.Contrato;
import tuti.desi.entidades.EstadoContrato;

public interface ContratoRepo extends JpaRepository<Contrato, Long> {

    boolean existsByPropiedadIdAndEstadoAndEliminadoFalse(
            Long idPropiedad,
            EstadoContrato estado);

    List<Contrato> findByEstadoAndEliminadoFalse(EstadoContrato estado);

    Optional<Contrato> findByIdAndEstadoAndEliminadoFalse(Long id, EstadoContrato estado);
}


