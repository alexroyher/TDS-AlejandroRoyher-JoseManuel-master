package umu.tds.dao;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.dominio.Cancion;
import umu.tds.dominio.Descuento;
import umu.tds.dominio.ListaCanciones;
import umu.tds.dominio.Usuario;
import beans.Entidad;
import beans.Propiedad;

/**
 * 
 * Clase que implementa el Adaptador DAO concreto de Usuario para el tipo H2.
 * 
 */
public final class TDSUsuarioDAO implements UsuarioDAO {

	private static final String USUARIO = "Usuario";
	
	private static final String NOMBRE = "nombre";
	private static final String APELLIDOS = "apellidos";
	private static final String EMAIL = "email";
	private static final String LOGIN = "login";
	private static final String PASSWORD = "password";
	private static final String FECHA_NACIMIENTO = "fechaNacimiento";
	private static final String PREMIUM = "premium";
	private static final String DESCUENTO = "descuento";
	private static final String RECIENTES = "recientes";
	private static final String PLAYLISTS = "playlists";
	
	private ServicioPersistencia servPersistencia;
	private CancionDAO adaptadorCancion;
	private ListaCancionesDAO adaptadorListasCanciones;


	public TDSUsuarioDAO() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
		try {
			adaptadorCancion = FactoriaDAO.getInstancia().getCancionDAO();
		} catch (DAOException e) {
			e.printStackTrace();
		}
		
		try {
			adaptadorListasCanciones = FactoriaDAO.getInstancia().getListaCancionesDAO();
		} catch (DAOException e) {
			e.printStackTrace();
		}
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
	
	private String obtenerCodigosListas(List<ListaCanciones> listas, ListaCancionesDAO adaptadorListas) {
		String cancionestr = "";
		for (ListaCanciones lista : listas) {
			adaptadorListas.create(lista);
			cancionestr += lista.getId() + " ";
		}
		return cancionestr.trim();
	}
	
	private List<ListaCanciones> obtenerListasDesdeCodigos(String listastr, ListaCancionesDAO adaptadorListas) {
		List<ListaCanciones> listas = new LinkedList<ListaCanciones>();
		StringTokenizer strTok = new StringTokenizer(listastr, " ");
		while (strTok.hasMoreTokens()) {
			try {
				listas.add(adaptadorListas.get(
						Integer.valueOf((String) strTok.nextElement())));
			} catch (NumberFormatException | DAOException e) {
				System.err.println("Inconsistencia en la base de datos");
				e.printStackTrace();
			}
		}
		return listas;
	}
     @SuppressWarnings("unused")
	private Usuario entidadToUsuario(Entidad eUsuario) throws DAOException {

		String nombre = servPersistencia.recuperarPropiedadEntidad(eUsuario, NOMBRE);
		String apellidos = servPersistencia.recuperarPropiedadEntidad(eUsuario, APELLIDOS);
		String email = servPersistencia.recuperarPropiedadEntidad(eUsuario, EMAIL);
		String login = servPersistencia.recuperarPropiedadEntidad(eUsuario, LOGIN);
		String password = servPersistencia.recuperarPropiedadEntidad(eUsuario, PASSWORD);
		String fechaNacimiento = servPersistencia.recuperarPropiedadEntidad(eUsuario, FECHA_NACIMIENTO);
		String recientestr = servPersistencia.recuperarPropiedadEntidad(eUsuario, RECIENTES);
		String playliststr = servPersistencia.recuperarPropiedadEntidad(eUsuario, PLAYLISTS);
		boolean premium = Boolean.parseBoolean(servPersistencia.recuperarPropiedadEntidad(eUsuario, PREMIUM));
		Descuento descuento;
		String nombre_clase = servPersistencia.recuperarPropiedadEntidad(eUsuario, DESCUENTO);
		try {
			Class<?> cldescuento = Class.forName(nombre_clase);
			Constructor<?> constructor = cldescuento.getConstructor();
			descuento = (Descuento) constructor.newInstance();
		} catch (Exception e) {
			throw new DAOException("Inconsistencia en la base de datos (Error en la clase "+ nombre_clase +")"
					+ "¿Es posible que ya no exista?");
		}
		
		ArrayList<Cancion> recientes = new ArrayList<Cancion>(
				this.obtenerCancionesDesdeCodigos(recientestr, adaptadorCancion));
		ArrayList<ListaCanciones> playlists = new ArrayList<ListaCanciones>(
				this.obtenerListasDesdeCodigos(playliststr, adaptadorListasCanciones));

		Usuario usuario = new Usuario.UsuarioBuilder(nombre).apellidos(apellidos).email(email).login(login).password(password).fechaNacimiento(fechaNacimiento).build();
		if (premium) usuario.setPremium();
		for (Cancion cancion : recientes)
			usuario.addRecientes(cancion);
		for (ListaCanciones lista : playlists)
			usuario.addPlayList(lista.getNombre());
		
		usuario.setId(eUsuario.getId());
		
		return usuario;
	}

