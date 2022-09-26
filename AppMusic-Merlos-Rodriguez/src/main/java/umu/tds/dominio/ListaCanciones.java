package umu.tds.dominio;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ListaCanciones {
	private int id;
	private String nombre;
	private ArrayList<Cancion> canciones;
	
	public int getId() {
		return this.id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public ListaCanciones(String nombre) {
		this.canciones = new ArrayList<Cancion>();
		this.nombre =  nombre;
	}
	
	public ArrayList<String> getTitulos(){
		return new ArrayList<String>(this.canciones.stream().map(d->d.getTitulo()).collect(Collectors.toList()));
	} 
	
	public ArrayList<String> getInterpretes(){
		return new ArrayList<String>(canciones.stream().map(d->d.getInterpreteName()).collect(Collectors.toList()));
	}
	
	public ArrayList<String> getEstilos(){
		return new ArrayList<String>(this.canciones.stream().map(d->d.getEstiloName()).collect(Collectors.toList()));
	}
	
	public ListaCanciones(String nombre, ArrayList<Cancion> c) {
		this.canciones =  c;
		this.nombre = nombre;
	}
	
	public void addCancion(Cancion cancion) {
		this.canciones.add(cancion);
	}
	
	public void removeCancion(Cancion cancion) {
		this.canciones.remove(cancion);
	}
	
	public ArrayList<Cancion> getCanciones(){
		return new ArrayList<Cancion>(this.canciones);
	}
	
}
