package umu.tds.dominio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import umu.tds.dao.DAOException;
import umu.tds.dao.*;
import umu.tds.dao.FactoriaDAO;
public class CatalogoInterprete {
	private static CatalogoInterprete unicaInstancia;
	private FactoriaDAO factoria;
	private InterpreteDAO adaptadorInterprete;
	
	
	private HashMap<String, Interprete> interpretePorNombre;
	
	private CatalogoInterprete(){
		interpretePorNombre = new HashMap<String, Interprete>();
		try {
			factoria = FactoriaDAO.getInstancia();
			adaptadorInterprete = factoria.getInterpreteDAO();
			List<Interprete> listaInterp = adaptadorInterprete.getAll();
			for (Interprete interp : listaInterp) {
				interpretePorNombre.put(interp.getNombre(), interp);
			}
		} catch (DAOException eDAO) {
			   eDAO.printStackTrace();
		}
	}
	public static CatalogoInterprete getUnicaInstancia() {
		if (unicaInstancia == null) unicaInstancia = new CatalogoInterprete();
		return unicaInstancia;
	}
	public void addInterprete(Interprete inter) {
		interpretePorNombre.put(inter.getNombre(), inter);
	}
	
	public void removeInterprete(Interprete inter) {
		interpretePorNombre.remove(inter.getNombre());
	}
	public ArrayList<Cancion> getCanciones(String inter,ArrayList<Cancion> canc) {
		ArrayList<Cancion> res = new ArrayList<Cancion>(canc.stream().filter(d->d.getInterpreteName().
				contains(inter)).collect(Collectors.toList()));
		return res;
	}

	public ArrayList<Interprete> getAllInterpretes() throws DAOException{
		return new ArrayList<Interprete>(interpretePorNombre.values());
	}
	
	public Interprete getInterprete(String key) {
		return interpretePorNombre.get(key);
	}
}
