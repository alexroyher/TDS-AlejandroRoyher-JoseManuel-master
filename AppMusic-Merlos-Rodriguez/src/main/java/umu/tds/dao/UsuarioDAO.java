package umu.tds.dao;

import java.util.List;

import umu.tds.dominio.Usuario;

public interface UsuarioDAO {
	
	void create(Usuario asistente);
	boolean delete(Usuario asistente);
	void update(Usuario asistente) throws DAOException;
	Usuario get(int id) throws DAOException;
	List<Usuario> getAll();
	
}
