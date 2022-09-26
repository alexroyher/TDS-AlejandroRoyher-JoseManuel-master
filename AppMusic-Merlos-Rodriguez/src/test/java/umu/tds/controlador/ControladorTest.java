package umu.tds.controlador;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import umu.tds.dao.CancionDAO;
import umu.tds.dao.DAOException;
import umu.tds.dao.FactoriaDAO;
import umu.tds.dominio.Cancion;
import umu.tds.dominio.CatalogoCanciones;
import umu.tds.dominio.CatalogoEstilo;
import umu.tds.dominio.CatalogoInterprete;
import umu.tds.dominio.CatalogoUsuarios;
import umu.tds.dominio.EstiloMusical;
import umu.tds.dominio.Interprete;
import umu.tds.dominio.ListaCanciones;
import umu.tds.dominio.Usuario;

public class ControladorTest {
	Usuario user;
	Controlador controller;
	Cancion cancion1;
	Cancion cancion2; 
	Interprete interprete1;
	Interprete interprete2;
	EstiloMusical estilo1;
	EstiloMusical estilo2;
	CancionDAO adaptadorCancion;
	FactoriaDAO factoria;

	
	@Before
	public void setUp() throws DAOException {
		try {
			factoria = FactoriaDAO.getInstancia();
		} catch (DAOException e) {
			e.printStackTrace();
		}
		adaptadorCancion = factoria.getCancionDAO();
		estilo1 = new EstiloMusical("uwu music");
		estilo2 = new EstiloMusical("owo music");
		interprete1 = new Interprete("los std");
		interprete2 = new Interprete("los peris");
		cancion1 = new Cancion("uwuwuuw" , "ruta/fichero", estilo1, interprete1);
		cancion2 = new Cancion("owowow", "ruta/fichero", estilo2, interprete2);
		interprete1.addCancion(cancion1);
		interprete2.addCancion(cancion2);
		estilo1.addCancion(cancion1);
		estilo2.addCancion(cancion2);
		CatalogoCanciones.getUnicaInstancia().addCancion(cancion1);
		CatalogoCanciones.getUnicaInstancia().addCancion(cancion2);
		CatalogoEstilo.getUnicaInstancia().addEstilo(estilo1);
		CatalogoEstilo.getUnicaInstancia().addEstilo(estilo2);
		CatalogoInterprete.getUnicaInstancia().addInterprete(interprete1);
		CatalogoInterprete.getUnicaInstancia().addInterprete(interprete2);
		controller =  new Controlador();
	}
	@Test
	public void testBusqueda() throws DAOException{
		ArrayList<Cancion> canciones =  controller.filtrarCanciones("uwu", "", estilo1.getNombre());
		assertEquals(canciones.get(0).getTitulo(), cancion1.getTitulo());
	}
	
	@Test
	public void testRegistroCorrecto() throws DAOException{
		controller.registrarUsuario("El", "Peri", "peri@um.es", "peri", "benitez", "08/01/1999");
		boolean res = controller.esUsuarioRegistrado("peri");
		assertEquals(res, true);
	}
	
	@Test
	public void testCreacionPlayList() throws DAOException{
		adaptadorCancion.create(cancion1);
		adaptadorCancion.create(cancion2);
		controller.registrarUsuario("Alejandro", "Rodriguez", "alex@um.es", "xd", "pass", "08/01/1999");
		user = CatalogoUsuarios.getUnicaInstancia().getUsuario("xd");
		controller.loginUsuario("xd", "pass");
		ListaCanciones playL = controller.createPlayList("lofi hip/hop");
		if (playL != null) {
			playL.addCancion(cancion1);
			ArrayList<ListaCanciones> listaUser = user.getListasCanciones();
			assertEquals(playL.getNombre(), listaUser.get(0).getNombre());
		}
	}

}
