package tuti.desi.accesoDatos;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tuti.desi.entidades.EstadoFactura;
import tuti.desi.entidades.Factura;

public interface FacturaRepo extends JpaRepository<Factura, Long> {

    List<Factura> findByEliminadoFalse();

    List<Factura> findByEstadoAndEliminadoFalse(EstadoFactura estado);

    List<Factura> findByContratoIdAndEliminadoFalse(Long contratoId);

    @Query("""
            SELECT f
            FROM Factura f
            WHERE f.eliminado = false
              AND (:contratoId IS NULL OR f.contrato.id = :contratoId)
              AND (:estado IS NULL OR f.estado = :estado)
              AND (:fechaDesde IS NULL OR f.fechaVencimiento >= :fechaDesde)
              AND (:fechaHasta IS NULL OR f.fechaVencimiento <= :fechaHasta)
            """)
    List<Factura> buscarConFiltros(
            @Param("contratoId") Long contratoId,
            @Param("estado") EstadoFactura estado,
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta
    );
}