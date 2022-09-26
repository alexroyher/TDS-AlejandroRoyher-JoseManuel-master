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
import umu.tds.dominio.Interprete;

public class TDSInterpreteDAO implements InterpreteDAO {
	
	private final static String INTERPRETE = "interprete";
	private final static String NOMBRE = "nombre";
	private final static String CANCIONES = "canciones";
	
	private ServicioPersistencia servPersistencia;
	private PoolDAO poolDAO;
	private CancionDAO adaptadorCancion;
	
	public TDSInterpreteDAO() {
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

	private Entidad InterpreteToEntidadRegistrar(Interprete interprete) {
		Entidad eInterprete = new Entidad();
		
		eInterprete.setNombre(INTERPRETE);
		
		eInterprete.setPropiedades(new ArrayList<Propiedad>(Arrays.asList(
				new Propiedad(NOMBRE, interprete.getNombre()),
				new Propiedad(CANCIONES, ""))));
		eInterprete = servPersistencia.registrarEntidad(eInterprete);
		
		interprete.setId(eInterprete.getId());
		poolDAO.addObject(eInterprete.getId(), interprete);
		
		String cancionestr = this.obtenerCodigosCanciones(interprete.getCanciones(), adaptadorCancion);
		
		servPersistencia.eliminarPropiedadEntidad(eInterprete, CANCIONES);
		servPersistencia.anadirPropiedadEntidad(eInterprete, CANCIONES, cancionestr);
		
		return eInterprete;
	}
	
	private Interprete EntidadToInterprete(Entidad eInterprete) {
		String nombre = servPersistencia.recuperarPropiedadEntidad(eInterprete, NOMBRE);
		String cancionestr = servPersistencia.recuperarPropiedadEntidad(eInterprete, CANCIONES);
		Interprete interprete = new Interprete(nombre);
		
		poolDAO.addObject(eInterprete.getId(), interprete);
		
		List<Cancion> canciones = this.obtenerCancionesDesdeCodigos(cancionestr, adaptadorCancion);
		
		interprete.setCanciones(new ArrayList<Cancion>(canciones));
		
		return interprete;
		
	}

	@Override
	public void create(Interprete interprete) {
		Entidad eInterprete;
		boolean existe = true;
		if (interprete.getId() != 0) {
			try {
				if (poolDAO.contains(interprete.getId())) return;
				eInterprete = servPersistencia.recuperarEntidad(interprete.getId());
			} catch (NullPointerException e) {
				existe = false;
			}
		} else
			existe = false;
		if (existe) return;
		eInterprete = InterpreteToEntidadRegistrar(interprete);
		interprete.setId(eInterprete.getId());
		
	}

	@Override
	public boolean delete(Interprete interprete) {
		if (this.poolDAO.contains(interprete.getId())) 
			this.poolDAO.removeObject(interprete.getId());
		Entidad eInterprete;
		try {
			eInterprete = servPersistencia.recuperarEntidad(interprete.getId());
		} catch (NullPointerException e) {
			return false;
		}
		
		return servPersistencia.borrarEntidad(eInterprete);
	}

	@Override
	public void update(Interprete interprete) throws DAOException {
		try {
			Entidad eInterprete = servPersistencia.recuperarEntidad(interprete.getId());
			
			String cancionestr = this.obtenerCodigosCanciones(interprete.getCanciones(), adaptadorCancion);
			
			servPersistencia.eliminarPropiedadEntidad(eInterprete, NOMBRE);
			servPersistencia.anadirPropiedadEntidad(eInterprete, NOMBRE, interprete.getNombre());
			servPersistencia.eliminarPropiedadEntidad(eInterprete, CANCIONES);
			servPersistencia.anadirPropiedadEntidad(eInterprete, CANCIONES, cancionestr);
		} catch (NullPointerException e) {
			throw new DAOException("No existe ninguna entidad con este id en la base de datos");
		}	
	}

	@Override
	public Interprete get(int id) throws DAOException {
		if ( !this.poolDAO.contains(id)) {
			try {
				Entidad eInterprete = this.servPersistencia.recuperarEntidad(id);
				return EntidadToInterprete(eInterprete);
			} catch (Exception e) {
				throw new DAOException("El elemento con id: " + " no se encuentra en la base de datos");
			}
		}
		else
			return (Interprete) this.poolDAO.getObject(id);
	}

	@Override
	public List<Interprete> getAll() {
		List<Entidad> entidades = servPersistencia.recuperarEntidades(INTERPRETE);
		List<Interprete> interpretes = new LinkedList<Interprete>();
		try {
			for (Entidad eInterprete : entidades) {
				interpretes.add(this.get(eInterprete.getId()));
			}
		} catch (DAOException e) {
			System.err.println("Inconsistencia en la base de datos");
			e.printStackTrace();
		}
		
		return interpretes;
	}

}
