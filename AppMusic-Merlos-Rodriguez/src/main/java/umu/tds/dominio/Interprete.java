package umu.tds.dominio;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Interprete {
	
	private int id;
    private String nombre;
    private ArrayList<Cancion> canciones;

    public Interprete(String nombre) {
        this.nombre = nombre;
        this.canciones = new ArrayList<Cancion>();
    }

    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
    public ArrayList<Cancion> getCancionesByName(String nombre){
    	return (ArrayList<Cancion>)this.canciones.stream().
    			filter(c -> c.getTitulo().contains(nombre)).
    			collect(Collectors.toList());
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void addCancion(Cancion cancion) {
        this.canciones.add(cancion);
    }
    public void setCanciones(ArrayList<Cancion> canciones) {
		this.canciones = canciones;
	}
    @SuppressWarnings("unchecked")
    public ArrayList<Cancion> getCanciones(){
        return (ArrayList<Cancion>)this.canciones.clone();
    }
}