	private Entidad usuarioToEntidad(Usuario usuario) {
		Entidad eUsuario = new Entidad();
		eUsuario.setNombre(USUARIO);
		
		String recientestr = this.obtenerCodigosCanciones(
				usuario.getCancionesRecientes(), adaptadorCancion);
		String playliststr = this.obtenerCodigosListas(
				usuario.getListasCanciones(), adaptadorListasCanciones);
		
		String descuento = usuario.getDescuento().getClass().getName();
		
		String premium = String.valueOf(usuario.esPremium());

		eUsuario.setPropiedades(Arrays.asList(
				new Propiedad(NOMBRE, usuario.getNombre()),
				new Propiedad(APELLIDOS, usuario.getApellidos()), 
				new Propiedad(EMAIL, usuario.getEmail()),
				new Propiedad(LOGIN, usuario.getLogin()), 
				new Propiedad(PASSWORD, usuario.getPassword()),
				new Propiedad(FECHA_NACIMIENTO, usuario.getFechaNacimiento()),
				new Propiedad(RECIENTES, recientestr),
				new Propiedad(PLAYLISTS, playliststr),
				new Propiedad(DESCUENTO, descuento),
				new Propiedad(PREMIUM, premium)));
		
		return eUsuario;
	}

	public void create(Usuario usuario) {
		Entidad eUsuario;
		boolean existe = true;
		if (usuario.getId() != 0) {
			try {
				eUsuario = servPersistencia.recuperarEntidad(usuario.getId());
			} catch (NullPointerException e) {
				existe = false;
			}
		} else existe = false;
		if (existe) return;
		eUsuario = usuarioToEntidad(usuario);
		eUsuario = servPersistencia.registrarEntidad(eUsuario);
		usuario.setId(eUsuario.getId());
	}

	public boolean delete(Usuario usuario) {
		Entidad eUsuario;
		try {
			eUsuario = servPersistencia.recuperarEntidad(usuario.getId());
		} catch (NullPointerException e) {
			return false;
		}
		
		return servPersistencia.borrarEntidad(eUsuario);
	}

	public void update(Usuario usuario) throws DAOException {
		try {
			String premiumstr = Boolean.toString(usuario.esPremium());
			String descuentostr = usuario.getDescuento().getClass().getName();
			String playliststr = this.obtenerCodigosListas(
					usuario.getListasCanciones(), adaptadorListasCanciones);
			String recientestr = this.obtenerCodigosCanciones(
					usuario.getCancionesRecientes(), adaptadorCancion);
			
			Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getId());
			servPersistencia.eliminarPropiedadEntidad(eUsuario, PASSWORD);
			servPersistencia.anadirPropiedadEntidad(eUsuario, PASSWORD, usuario.getPassword());
			servPersistencia.eliminarPropiedadEntidad(eUsuario, EMAIL);
			servPersistencia.anadirPropiedadEntidad(eUsuario, EMAIL, usuario.getEmail());
			servPersistencia.eliminarPropiedadEntidad(eUsuario, LOGIN);
			servPersistencia.anadirPropiedadEntidad(eUsuario, LOGIN, usuario.getLogin());
			servPersistencia.eliminarPropiedadEntidad(eUsuario, NOMBRE);
			servPersistencia.anadirPropiedadEntidad(eUsuario, NOMBRE, usuario.getNombre());
			servPersistencia.eliminarPropiedadEntidad(eUsuario, APELLIDOS);
			servPersistencia.anadirPropiedadEntidad(eUsuario, APELLIDOS, usuario.getApellidos());
			servPersistencia.eliminarPropiedadEntidad(eUsuario, FECHA_NACIMIENTO);
			servPersistencia.anadirPropiedadEntidad(eUsuario, FECHA_NACIMIENTO, usuario.getFechaNacimiento());
			servPersistencia.eliminarPropiedadEntidad(eUsuario, PREMIUM);
			servPersistencia.anadirPropiedadEntidad(eUsuario, PREMIUM, premiumstr);
			servPersistencia.eliminarPropiedadEntidad(eUsuario, DESCUENTO);
			servPersistencia.anadirPropiedadEntidad(eUsuario, DESCUENTO, descuentostr);
			servPersistencia.eliminarPropiedadEntidad(eUsuario, RECIENTES);
			servPersistencia.anadirPropiedadEntidad(eUsuario, RECIENTES, recientestr);
			servPersistencia.eliminarPropiedadEntidad(eUsuario, PLAYLISTS);
			servPersistencia.anadirPropiedadEntidad(eUsuario, PLAYLISTS, playliststr);
		} catch (NullPointerException e) {
			throw new DAOException("No existe ninguna entidad con este id en la base de datos");
		}
	}

	public Usuario get(int id) throws DAOException {
		try {
			Entidad eUsuario = servPersistencia.recuperarEntidad(id);
			return entidadToUsuario(eUsuario);
		} catch (NullPointerException e) {
			throw new DAOException("No existe ninguna entidad con este id en la base de datos");
		}
	}

	public List<Usuario> getAll() {
		List<Entidad> entidades = servPersistencia.recuperarEntidades(USUARIO);
		List<Usuario> usuarios = new LinkedList<Usuario>();
		try {
			for (Entidad eUsuario : entidades) {
				usuarios.add(get(eUsuario.getId()));
			}
		} catch (DAOException e) {
			System.err.println();
			e.printStackTrace();
		}
		
		return usuarios;
	}

}
