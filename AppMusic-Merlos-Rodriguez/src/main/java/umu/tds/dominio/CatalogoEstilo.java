package umu.tds.dominio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import umu.tds.dao.DAOException;
import umu.tds.dao.EstiloMusicalDAO;
import umu.tds.dao.FactoriaDAO;

public class CatalogoEstilo {
	private static CatalogoEstilo unicaInstancia;
	private FactoriaDAO factoria;
	private EstiloMusicalDAO adaptadorEstilo;
	
	private HashMap<String, EstiloMusical> estiloPorNombre;
	
	private CatalogoEstilo(){
		estiloPorNombre = new HashMap<String, EstiloMusical>();
		try {
			factoria = FactoriaDAO.getInstancia();
			adaptadorEstilo = factoria.getEstiloMusicalDAO();
			List<EstiloMusical> listaEstilos = adaptadorEstilo.getAll();
			for (EstiloMusical estilo : listaEstilos) {
				estiloPorNombre.put(estilo.getNombre(), estilo);
			}
		} catch (DAOException eDAO) {
			   eDAO.printStackTrace();
		}
	}
	public static CatalogoEstilo getUnicaInstancia() {
		if (unicaInstancia == null) unicaInstancia = new CatalogoEstilo();
		return unicaInstancia;
	}
	
	public void addEstilo(EstiloMusical estilo) {
		estiloPorNombre.put(estilo.getNombre(), estilo);
	}
	
	public void removeEstilo(EstiloMusical estilo) {
		estiloPorNombre.remove(estilo.getNombre());
	}

	public ArrayList<EstiloMusical> getAllEstilos() throws DAOException{
		return new ArrayList<EstiloMusical>(estiloPorNombre.values());
	}

	public EstiloMusical getEstilo(String key) {
		return estiloPorNombre.get(key);
	}
	public ArrayList<Cancion> getCancionesEstilo(String estilo) {
		return this.estiloPorNombre.get(estilo).getCanciones();
	}
}
