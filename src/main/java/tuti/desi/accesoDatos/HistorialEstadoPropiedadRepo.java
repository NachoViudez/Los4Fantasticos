package tuti.desi.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;

import tuti.desi.entidades.HistorialEstadoContrato;

public interface HistorialEstadoPropiedadRepo extends JpaRepository<HistorialEstadoContrato, Long> {

}
