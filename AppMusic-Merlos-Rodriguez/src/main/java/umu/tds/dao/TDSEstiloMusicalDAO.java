package umu.tds.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import beans.Entidad;
import beans.Propiedad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.dominio.Cancion;
import umu.tds.dominio.EstiloMusical;

public class TDSEstiloMusicalDAO implements EstiloMusicalDAO{
	
	private final static String ESTILOMUSICAL = "estilomusical";
	private final static String NOMBRE = "nombree";
	private final static String CANCIONES = "cancionese";
	
	private ServicioPersistencia servPersistencia;
	private PoolDAO poolDAO;
	private CancionDAO adaptadorCancion;

	public TDSEstiloMusicalDAO() {
		this.servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
		this.poolDAO = PoolDAO.getInstance();
		try {
			adaptadorCancion = FactoriaDAO.getInstancia().getCancionDAO();
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}
	
	private List<Cancion> obtenerCancionesDesdeCodigos(String cancionestr, CancionDAO adaptadorCancion) {
		List<Cancion> canciones = new LinkedList<Cancion>();
		StringTokenizer strTok = new StringTokenizer(cancionestr, " ");
		while (strTok.hasMoreTokens()) {
			try {
				canciones.add(adaptadorCancion.get(
						Integer.valueOf((String) strTok.nextElement())));
			} catch (NumberFormatException | DAOException e) {
				System.err.println("Inconsistencia en la base de datos");
				e.printStackTrace();
			}
		}
		return canciones;
	}
	
	private String obtenerCodigosCanciones(List<Cancion> canciones, CancionDAO adaptadorCancion) {
		String cancionestr = "";
		for (Cancion cancion : canciones) {
			adaptadorCancion.create(cancion);
			cancionestr += cancion.getId() + " ";
		}
		return cancionestr.trim();
	}
	
	private Entidad EstiloMusicalToEntidad(EstiloMusical estilo) {
		
		Entidad eEstiloMusical = new Entidad();
		eEstiloMusical.setNombre(ESTILOMUSICAL);
		
		eEstiloMusical.setPropiedades(new ArrayList<Propiedad>(Arrays.asList(
				new Propiedad(NOMBRE, estilo.getNombre()),
				new Propiedad(CANCIONES, ""))));
		
		eEstiloMusical = this.servPersistencia.registrarEntidad(eEstiloMusical);
		
		estilo.setId(eEstiloMusical.getId());
		this.poolDAO.addObject(eEstiloMusical.getId(), estilo);
		
		String cancionestr = this.obtenerCodigosCanciones(estilo.getCanciones(), adaptadorCancion);
		
		this.servPersistencia.eliminarPropiedadEntidad(eEstiloMusical, CANCIONES);
		this.servPersistencia.anadirPropiedadEntidad(eEstiloMusical, CANCIONES, cancionestr);
		
		return eEstiloMusical;
	}
	
	private EstiloMusical entidadToEstiloMusical(Entidad eEstiloMusical) {
		
		String nombre = servPersistencia.recuperarPropiedadEntidad(eEstiloMusical, NOMBRE);
		String cancionestr = servPersistencia.recuperarPropiedadEntidad(eEstiloMusical, CANCIONES);
		EstiloMusical estilo = new EstiloMusical(nombre);
		
		this.poolDAO.addObject(eEstiloMusical.getId(), estilo);
		
		List<Cancion> canciones = this.obtenerCancionesDesdeCodigos(cancionestr, adaptadorCancion);
		
		estilo.setCanciones(new ArrayList<Cancion>(canciones));
		
		estilo.setId(eEstiloMusical.getId());
		
		return estilo;
	}

	public void create(EstiloMusical estilo) {
		Entidad eEstiloMusical;
		boolean existe = true;
		if (estilo.getId() != 0) {
			try {
				if (this.poolDAO.contains(estilo.getId())) return;
				eEstiloMusical = this.servPersistencia.recuperarEntidad(estilo.getId());
			} catch (NullPointerException e) {
				existe = false;
			}
		} else
			existe = false;
		if (existe) return;
		eEstiloMusical = EstiloMusicalToEntidad(estilo);
		estilo.setId(eEstiloMusical.getId());
	}

	public boolean delete(EstiloMusical estilo) {
		if (this.poolDAO.contains(estilo.getId())) 
			this.poolDAO.removeObject(estilo.getId());
		Entidad eEstiloMusical;
		try {
			eEstiloMusical = servPersistencia.recuperarEntidad(estilo.getId());
		} catch (NullPointerException e) {
			return false;
		}
		
		return servPersistencia.borrarEntidad(eEstiloMusical);
	}


	public void update(EstiloMusical estilo) throws DAOException {
		try {
			Entidad eEstiloMusical = servPersistencia.recuperarEntidad(estilo.getId());
			String cancionestr = this.obtenerCodigosCanciones(estilo.getCanciones(), adaptadorCancion);

			
			servPersistencia.eliminarPropiedadEntidad(eEstiloMusical, NOMBRE);
			servPersistencia.anadirPropiedadEntidad(eEstiloMusical, NOMBRE, estilo.getNombre());
			servPersistencia.eliminarPropiedadEntidad(eEstiloMusical, CANCIONES);
			servPersistencia.anadirPropiedadEntidad(eEstiloMusical, CANCIONES, cancionestr);
		} catch (NullPointerException e) {
			throw new DAOException("No existe el estilo en la base de datos");
		}
	}

	public EstiloMusical get(int id) throws DAOException {
		if ( !this.poolDAO.contains(id)) {
			try {
				Entidad eEstiloMusical = this.servPersistencia.recuperarEntidad(id);
				return entidadToEstiloMusical(eEstiloMusical);
			} catch (Exception e) {
				throw new DAOException("El elemento con id: " + " no se encuentra en la base de datos");
			}
		}
		else
			return (EstiloMusical) this.poolDAO.getObject(id);
	}

	public List<EstiloMusical> getAll() {
		List<Entidad> entidades = servPersistencia.recuperarEntidades(ESTILOMUSICAL);

		List<EstiloMusical> estilo = new LinkedList<EstiloMusical>();
		try {
			for (Entidad eEstiloMusical : entidades) {
				estilo.add(get(eEstiloMusical.getId()));
			}
		} catch (DAOException e) {
			System.err.println("Error de aceso al DAO");
			e.printStackTrace();
		}
		
		return estilo;
	}
	
}
