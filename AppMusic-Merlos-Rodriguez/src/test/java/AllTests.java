import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import umu.tds.controlador.ControladorTest;
import umu.tds.dao.TDSCancionDAOTest;
import umu.tds.dao.TDSUsuarioDAOTest;

@RunWith(Suite.class)
@SuiteClasses({ControladorTest.class, TDSCancionDAOTest.class, TDSUsuarioDAOTest.class})
public class AllTests {

}
