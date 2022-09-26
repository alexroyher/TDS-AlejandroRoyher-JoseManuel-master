package umu.tds.dao;

import java.util.List;

import umu.tds.dominio.Cancion;

public interface CancionDAO {

	void create(Cancion asistente);
	boolean delete(Cancion asistente);
	void update(Cancion asistente) throws DAOException;
	Cancion get(int id) throws DAOException;
	List<Cancion> getAll();
	
	
}
