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
import umu.tds.dominio.ListaCanciones;

public class TDSListaCancionesDAO implements ListaCancionesDAO {
	
	private final String LISTACANCIONES = "listacanciones";
	private final String NOMBRE = "nombrel";
	private final String CANCIONES = "cancionesl";
	private ServicioPersistencia servPersistencia;
	
	private CancionDAO adaptadorCancion;
	private PoolDAO poolDAO;
	
	public TDSListaCancionesDAO() {
		this.servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
		try {
			 this.adaptadorCancion = FactoriaDAO.getInstancia().getCancionDAO();
		} catch (DAOException e) {
			System.err.println("Inconsistencia en la base de datos");
		}
		this.poolDAO = PoolDAO.getInstance();
	}
	
	private String obtenerCodigosCanciones(List<Cancion> canciones, CancionDAO adaptadorCancion) {
		String cancionestr = "";
		for (Cancion cancion : canciones) {
			adaptadorCancion.create(cancion);
			cancionestr += cancion.getId() + " ";
		}
		return cancionestr.trim();
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
	
	private Entidad listaCancionesToEntidad(ListaCanciones lista) {
		Entidad eListaCanciones = new Entidad();
		eListaCanciones.setNombre(LISTACANCIONES);
		
		String cacionestr = this.obtenerCodigosCanciones(lista.getCanciones(), adaptadorCancion);
		eListaCanciones.setPropiedades(new ArrayList<Propiedad>(Arrays.asList(
				new Propiedad(CANCIONES, cacionestr),
				new Propiedad(NOMBRE, lista.getNombre()))));
		return eListaCanciones;
	}
	
	private ListaCanciones EntidadToListaCanciones(Entidad eListaCanciones) {
		String nombre = servPersistencia.recuperarPropiedadEntidad(eListaCanciones, NOMBRE);
		String cancionestr = servPersistencia.recuperarPropiedadEntidad(eListaCanciones, CANCIONES);
		ArrayList<Cancion> canciones = new ArrayList<Cancion>(this.obtenerCancionesDesdeCodigos(
				cancionestr, adaptadorCancion));
		ListaCanciones lista = new ListaCanciones(nombre, canciones);
		lista.setId(eListaCanciones.getId());
		
		return lista;
		
	}

	@Override
	public void create(ListaCanciones lista) {
		Entidad eListaCanciones;
		boolean existe = true;
		if (lista.getId() != 0) {
			try {
				if (poolDAO.contains(lista.getId())) return;
				eListaCanciones = this.servPersistencia.recuperarEntidad(lista.getId());
			} catch (NullPointerException e) {
				existe = false;
			}
		} else
			existe = false;
		if (existe) return;
		eListaCanciones= listaCancionesToEntidad(lista);
		eListaCanciones = servPersistencia.registrarEntidad(eListaCanciones);
		lista.setId(eListaCanciones.getId());
		
	}

	@Override
	public boolean delete(ListaCanciones lista) {
		Entidad eListaCanciones;
		try {
			eListaCanciones = servPersistencia.recuperarEntidad(lista.getId());
		} catch (NullPointerException e) {
			return false;
		}
		
		return servPersistencia.borrarEntidad(eListaCanciones);
	}

	@Override
	public void update(ListaCanciones lista) throws DAOException {
		try {
			String cancionestr = this.obtenerCodigosCanciones(lista.getCanciones(), adaptadorCancion);
			Entidad eListaCanciones = servPersistencia.recuperarEntidad(lista.getId());
			servPersistencia.eliminarPropiedadEntidad(eListaCanciones, NOMBRE);
			servPersistencia.anadirPropiedadEntidad(eListaCanciones, NOMBRE, lista.getNombre());
			servPersistencia.eliminarPropiedadEntidad(eListaCanciones, CANCIONES);
			servPersistencia.anadirPropiedadEntidad(eListaCanciones, CANCIONES, cancionestr);
		} catch (NullPointerException e) {
			throw new DAOException("No existe la lista en la base de datos");
		}
		
	}

	@Override
	public ListaCanciones get(int id) throws DAOException {
		if (!this.poolDAO.contains(id)) {
			try {
				Entidad eListaCanciones = servPersistencia.recuperarEntidad(id);
				return EntidadToListaCanciones(eListaCanciones);
			} catch (NullPointerException e) {
				throw new DAOException("El elemento con id: " + " no se encuentra en la base de datos");
			}	
		} else 
			return (ListaCanciones) this.poolDAO.getObject(id);
	}

	@Override
	public List<ListaCanciones> getAll() {
		List<Entidad> entidades = servPersistencia.recuperarEntidades(LISTACANCIONES);

		List<ListaCanciones> listas = new LinkedList<ListaCanciones>();
		try {
			for (Entidad eListaCanciones : entidades) {
				listas.add(get(eListaCanciones.getId()));
			}
		} catch (DAOException e) {
			System.err.println("Error de aceso al DAO");
			e.printStackTrace();
		}
		
		return listas;
	}

}
