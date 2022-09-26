package umu.tds.dao;

import java.util.List;

import umu.tds.dominio.Interprete;

public interface InterpreteDAO {

	void create(Interprete asistente);
	boolean delete(Interprete asistente);
	void update(Interprete asistente) throws DAOException;
	Interprete get(int id) throws DAOException;
	List<Interprete> getAll();
}
