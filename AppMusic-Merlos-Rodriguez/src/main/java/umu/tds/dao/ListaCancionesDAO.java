package umu.tds.dao;

import java.util.List;

import umu.tds.dominio.ListaCanciones;

public interface ListaCancionesDAO {
	void create(ListaCanciones asistente);
	boolean delete(ListaCanciones asistente);
	void update(ListaCanciones asistente) throws DAOException;
	ListaCanciones get(int id) throws DAOException;
	List<ListaCanciones> getAll();
}
