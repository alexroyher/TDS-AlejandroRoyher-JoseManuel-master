package umu.tds.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import beans.Entidad;
import beans.Propiedad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.dominio.Cancion;
import umu.tds.dominio.EstiloMusical;
import umu.tds.dominio.Interprete;

public class TDSCancionDAO implements CancionDAO {
	
	private final String CANCION = "cancion";
	private final String TITULO = "titulo";
	private final String RUTAFICHERO = "rutafichero";
	private final String NUMRERP = "numrep";
	private final String ESTILO = "estilo";
	private final String INTERPRETE = "interprete";
	
	private ServicioPersistencia servPersistencia;
	private PoolDAO poolDAO;
	
	public TDSCancionDAO() {
		this.servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
		this.poolDAO = PoolDAO.getInstance();
	}
	
	private Entidad cancionToEntidadRegistrar(Cancion cancion) {
		Entidad eCancion = new Entidad();
		
		InterpreteDAO adaptadorInterprete = null;
		EstiloMusicalDAO adaptadorEstiloMusical = null;
		try {
			 adaptadorInterprete = FactoriaDAO.getInstancia().getInterpreteDAO();
		} catch (DAOException e) {
			e.printStackTrace();
		}
		
		try {
			 adaptadorEstiloMusical = FactoriaDAO.getInstancia().getEstiloMusicalDAO();
		} catch (DAOException e) {
			e.printStackTrace();
		}
		
		adaptadorEstiloMusical.create(cancion.getEstiloMusical());
		
		eCancion.setNombre(CANCION);
		eCancion.setPropiedades(new ArrayList<Propiedad>(Arrays.asList(
				new Propiedad(TITULO, cancion.getTitulo()),
				new Propiedad(RUTAFICHERO, cancion.getRutaFichero()),
				new Propiedad(NUMRERP, String.valueOf(cancion.getNumReproducciones())),
				new Propiedad(ESTILO, ""),
				new Propiedad(INTERPRETE, ""))));
		
		eCancion = servPersistencia.registrarEntidad(eCancion);
		cancion.setId(eCancion.getId());
		poolDAO.addObject(eCancion.getId(), cancion);
		
		adaptadorInterprete.create(cancion.getInterprete());
		adaptadorEstiloMusical.create(cancion.getEstiloMusical());
		
		servPersistencia.eliminarPropiedadEntidad(eCancion, INTERPRETE);
		servPersistencia.anadirPropiedadEntidad(eCancion, INTERPRETE, 
				String.valueOf(cancion.getInterprete().getId()));
		servPersistencia.eliminarPropiedadEntidad(eCancion, ESTILO);
		servPersistencia.anadirPropiedadEntidad(eCancion, ESTILO, 
				String.valueOf(cancion.getEstiloMusical().getId()));
		
		
		return eCancion;
		
	}
	
