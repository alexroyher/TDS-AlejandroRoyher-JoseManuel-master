package umu.tds.dominio;
import java.util.ArrayList;

public class EstiloMusical {
	
	private int id;
	private String nombre;
	ArrayList<Cancion> canciones;
	
	
	public EstiloMusical(String nombre) {
		this.nombre = nombre;
		this.canciones = new ArrayList<Cancion>();
	}
	
	public EstiloMusical(String nombre, ArrayList<Cancion> canciones) {
		this.nombre = nombre;
		this.canciones = new ArrayList<Cancion>(canciones);
	}
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public ArrayList<Cancion> getCanciones() {
    	return new ArrayList<Cancion>(this.canciones);
    }
    
    public void setCanciones(ArrayList<Cancion> canciones) {
    	this.canciones = new ArrayList<Cancion>(canciones);
    }
    
    public void addCancion(Cancion cancion) {
    	this.canciones.add(cancion);
    }
    
    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
