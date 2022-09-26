package umu.tds.controlador;


import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import com.itextpdf.text.DocumentException;


import umu.tds.componente.CancionLiteral;
import umu.tds.componente.CancionesEvent;
import umu.tds.componente.CancionesListener;
import umu.tds.dao.*;
import umu.tds.dominio.*;

public final class Controlador implements CancionesListener{

	private UsuarioDAO adaptadorUsuario;
	private CancionDAO adaptadorCancion;
	private ListaCancionesDAO adaptadorListas;

	private Usuario usuarioActual;
	private static Controlador unicaInstancia = null;

	private CatalogoUsuarios catalogoUsuarios;
	private CatalogoCanciones catalogoCanciones;
	private CatalogoEstilo catalogoEstilo;
	private CatalogoInterprete catalogoInterprete;

	private MusicPlayer mp3player;

	public Controlador() throws DAOException {
		inicializarAdaptadores(); 
		inicializarCatalogos();
		this.mp3player = new MusicPlayer();
	}

	private void inicializarAdaptadores() {
		try {
			adaptadorUsuario = FactoriaDAO.getInstancia().getUsuarioDAO();
			adaptadorCancion = FactoriaDAO.getInstancia().getCancionDAO();
			adaptadorListas = FactoriaDAO.getInstancia().getListaCancionesDAO();
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	private void inicializarCatalogos() throws DAOException {
		catalogoUsuarios = CatalogoUsuarios.getUnicaInstancia();
		catalogoCanciones = CatalogoCanciones.getUnicaInstancia();
		catalogoInterprete = CatalogoInterprete.getUnicaInstancia();
		catalogoEstilo = CatalogoEstilo.getUnicaInstancia();
	}

	public static Controlador getUnicaInstancia() throws DAOException {
		if (unicaInstancia == null)
			unicaInstancia = new Controlador();
		return unicaInstancia;
	}

	public Usuario getUsuarioActual() {
		return usuarioActual;
	}

	public boolean esUsuarioRegistrado(String login) {
		return catalogoUsuarios.getUsuario(login) != null;
	}

	public boolean loginUsuario(String nombre, String password) {
		Usuario usuario = catalogoUsuarios.getUsuario(nombre);
		if (usuario != null && usuario.getPassword().equals(password)) {
			this.usuarioActual = usuario;
			return true;
		}
		return false;
	}
	
	public boolean registrarUsuario(String nombre, String apellidos, String email, String login, String password,
			String fechaNacimiento) {

		if (esUsuarioRegistrado(login))
			return false;
		Usuario usuario = new Usuario.UsuarioBuilder(nombre).apellidos(apellidos).email(email).login(login).password(password).fechaNacimiento(fechaNacimiento).build();
		adaptadorUsuario.create(usuario);
		catalogoUsuarios.addUsuario(usuario);
		return true;
	}

	public boolean borrarUsuario(Usuario usuario) {
		if (!esUsuarioRegistrado(usuario.getLogin()))
			return false;
		adaptadorUsuario.delete(usuario);
		catalogoUsuarios.removeUsuario(usuario);
		return true;
	}

	/*Metodo para hacer premium*/
	public String hacerUsuarioPremium() throws DAOException {
		usuarioActual.setPremium();
		adaptadorUsuario.update(usuarioActual);
		return usuarioActual.getNombreDescuento();
	}

	public boolean esPremium() {
		return usuarioActual.esPremium();
	}

	public ArrayList<Cancion> filtrarCanciones(String titulo, String inter, String estilo){
		ArrayList<Cancion> byestilo = catalogoEstilo.getCancionesEstilo(estilo);
		ArrayList<Cancion> byname = catalogoCanciones.getCanciones(titulo,byestilo);
		ArrayList<Cancion> byinter = catalogoInterprete.getCanciones(inter,byname);
		return byinter;
	}

	public ArrayList<String> getNombresEstilos() throws DAOException{
		return new ArrayList<String>(catalogoEstilo.getAllEstilos().
				stream().map(s-> s.getNombre()).collect(Collectors.toList()));
	}

	public ListaCanciones createPlayList(String nombre) throws DAOException {
		ListaCanciones newPlayList = usuarioActual.addPlayList(nombre);
		if(newPlayList != null) {
			adaptadorListas.create(newPlayList);
			adaptadorUsuario.update(usuarioActual);
		}
		return newPlayList;
	}

	public void addSongPlayList(ArrayList<String> canciones, String playlist) throws DAOException {
		ListaCanciones pList = usuarioActual.getListaByName(playlist);
		ArrayList<Cancion> canc = new ArrayList<Cancion>(canciones.stream().
				map(d->catalogoCanciones.getCancion(d)).collect(Collectors.toList()));
		canc.forEach(p->pList.addCancion(p));
		adaptadorListas.update(pList);
	}

	public void removeSongPlayList(Cancion cancion, ListaCanciones playlist) throws DAOException {
		 playlist.removeCancion(cancion);
		 adaptadorListas.update(playlist);
	}

	public boolean existePlayList(String nombrelista) {
		return usuarioActual.hasPlayList(nombrelista);
	}

	public ArrayList<Cancion> getCancionesRec(){
		return usuarioActual.getCancionesRecientes();
	}
	
	public ArrayList<Cancion> getCancionesMasRep(){
		return catalogoCanciones.getCancionesMasRep();
	}

	public ArrayList<Cancion> getAllCanciones() {
		return catalogoCanciones.getAllCanciones();
	}

	public void playByName(String titulo) throws DAOException {
		Cancion c = catalogoCanciones.getCancion(titulo);
		this.repCancion(c);
	}

	public void repCancion(Cancion c) throws DAOException {
		c.addReproduccion();
		this.mp3player.playCancion(c,usuarioActual,adaptadorUsuario);
	}

	public void stopCancion() {
		this.mp3player.stopCancion();
	}

	public void pauseCancion() {
		this.mp3player.pauseCancion();
	}

	public boolean generatePDF(String path) throws FileNotFoundException, DocumentException{
		PDFGenerator pdfgen = new PDFGenerator();
		return pdfgen.generatePDF(path, usuarioActual);
	}

	@Override 
	public void enteradoCambioCanciones(CancionesEvent e) {
		boolean nueva;
		for (CancionLiteral c : e.getCanciones().getCancion() ) {
			 List<Cancion> canciones = adaptadorCancion.getAll();
			 nueva = canciones.stream().filter(d->equalsCancion(c.getTitulo(),
					 c.getInterprete(), c.getEstilo(),d)).collect(Collectors.toList()).isEmpty();
			 if(nueva) {
				 Cancion cancion = toCancion(c);
				 adaptadorCancion.create(cancion);
			 }
		}
	}

	public Cancion toCancion(CancionLiteral c) {
		Interprete inter = getInterprete(c.getInterprete());
		EstiloMusical estilo = getEstiloMusical(c.getEstilo());
		Cancion res = new Cancion(c.getTitulo(), c.getURL(),estilo,inter);
		inter.addCancion(res);
		estilo.addCancion(res);
		catalogoCanciones.addCancion(res);
		return res;
	}

	public Interprete getInterprete(String inter) {
		Interprete newInter =  catalogoInterprete.getInterprete(inter);
		if(newInter==null) {
			 newInter = new Interprete(inter);
			catalogoInterprete.addInterprete(newInter);
		}
		return newInter;
	}

	public EstiloMusical getEstiloMusical(String estilo) {
		EstiloMusical newEstilo = catalogoEstilo.getEstilo(estilo);
		if(newEstilo==null) {
			 newEstilo = new EstiloMusical(estilo); 
			catalogoEstilo.addEstilo(newEstilo);
		}
		return newEstilo;
	}

	public boolean equalsCancion(String name,String inter,String estilo, Cancion o2) {
		if (!name.equals(o2.getTitulo())) return false;
		if(!estilo.equals(o2.getEstiloName())) return false;
		if(!inter.equals(o2.getInterpreteName())) return false;
		return true;
	}

	public ArrayList<String> getNombresListas() {
		return new ArrayList<String>(usuarioActual.getListasCanciones().stream().
				map(d->d.getNombre()).collect(Collectors.toList()));
	}
	
	public ListaCanciones getCancionesPlayList(String listName){
		return usuarioActual.getListaByName(listName);
	}

}