	private Cancion entidadToCancion(Entidad eCancion) {
		
		InterpreteDAO adaptadorInterprete = null;
		EstiloMusicalDAO adaptadorEstiloMusical = null;
		try {
			 adaptadorInterprete = FactoriaDAO.getInstancia().getInterpreteDAO();
		} catch (DAOException e) {
			e.printStackTrace();
		}
		
		try {
			 adaptadorEstiloMusical = FactoriaDAO.getInstancia().getEstiloMusicalDAO();
		} catch (DAOException e) {
			e.printStackTrace();
		}
		
		String titulo = this.servPersistencia.recuperarPropiedadEntidad(eCancion, TITULO);
		String rutafichero = this.servPersistencia.recuperarPropiedadEntidad(eCancion, RUTAFICHERO);
		int numrep = Integer.parseInt(this.servPersistencia.recuperarPropiedadEntidad(eCancion, NUMRERP));
		int interpreteId = Integer.parseInt(this.servPersistencia.recuperarPropiedadEntidad(eCancion, INTERPRETE));
		int estiloId = Integer.parseInt(this.servPersistencia.recuperarPropiedadEntidad(eCancion, ESTILO));
		
		Cancion cancion = new Cancion(titulo, rutafichero, null, null, numrep);
		this.poolDAO.addObject(eCancion.getId(), cancion);
		
		try {
			Interprete interprete = adaptadorInterprete.get(interpreteId);
			EstiloMusical estilo = adaptadorEstiloMusical.get(estiloId);
			cancion.setInterprete(interprete);
			cancion.setEstiloMusical(estilo);
		} catch (DAOException e) {
			System.err.println("Inconsistencia en la base de datos");
			e.printStackTrace();
		}
		
		return cancion;
	}

	
	public void create(Cancion cancion) {
		Entidad eCancion;
		boolean existe = true;
		if (cancion.getId() != 0) {
			try {
				if (poolDAO.contains(cancion.getId())) return;
				eCancion = this.servPersistencia.recuperarEntidad(cancion.getId());
			} catch (NullPointerException e) {
				existe = false;
			}
		} else 
			existe = false;
		if (existe) return;
		eCancion = cancionToEntidadRegistrar(cancion);
		cancion.setId(eCancion.getId());
		
	}

	
	public boolean delete(Cancion cancion) {
		Entidad eCancion;
		try {
			eCancion = servPersistencia.recuperarEntidad(cancion.getId());
		} catch (NullPointerException e) {
			return false;
		}
		
		return servPersistencia.borrarEntidad(eCancion);
	}

	
	public void update(Cancion cancion) throws DAOException {
		
		InterpreteDAO adaptadorInterprete = null;
		EstiloMusicalDAO adaptadorEstiloMusical = null;
		try {
			 adaptadorInterprete = FactoriaDAO.getInstancia().getInterpreteDAO();
		} catch (DAOException e) {
			e.printStackTrace();
		}
		
		try {
			 adaptadorEstiloMusical = FactoriaDAO.getInstancia().getEstiloMusicalDAO();
		} catch (DAOException e) {
			e.printStackTrace();
		}
		
		try {
			Entidad eCancion = servPersistencia.recuperarEntidad(cancion.getId());
			String nuevoNumrep = String.valueOf(cancion.getNumReproducciones());
			adaptadorInterprete.create(cancion.getInterprete());
			String nuevoInterprete = String.valueOf(cancion.getInterprete().getId());
			
			adaptadorEstiloMusical.create(cancion.getEstiloMusical());
			String nuevoEstilo = String.valueOf(cancion.getEstiloMusical().getId());
			
			servPersistencia.eliminarPropiedadEntidad(eCancion, TITULO);
			servPersistencia.anadirPropiedadEntidad(eCancion, TITULO, cancion.getTitulo());
			servPersistencia.eliminarPropiedadEntidad(eCancion, RUTAFICHERO);
			servPersistencia.anadirPropiedadEntidad(eCancion, RUTAFICHERO, cancion.getRutaFichero());
			servPersistencia.eliminarPropiedadEntidad(eCancion, NUMRERP);
			servPersistencia.anadirPropiedadEntidad(eCancion, NUMRERP, nuevoNumrep);
			servPersistencia.eliminarPropiedadEntidad(eCancion, INTERPRETE);
			servPersistencia.anadirPropiedadEntidad(eCancion, INTERPRETE, nuevoInterprete);
			servPersistencia.eliminarPropiedadEntidad(eCancion, ESTILO);
			servPersistencia.anadirPropiedadEntidad(eCancion, ESTILO, nuevoEstilo);
		} catch (NullPointerException e) {
			throw new DAOException("No existe ninguna entidad con este id en la base de datos");
		}
		
	}

	
	public Cancion get(int id) throws DAOException {
		if ( !this.poolDAO.contains(id)) {
			try {
				Entidad eCancion = this.servPersistencia.recuperarEntidad(id);
				return entidadToCancion(eCancion);
			} catch (NullPointerException e) {
				throw new DAOException("El elemento con id: " + " no se encuentra en la base de datos");
			}
		}
		else
			return (Cancion) this.poolDAO.getObject(id);
	}

	
	public List<Cancion> getAll() {
		List<Entidad> entidades = servPersistencia.recuperarEntidades(CANCION);
		List<Cancion> canciones = new LinkedList<Cancion>();
		try {
			for (Entidad eCancion : entidades) {
				canciones.add(this.get(eCancion.getId()));
			}
		} catch (DAOException e) {
			System.err.println("Inconsistencia en la base de datos");
			e.printStackTrace();
		}
		
		return canciones;
	}

}
