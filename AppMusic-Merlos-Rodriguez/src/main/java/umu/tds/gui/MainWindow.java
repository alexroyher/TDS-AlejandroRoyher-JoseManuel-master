package umu.tds.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventObject;
import java.util.stream.Collectors;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;

import com.itextpdf.text.DocumentException;

import javafx.util.Pair;
import umu.tds.componente.CargadorCanciones;
import umu.tds.controlador.Controlador;
import umu.tds.dao.DAOException;
import umu.tds.dominio.Cancion;
import umu.tds.dominio.ListaCanciones;
import java.awt.Color;
import javax.swing.JTable;

import pulsador.IEncendidoListener;
import pulsador.Luz;

public class MainWindow {

	private Controlador controlador;
	private JFrame frame;
	private JPanel contentPane;
	private JPanel variablePanel;
	private JTextField txtFldInterprete;
	private JTextField txtFldTitulo;
	private JTable table_1;
	private JTable table_2;
	private JPanel listPanel;
	private JPanel panel_1;
	
	private ArrayList<Pair<String,String>> listaglob;
	private ArrayList<Pair<String,String>> listaglob2;
	private JComboBox<String> comboBox;
	private boolean listaDesplegada;
	private ArrayList<String> listasglob;
	private String currentPlaylist;
	private boolean enCreacion;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}
	public void mostrarVentana() {
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	/**
	 * Initialize the contents of the frame.
	 */
	
	public void setFldInterprete(JTextField txtfld) {
		this.txtFldInterprete = txtfld;
	}
	
	public void setFldTitulo(JTextField txtfld) {
		this.txtFldTitulo = txtfld;
	}
	
	public void setComboBox(JComboBox<String> comboBox) {
		this.comboBox = comboBox;
	}
	
	public void setTable1(JTable table) {
		this.table_1 = table;
	}
	
	public void setTable2(JTable table) {
		this.table_2 = table;
	}
	
	public  ArrayList<Pair<String,String>> getLista1() {
		return this.listaglob;
	}
	
	public  ArrayList<Pair<String,String>> getLista2() {
		return this.listaglob2;
	}
	
	private void initialize() {
		frame = new JFrame();
		try {
			controlador = Controlador.getUnicaInstancia();
		} catch (DAOException e) {
			JOptionPane.showMessageDialog(frame, "Error al inicializar la base de datos",
					"Error", JOptionPane.ERROR_MESSAGE);
		}
		frame.setBounds(100, 100, 750, 470);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.GRAY);
		contentPane.setForeground(Color.GRAY);
		frame.getContentPane().add(contentPane, BorderLayout.WEST);
		contentPane.setBorder(new LineBorder(Color.black));
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_panel);
		
		createBtnExplorar(contentPane);
		
		createLftBtns();
		
		panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		
		String nombreUsuario = controlador.getUsuarioActual().getNombre();
		JLabel saludoUsuario = new JLabel("Hola " + nombreUsuario + "!");
		panel_1.add(saludoUsuario);
		
		Luz botonLuz = new Luz();
		botonLuz.setColorInferior(Color.BLACK);
		panel_1.add(botonLuz);
		
		botonLuz.addEncendidoListener(new IEncendidoListener() {
			
			@Override
			public void enteradoCambioEncendido(EventObject e) {
				 JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				 int returnValue = jfc.showOpenDialog(null);
				 if(returnValue == JFileChooser.APPROVE_OPTION) {
					 String path = jfc.getSelectedFile().getAbsolutePath();
					 CargadorCanciones cargador = new CargadorCanciones();
					 cargador.addCancionesListener(controlador);
					 cargador.setArchivoCanciones(path);
				 }
			}
		});
		
		if (controlador.esPremium()) {
			JButton btnGenerarPDF = new JButton("PDF");
			addManejadorPDF(btnGenerarPDF);
			panel_1.add(btnGenerarPDF);
			JButton btnMasEscuchadas = new JButton("Trending");
			addManejadorTrending(btnMasEscuchadas);
			panel_1.add(btnMasEscuchadas);
		}
		
		JButton btnMejorarMiCuenta = new JButton("Mejorar mi cuenta");
		addManejadorMejorarCuenta(btnMejorarMiCuenta);
		panel_1.add(btnMejorarMiCuenta);
		
		createBtnLogout(panel_1);
		
		variablePanel = new JPanel();
		frame.getContentPane().add(variablePanel, BorderLayout.CENTER);
	}

	

	private void createLftBtns() {
		JButton btnNewButton_1 = new JButton("➕ Nueva Lista ");
		AddManejadorAnadiLista(btnNewButton_1);
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_1.gridx = 0;
		gbc_btnNewButton_1.gridy = 1;
		contentPane.add(btnNewButton_1, gbc_btnNewButton_1);
		
		JButton btnNewButton_4 = new JButton("↺   Recientes  ");
		AddManejadorRecientes(btnNewButton_4);
		GridBagConstraints gbc_btnNewButton_4 = new GridBagConstraints();
		gbc_btnNewButton_4.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_4.gridx = 0;
		gbc_btnNewButton_4.gridy = 2;
		contentPane.add(btnNewButton_4, gbc_btnNewButton_4);
		
		JButton btnMisListas = new JButton("♫  Mis listas   ");
		addManejadorMisListas(btnMisListas);
		GridBagConstraints gbc_btnMisListas = new GridBagConstraints();
		gbc_btnMisListas.gridx = 0;
		gbc_btnMisListas.gridy = 3;
		contentPane.add(btnMisListas, gbc_btnMisListas);
	}
	
	private void createMostrarLista() {
		frame.remove(variablePanel);
		variablePanel = new MostrarListaBusqueda(frame, this);
		contentPane.revalidate();
		contentPane.repaint();
		frame.validate();
	}

	public void createComboBox(JComboBox<String> box) {
		ArrayList<String> estilos;
		try {
			estilos = controlador.getNombresEstilos();
		} catch (DAOException e) {
			JOptionPane.showMessageDialog(frame, "Error de acceso a base de datos",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
	    for(String estilo : estilos) {
	    	box.addItem(estilo);
	    }
	}
	
	private void createAddPlaylist() {
		enCreacion = false;
		frame.remove(variablePanel);
		variablePanel = new AddPlayList(frame, this);
		contentPane.revalidate();
		contentPane.repaint();
		frame.validate();
	}
	
	private void createExplorarWindow() {
		frame.remove(variablePanel);
		this.variablePanel = new PanelPreBusqueda(frame, this);
		contentPane.revalidate();
		contentPane.repaint();
		frame.validate();
	}
	
	private void createListaRep() {
		
		frame.remove(variablePanel);
		variablePanel = new ListaRep(frame, this);
		contentPane.revalidate();
		contentPane.repaint();
		frame.validate();
	}

	private void createListaListas() {
		listPanel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 4;
		contentPane.add(listPanel, gbc_panel);
		
		DefaultListModel<String> model = new DefaultListModel<String>();
		for(String s : listasglob) {
			model.addElement(s);
		}
		JList<String> list = new JList<String>(model);
		listPanel.add(list);
		addControladorSeleccionarLista(list);
		contentPane.revalidate();
		contentPane.repaint();
		frame.validate();
	}

	private void createBtnExplorar(JPanel panel) {
		JButton btnExplorar = new JButton("⚲    Explorar    ");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 0;
		AddManejadorExplorar(btnExplorar);
		panel.add(btnExplorar, gbc_btnNewButton);
	}

	private void createBtnLogout(JPanel panel) {
		JButton btnLogout = new JButton("LogOut");
		AddManejadorLogOut(btnLogout);
		panel.add(btnLogout);
	}
	private void addControladorSeleccionarLista(JList<String> list) {
	    ListSelectionModel selmode = list.getSelectionModel();
	    selmode.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int index = list.getSelectedIndex();
	            String listNombre = listasglob.get(index);
	            ListaCanciones list = controlador.getCancionesPlayList(listNombre);
	    		listaglob = new ArrayList<Pair<String,String>>(
	    				list.getCanciones().stream().map(d->new Pair<String,String>(d.getTitulo(),
	    				d.getInterpreteName())).collect(Collectors.toList()));
	            createListaRep();
			}
		});
	}
	private void AddManejadorRecientes(JButton btnRecientes) {
		btnRecientes.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ArrayList<Cancion> canciones = controlador.getCancionesRec();
				listaglob =  new ArrayList<Pair<String,String>>(canciones.stream().map(d->new Pair<String,String>(d.getTitulo(),
						d.getInterpreteName())).collect(Collectors.toList()));
				Collections.reverse(listaglob);
				createListaRep();
				
			}
		});
	}
	public void AddManejadorExplorar(JButton btnExplorar) {
		btnExplorar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				createExplorarWindow();
				
			}
		});
	}
	
	public void AddManejadorAnadiLista(JButton btnNueva) {
		btnNueva.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				createAddPlaylist();
				
			}
		});
	}
	public void addManejadorBtnCrear(JButton crearbtn, JTextField nombre) {
		crearbtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (nombre.getText().isEmpty()) {
					JOptionPane.showMessageDialog(frame, "Es necesario introducir un nombre para la "
							+ "playlist");
					return;
				}
				if (enCreacion) {
					JOptionPane.showMessageDialog(frame, "Ya nos encontramos en proceso de creación"
							+ " de la playlist " + nombre.getText());
					return;
				}
				else {
					enCreacion = true;
				}
				if (controlador.existePlayList(nombre.getText())) {
					JOptionPane.showMessageDialog(frame, "Ya existe una PlayList"
							+ " con ese nombre, prueba con otro.");
					enCreacion = false;
				}
				else {
					JOptionPane.showMessageDialog(frame, "Se ha creado la Play"
							+ "List correctamente.");
					currentPlaylist = nombre.getText();
					listaglob = new ArrayList<Pair<String,String>>(controlador.getAllCanciones().stream().
							map(d->new Pair<String,String>(d.getTitulo(),d.getInterpreteName())).collect(Collectors.toList()));
					listaglob2 = new ArrayList<Pair<String,String>>();
					AddPlayList panel = (AddPlayList) variablePanel;
					panel.showSelector();
					contentPane.revalidate();
					contentPane.repaint();
					frame.validate();
				}
				
			}
		});
		
	}

	
	public void AddManejadorLogOut(JButton btnLogout) {
		btnLogout.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				controlador.stopCancion();
				frame.dispose();
			}
		});
	}
	
	public void addManejadorBuscar(JButton btnBuscar) {
		btnBuscar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.remove(variablePanel);
				ArrayList<Cancion> filtrado  = controlador.filtrarCanciones(
						txtFldTitulo.getText(), txtFldInterprete.getText(), 
						(String) comboBox.getSelectedItem());
				listaglob = new ArrayList<Pair<String,String>>(filtrado.stream().
						map(d->new Pair<String,String>(d.getTitulo(),d.getInterpreteName())).collect(Collectors.toList()));
				createMostrarLista();
				
			}
		});
	}
	
	public void addManejadorMisListas(JButton btnMisListas) {
		btnMisListas.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(listaDesplegada) {
					listPanel.setVisible(false);
					listaDesplegada = false;
				}
				else {
					listaDesplegada = true;
					listasglob = controlador.getNombresListas();
					createListaListas();
				}
			}
		});
	}
	
	public void addManejadorCancelar(JButton btnCancelar) {
		btnCancelar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.remove(variablePanel);
				variablePanel = new JPanel();
				frame.getContentPane().add(variablePanel, BorderLayout.CENTER);
				contentPane.revalidate();
				contentPane.repaint();
				frame.validate();
			}
		});
	}
	public void addMouseEraser(JTextField field) {
		field.addMouseListener(new MouseAdapter() {
			  @Override
			  public void mouseClicked(MouseEvent e) {
			    field.setText("");
			  }
			});
		
	}
	public void addManejadorPlay(JButton btnPlay) {
		btnPlay.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int row = table_1.getSelectedRow();
				if (row < 0) return;
				String titulo = (String) table_1.getModel().getValueAt(row, 0);
				try {
					controlador.playByName(titulo);
				} catch (DAOException e) {
					JOptionPane.showMessageDialog(frame, "Error de acceso a la base de datos",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	
	private void addManejadorMejorarCuenta(JButton btnMejorarMiCuenta) {
		btnMejorarMiCuenta.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (controlador.esPremium()) {
					JOptionPane.showMessageDialog(frame, "Ya eres usuario Premium!");
				}
				else {
					String desc = "";
					try {
						desc = controlador.hacerUsuarioPremium();
					} catch (DAOException e) {
						System.out.println("Ha ocurrido un error al pasar a Premium :(");
					}
					JOptionPane.showMessageDialog(frame, "Se ha realizado la mejora satisfactoriamente"
							+ " y se he aplicado el descuento : " + desc );
					JButton btnGenerarPDF = new JButton("PDF");
					addManejadorPDF(btnGenerarPDF);
					panel_1.add(btnGenerarPDF);
					JButton btnMasEscuchadas = new JButton("Trending");
					addManejadorTrending(btnMasEscuchadas);
					panel_1.add(btnMasEscuchadas);
					frame.validate();
				}
			}
		});		
	}
	
	public void addManejadorRetrocederCancion(JButton btnRet) {
		btnRet.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int row = table_1.getSelectedRow();
				if (row < 0) return;
				if (row == 0)
					row = table_1.getModel().getRowCount()-1;
				else row--;
				table_1.setRowSelectionInterval(row, row);
				String titulo = (String) table_1.getModel().getValueAt(row, 0);
				try {
					controlador.playByName(titulo);
				} catch (DAOException e) {
					JOptionPane.showMessageDialog(frame, "Error de acceso a base de datos",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
	}
	
	public void addManejadorAvanzarCancion(JButton btnNext) {
		btnNext.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int row = table_1.getSelectedRow();
				if (row < 0) return;
				if (row == table_1.getModel().getRowCount()-1)
					row = 0;
				else row++;
				table_1.setRowSelectionInterval(row, row);
				String titulo = (String) table_1.getModel().getValueAt(row, 0);
				try {
					controlador.playByName(titulo);
				} catch (DAOException e) {
					JOptionPane.showMessageDialog(frame, "Error de acceso a la base de datos",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
	}
	
	public void addManejadorAceptarCreatePlayList(JButton aceptarbtn) {
		aceptarbtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int count= table_2.getModel().getRowCount(); 
				if(count == 0 ) {
					JOptionPane.showMessageDialog(frame, "No se ha seleccionado ninguna canción!");
				}
				else {
					ArrayList<String> list = new ArrayList<String>();
					for(int i = 0;i<table_2.getModel().getRowCount();i++)
					{
						list.add((String)table_2.getModel().getValueAt(i,0));
					}
					try {
						controlador.createPlayList(currentPlaylist);
						controlador.addSongPlayList(list, currentPlaylist);
					} catch (DAOException e) {
						JOptionPane.showMessageDialog(frame, "Error de acceso a base de datos",
								"Error", JOptionPane.ERROR_MESSAGE);
					}
					JOptionPane.showMessageDialog(frame, "Se han guardado correctamente las canciones");
				}
			}
		});
		
	}

	public void addManejadorRetirar(JButton retirar) {
		retirar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int row = table_2.getSelectedRow();
				if (row == -1) {
					JOptionPane.showMessageDialog(frame,
							"No se ha seleccionado ninguna cancion para retirar");
				}
				else {
					DefaultTableModel model = (DefaultTableModel) table_2.getModel();
					model.removeRow(row);
				}
			}
		});
		
	}

	public void addManejadorAnadir( JButton anadir) {
		anadir.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int row = table_1.getSelectedRow();
				if (row == -1) {
					JOptionPane.showMessageDialog(frame,
							"No se ha seleccionado ninguna cancion para añadir");
				}
				else {
					String titulo = (String) table_1.getModel().getValueAt(row, 0);
					String interp = (String) table_1.getModel().getValueAt(row, 1);
					String[] song = new String[2];
					song[0] = titulo;
					song[1] = interp;
					DefaultTableModel model = (DefaultTableModel) table_2.getModel();
					model.addRow(song);
					
				}
						
			}
			
		});
	}
	
	public void addManejadorBuscarAnadir(JButton btnBuscar) {
		btnBuscar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ArrayList<Cancion> res = controlador.filtrarCanciones(
						txtFldTitulo.getText(), txtFldInterprete.getText(), (String) comboBox.getSelectedItem());
				listaglob = new ArrayList<Pair<String,String>>(res.stream().
						map(d->new Pair<String,String>(d.getTitulo(),d.getInterpreteName())).collect(Collectors.toList()));
				String[][] content = getArrayPair(listaglob);
				table_1.setModel(new DefaultTableModel(content,
					new String[] {
						"Titulo", "Interprete"
					}
				));
				
			}
		});
	}
	
	public void addManejadorPDF(JButton btnGenerarPDF) {
		btnGenerarPDF.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				 int returnValue = jfc.showOpenDialog(null);
				 if(returnValue == JFileChooser.APPROVE_OPTION) {
					 String path = jfc.getSelectedFile().getAbsolutePath();
					 try {
						controlador.generatePDF(path);
					} catch (FileNotFoundException | DocumentException e) {
						JOptionPane.showMessageDialog(frame, "Se ha producido un error generando"
								+ " el PDF de las listas, asegúrate de que existe al menos una lista"
								+ " creada.");
					}
				 }
				
			}
		});
	}
	
	public void addManejadorTrending(JButton btnMasEscuchadas) {
		btnMasEscuchadas.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
					ArrayList<Cancion> canciones = controlador.getCancionesMasRep();
		    		listaglob = new ArrayList<Pair<String,String>>(
		    				canciones.stream().map(d->new Pair<String,String>(d.getTitulo(),
		    				d.getInterpreteName())).collect(Collectors.toList()));
					createListaRep();
				}
		});
	}
	
	private String[][] getArrayPair(ArrayList<Pair<String,String>> lista) {
		String[][] content = new String[lista.size()][2];
		for(int i =0;i<lista.size();i++) {
			content[i][0] = lista.get(i).getKey();
			content[i][1] = lista.get(i).getValue();
		}
		return content;
	}

	public void addManejadorPause(JButton btnNewButton_3) {
		btnNewButton_3.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controlador.pauseCancion();	
			}
		});
	}
	
}

