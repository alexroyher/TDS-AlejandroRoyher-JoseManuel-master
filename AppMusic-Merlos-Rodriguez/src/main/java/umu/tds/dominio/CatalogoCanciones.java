package umu.tds.dominio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import umu.tds.dao.*;

public class CatalogoCanciones {
	private static CatalogoCanciones unicaInstancia;
	private FactoriaDAO factoria;
	private CancionDAO adaptadorCancion;
	
	private HashMap<String, Cancion> cancionesPorTitulo;
	private HashMap<Integer, Cancion> cancionesPorId;
	
	
	private CatalogoCanciones(){
		cancionesPorTitulo = new HashMap<String, Cancion>();
		cancionesPorId = new HashMap<Integer, Cancion>();
		try {
			factoria = FactoriaDAO.getInstancia();
			adaptadorCancion = factoria.getCancionDAO();
			List<Cancion> listaCanciones = adaptadorCancion.getAll();
			for (Cancion cancion : listaCanciones) {
				cancionesPorTitulo.put(cancion.getTitulo(), cancion);
				cancionesPorId.put(cancion.getId(), cancion);
			}
		} catch (DAOException eDAO) {
			   eDAO.printStackTrace();
		}
	}
	public static CatalogoCanciones getUnicaInstancia() {
		if (unicaInstancia == null) unicaInstancia = new CatalogoCanciones();
		return unicaInstancia;
	}
	public void addCancion(Cancion cancion) {
		cancionesPorTitulo.put(cancion.getTitulo(), cancion);
		cancionesPorId.put(cancion.getId(), cancion);
	}
	public void removeCancion(Cancion cancion) {
		cancionesPorTitulo.remove(cancion.getTitulo());
		cancionesPorId.remove(cancion.getId());
	}
	
	public ArrayList<Cancion> getCanciones(String titulo,ArrayList<Cancion> canc) {
		ArrayList<Cancion> res = new ArrayList<Cancion>(canc.stream().filter(d->d.getTitulo().
				contains(titulo)).collect(Collectors.toList()));
		return res;
	}
	
	public ArrayList<Cancion> getAllCanciones() {
		return new ArrayList<Cancion>(cancionesPorTitulo.values());
	}
	
	public ArrayList<Cancion> getCancionesMasRep() {
		ArrayList<Cancion> newList = getAllCanciones();
		newList.sort((Cancion o1,Cancion o2)->o2.getNumReproducciones()-o1.getNumReproducciones());
		return new ArrayList<Cancion>(newList.stream().limit(10).collect(Collectors.toList()));
	}
	
	public Cancion getCancion(int key) {
		return cancionesPorId.get(key);
	}
	public Cancion getCancion(String key) {
		return cancionesPorTitulo.get(key);
	}
	
	
}
