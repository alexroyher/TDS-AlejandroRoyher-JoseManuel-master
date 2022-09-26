package umu.tds.dao;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import umu.tds.dominio.DescuentoFijo;
import umu.tds.dominio.Usuario;

public class TDSUsuarioDAOTest {
	
	TDSUsuarioDAO usuarioDAO;
	Usuario usuario1;
	
	@Before
	public void setUp() {
		usuarioDAO = new TDSUsuarioDAO();
		usuario1 = new Usuario.UsuarioBuilder("Jhonny").apellidos("Jhonnson").email("Jhonny@Jhonnson.jh").password("bingo").fechaNacimiento("08/06/1992").build();
		usuario1.setDescuento(new DescuentoFijo());
	}

	@Test
	public void testSimple() throws DAOException {
		usuarioDAO.create(usuario1);
		Usuario usuarioRecuperado = usuarioDAO.get(usuario1.getId());
		assertEquals(usuario1.getNombre(), usuarioRecuperado.getNombre());
	}
	
	@Test
	public void testUpdate() throws DAOException {
		usuarioDAO.create(usuario1);
		usuario1.setPremium();
		usuarioDAO.update(usuario1);
		Usuario usuarioRecuperado = usuarioDAO.get(usuario1.getId());
		assertEquals(usuario1.esPremium(), usuarioRecuperado.esPremium());
	}
	
	@Test
	public void testDobleInsercion() {
		usuarioDAO.create(usuario1);
		int id1 = usuario1.getId();
		usuarioDAO.create(usuario1);
		int id2 = usuario1.getId();
		assertEquals(id1, id2);
	}

}
