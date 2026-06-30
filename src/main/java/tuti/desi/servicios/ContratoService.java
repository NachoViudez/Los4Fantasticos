package tuti.desi.servicios;

import java.util.List;

import tuti.desi.entidades.Contrato;
import tuti.desi.entidades.EstadoContrato;
import tuti.desi.entidades.Persona;
import tuti.desi.entidades.Propiedad;
import tuti.desi.excepciones.Excepcion;
import tuti.desi.presentacion.contratos.ContratoForm;
import tuti.desi.presentacion.contratos.ContratosBuscarForm;

public interface ContratoService {

	List<Contrato> buscar(ContratosBuscarForm form);

	Contrato buscarPorId(Long id);

	void guardar(ContratoForm form) throws Excepcion;

	void eliminar(Long id) throws Excepcion;

	List<Propiedad> buscarPropiedades();

	List<Persona> buscarInquilinos();

	EstadoContrato[] buscarEstados();
}