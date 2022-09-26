package umu.tds.dominio;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import umu.tds.dao.DAOException;
import umu.tds.dao.FactoriaDAO;

public class CatalogoUsuarios {
	private static CatalogoUsuarios unicaInstancia;
	private FactoriaDAO factoria;

	private HashMap<String, Usuario> asistentesPorLogin;

	public static CatalogoUsuarios getUnicaInstancia() throws DAOException {
		if (unicaInstancia == null) unicaInstancia = new CatalogoUsuarios();
		return unicaInstancia;
	}
	
	private CatalogoUsuarios () throws DAOException{
		asistentesPorLogin = new HashMap<String, Usuario>();
		

			factoria = FactoriaDAO.getInstancia();
			
			List<Usuario> listaAsistentes = factoria.getUsuarioDAO().getAll();
			for (Usuario usuario : listaAsistentes) {
				asistentesPorLogin.put(usuario.getLogin(), usuario);
			}

	}
	
	public List<Usuario> getUsuarios() throws DAOException {
		return new LinkedList<Usuario>(asistentesPorLogin.values());
	}
	
	public Usuario getUsuario(String login) {
		return asistentesPorLogin.get(login);
	}

	
	public void addUsuario(Usuario usuario) {
		asistentesPorLogin.put(usuario.getLogin(), usuario);
	}
	
	public void removeUsuario(Usuario usuario) {
		asistentesPorLogin.remove(usuario.getLogin());
	}

}
