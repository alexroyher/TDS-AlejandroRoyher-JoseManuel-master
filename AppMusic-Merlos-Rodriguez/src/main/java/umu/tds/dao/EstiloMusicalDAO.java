package umu.tds.dao;

import java.util.List;

import umu.tds.dominio.EstiloMusical;

public interface EstiloMusicalDAO {

    void create(EstiloMusical asistente);
    boolean delete(EstiloMusical asistente);
    void update(EstiloMusical asistente) throws DAOException;
    EstiloMusical get(int id) throws DAOException;
    List<EstiloMusical> getAll();

}