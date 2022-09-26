package umu.tds.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import umu.tds.dominio.Cancion;
import umu.tds.dominio.EstiloMusical;
import umu.tds.dominio.Interprete;

public class TDSCancionDAOTest {

	TDSCancionDAO cancionDAO;
	Cancion cancion1;
	Cancion cancion2;
	Interprete interprete1;
	Interprete interprete2;
	EstiloMusical estilo;
	
	@Before
	public void setUp() {
		cancionDAO = new TDSCancionDAO();
		estilo = new EstiloMusical("K-pop");
		interprete1 = new Interprete("black pink");
		interprete2 = new Interprete("BTS");
		cancion1 = new Cancion("How you like that", "ruta/fichero", estilo, interprete1);
		cancion2 = new Cancion("Kill this love", "ruta/fichero", estilo, interprete1);
		interprete1.addCancion(cancion1);
	}

	@Test
	public void testUnaCancion() throws DAOException {
		cancionDAO.create(cancion1);
		Cancion cancionRecuperada = cancionDAO.get(cancion1.getId());
		assertEquals(cancionRecuperada.getTitulo(), cancion1.getTitulo());
	}
	
	@Test
	public void testVariasCanciones() throws DAOException {
		interprete1.addCancion(cancion2);
		cancionDAO.create(cancion1);
		Cancion cancionRecuperada = cancionDAO.get(cancion1.getId());
		ArrayList<Cancion> cancionesInterprete = cancionRecuperada.getInterprete().getCanciones();
		assertEquals(2, cancionesInterprete.size());
		assertNotEquals(cancionesInterprete.get(0).getTitulo(), cancionesInterprete.get(1).getTitulo());
	}
	
	@Test
	public void testVariasCancionesBorrandoPool() throws DAOException {
		interprete1.addCancion(cancion2);
		cancionDAO.create(cancion1);
		PoolDAO.getInstance().empty();
		Cancion cancionRecuperada = cancionDAO.get(cancion1.getId());
		ArrayList<Cancion> cancionesInterprete = cancionRecuperada.getInterprete().getCanciones();
		assertEquals(2, cancionesInterprete.size());
		assertNotEquals(cancionesInterprete.get(0).getTitulo(), cancionesInterprete.get(1).getTitulo());
	}
	
	@Test
	public void testVariosInterpretes() throws DAOException {
		cancion2.setInterprete(interprete2);
		interprete2.addCancion(cancion2);
		cancionDAO.create(cancion1);
		cancionDAO.create(cancion2);
		Cancion cancionrecuperada1 = cancionDAO.get(cancion1.getId());
		Cancion cancionrecuperada2 = cancionDAO.get(cancion2.getId());
		String nombre1 = cancionrecuperada1.getInterprete().getNombre();
		String nombre2 = cancionrecuperada2.getInterprete().getNombre();
		assertNotEquals(nombre1, nombre2);
	}
	
	@Test
	public void testVariosInterpretesBorrandoPool() throws DAOException {
		cancion2.setInterprete(interprete2);
		interprete2.addCancion(cancion2);
		cancionDAO.create(cancion1);
		cancionDAO.create(cancion2);
		PoolDAO.getInstance().empty();
		Cancion cancionrecuperada1 = cancionDAO.get(cancion1.getId());
		Cancion cancionrecuperada2 = cancionDAO.get(cancion2.getId());
		String nombre1 = cancionrecuperada1.getInterprete().getNombre();
		String nombre2 = cancionrecuperada2.getInterprete().getNombre();
		assertNotEquals(nombre1, nombre2);
	}
	
	@Test
	public void testDobleInsercion() {
		cancionDAO.create(cancion1);
		int id1 = cancion1.getId();
		cancionDAO.create(cancion1);
		int id2 = cancion1.getId();
		assertEquals(id1, id2);
	}

}
