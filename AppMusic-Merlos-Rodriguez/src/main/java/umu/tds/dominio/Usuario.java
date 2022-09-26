package umu.tds.dominio;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class Usuario {
	
	private int id;
	private String nombre;
	private String apellidos;
	private String email;
	private String login;
	private String password;
	private String fechaNacimiento;
	private boolean premium;
	private Descuento descuento;
	private Queue<Cancion> recientes;
	private ArrayList<ListaCanciones> playlists;

	public Usuario(UsuarioBuilder usuarioBuilder) {
		this.nombre = usuarioBuilder.nombre;
		this.apellidos = usuarioBuilder.apellidos;
		this.email = usuarioBuilder.email;
		this.login = usuarioBuilder.login;
		this.password = usuarioBuilder.password;
		this.fechaNacimiento = usuarioBuilder.fechaNacimiento;
		this.descuento = aplicarDescuento();
		this.recientes = new LinkedList<Cancion>();
		this.playlists = new ArrayList<ListaCanciones>();
		this.id = 0;
		this.premium = false;

	}

	private Descuento aplicarDescuento() {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date d = new Date();
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
		try {
			d = df.parse(this.fechaNacimiento);
			cal.setTime(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date now = new Date();
		Calendar cal2 = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
		cal2.setTime(now);
		int curr = cal2.get(Calendar.YEAR);
		int nac = cal.get(Calendar.YEAR);
		if (curr-nac <= 20) return new DescuentoJovenes();
		else return new DescuentoFijo();
	}

	public void addRecientes(Cancion cancion) {
			if(recientes.size()==10) recientes.remove();
			this.recientes.add(cancion);
	}
	
	public ListaCanciones addPlayList(String lista) {
		if (this.hasPlayList(lista)) return null;
		ListaCanciones newPlayList = new ListaCanciones(lista);
		this.playlists.add(newPlayList);
		return newPlayList;
	}
	
	public boolean hasPlayList(String lista) {
		return !this.playlists.stream().filter(d->d.getNombre().equals(lista)).collect(Collectors.toList()).isEmpty();
	}
	
	public ArrayList<Cancion> getCancionesRecientes(){
		return new ArrayList<Cancion>(this.recientes);
	}
	
	public ArrayList<ListaCanciones> getListasCanciones(){
		return new ArrayList<ListaCanciones>(this.playlists);
	}
	public Descuento getDescuento() {
		return descuento;
	}

	public void setDescuento(Descuento descuento) {
		this.descuento = descuento;
	}
	public void setPremium() {
		this.premium = true;
	}
	
	public boolean esPremium() {
		return this.premium;
	}
	public int getId() {
		return id;
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

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFechaNacimiento() {
		return fechaNacimiento;
	}
	
	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public ListaCanciones getListaByName(String listNombre) {
		ArrayList<ListaCanciones> lista = (ArrayList<ListaCanciones>) this.playlists.stream().filter(d->d.getNombre().equals(listNombre)).
				collect(Collectors.toList());
		return lista.get(0);
	}
	
	public static class UsuarioBuilder {
		private String nombre;
		private String apellidos;
		private String email;
		private String login;
		private String password;
		private String fechaNacimiento;
		
		public UsuarioBuilder(String _nombre) {
			this.nombre = _nombre;
		}
		
		public UsuarioBuilder apellidos(String _apellidos) {
			this.apellidos = _apellidos; 
			return this;
		}
		
		public UsuarioBuilder email(String _email) {
			this.email= _email;
			return this;
		}
		
		public UsuarioBuilder login(String _login) {
			this.login = _login;
			return this;
		}
		
		public UsuarioBuilder password(String _password) {
			this.password = _password;
			return this;
		}
		
		public UsuarioBuilder fechaNacimiento(String _fecha) {
			this.fechaNacimiento = _fecha;
			return this;
		}
		
		public Usuario build() {
			return new Usuario(this);
		}
	}

	public String getNombreDescuento() {
		return this.descuento.getNombre();
	}

}
