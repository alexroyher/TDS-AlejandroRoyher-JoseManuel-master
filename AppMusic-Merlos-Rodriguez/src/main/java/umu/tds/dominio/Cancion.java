package umu.tds.dominio;



public class Cancion {
    private int id;
    private String titulo;
    private String rutaFichero;
    private int numReproducciones;
    private EstiloMusical estiloMusical;
    private Interprete interprete;

    public Cancion(String titulo, String rutaFichero, EstiloMusical estiloMusical, Interprete interprete) {
        this.id = 0;
        this.titulo = titulo;
        this.rutaFichero = rutaFichero;
        this.numReproducciones = 0;
        this.estiloMusical = estiloMusical;
        this.interprete = interprete;
    }

    public Cancion(String titulo, String rutaFichero, EstiloMusical estiloMusical, Interprete interprete, 
            int numReproducciones) {
        this(titulo, rutaFichero, estiloMusical, interprete);
        this.numReproducciones = numReproducciones;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getRutaFichero() {
        return rutaFichero;
    }

    public void setRutaFichero(String rutaFichero) {
        this.rutaFichero = rutaFichero;
    }

    public int getNumReproducciones() {
        return numReproducciones;
    }

    public void setNumReproducciones(int numReproducciones) {
        this.numReproducciones = numReproducciones;
    }

    public EstiloMusical getEstiloMusical() {
        return estiloMusical;
    }

    public void setEstiloMusical(EstiloMusical estiloMusical) {
        this.estiloMusical = estiloMusical;
    }
    
    public String getEstiloName() {
    	return this.estiloMusical.getNombre();
    }
    public Interprete getInterprete() {
        return interprete;
    }

    public void setInterprete(Interprete interprete) {
        this.interprete = interprete;
    }
    
    public void addReproduccion() {
    	this.numReproducciones++;
    }
	public String getInterpreteName() {
		return this.interprete.getNombre();
	}
}

