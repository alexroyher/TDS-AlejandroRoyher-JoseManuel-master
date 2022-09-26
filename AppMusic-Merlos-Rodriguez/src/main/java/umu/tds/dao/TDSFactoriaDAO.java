package umu.tds.dao;

/** 
 * Factoria concreta DAO para el Servidor de Persistencia de la asignatura TDS.
 * 
 */

public final class TDSFactoriaDAO extends FactoriaDAO {
	
	public TDSFactoriaDAO() {	}
	
	@Override
	public TDSUsuarioDAO getUsuarioDAO() {	
		return new TDSUsuarioDAO(); 
	}
	
	@Override
	public TDSCancionDAO getCancionDAO() {
		return new TDSCancionDAO();
	}
	
	@Override
	public TDSEstiloMusicalDAO getEstiloMusicalDAO() {
		return new TDSEstiloMusicalDAO();
	}
	
	@Override
	public TDSInterpreteDAO getInterpreteDAO() {
		return new TDSInterpreteDAO();
	}
	
	@Override
	public TDSListaCancionesDAO getListaCancionesDAO() {
		return new TDSListaCancionesDAO();
	}

}
