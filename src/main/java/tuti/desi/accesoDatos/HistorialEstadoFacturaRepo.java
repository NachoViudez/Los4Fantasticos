package tuti.desi.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;

import tuti.desi.entidades.HistorialEstadoFactura;

public interface HistorialEstadoFacturaRepo extends JpaRepository<HistorialEstadoFactura, Long> {
}