package umu.tds.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import javafx.util.Pair;

public class AddPlayList extends JPanel {

	private JPanel searchPanel;
	private MainWindow window;
	
	private JTextField txtFldInterprete;
	private JTextField txtFldTitulo;
	private JComboBox<String> comboBox;
	
	private JTable table_1;
	private JTable table_2;
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public AddPlayList(JFrame frame, MainWindow window) {
		this.window = window;
		
		frame.getContentPane().add(this, BorderLayout.CENTER);
		this.setLayout(new BorderLayout(0, 0));
		
		searchPanel = new JPanel();
		this.add(searchPanel, BorderLayout.NORTH);
		frame.remove(searchPanel);
		searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
		
		JPanel panel_5 = new JPanel();
		searchPanel.add(panel_5);
		panel_5.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JTextField nombrebtn = new JTextField();
		nombrebtn.setText("Nombre");
		window.addMouseEraser(nombrebtn);
		panel_5.add(nombrebtn);
		nombrebtn.setColumns(10);
		
		JButton crearbtn = new JButton("Crear");
		panel_5.add(crearbtn);
		window.addManejadorBtnCrear(crearbtn,nombrebtn);
	}
	
	public void showSelector() {
		Box horizontalBox = Box.createHorizontalBox();
		searchPanel.add(horizontalBox);
		
		JPanel panel_3 = new JPanel();
		horizontalBox.add(panel_3);
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.X_AXIS));
		panel_3.add(Box.createHorizontalStrut(10));
		
		txtFldInterprete = new JTextField();
		txtFldInterprete.setText("Interprete");
		panel_3.add(txtFldInterprete);
		txtFldInterprete.setColumns(7);
		window.addMouseEraser(txtFldInterprete);
		txtFldTitulo = new JTextField();
		txtFldTitulo.setText("Titulo");
		panel_3.add(txtFldTitulo);
		txtFldTitulo.setColumns(7);
		window.addMouseEraser(txtFldTitulo);
		comboBox = new JComboBox<String>();
	    window.createComboBox(comboBox);
		panel_3.add(comboBox);
		
		JButton btnBuscar = new JButton("Buscar");
		window.addManejadorBuscarAnadir(btnBuscar);
		panel_3.add(btnBuscar);
		
		JPanel mediapanel = new JPanel();
		this.add(mediapanel, BorderLayout.SOUTH);
		
		JButton aceptarbtn = new JButton("Aceptar");
		mediapanel.add(aceptarbtn);
		window.addManejadorAceptarCreatePlayList(aceptarbtn);
		
		JButton cancelarbtn = new JButton("Cancelar");
		window.addManejadorCancelar(cancelarbtn);
		mediapanel.add(cancelarbtn);
		
		JPanel centerpanel = new JPanel();
		this.add(centerpanel, BorderLayout.CENTER);
		
		centerpanel.setLayout(new BoxLayout(centerpanel, BoxLayout.X_AXIS));

		
		table_1 = new JTable();
		String[][] content = getArrayPair(window.getLista1());
		table_1.setModel(new DefaultTableModel(
			content,
			new String[] {
				"Titulo", "Interprete"
			}
		));
		table_1.setPreferredScrollableViewportSize(new Dimension(260, 200));
		JScrollPane scrollPane_1 = new JScrollPane(table_1);
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		centerpanel.add(Box.createHorizontalStrut(20));
		centerpanel.add(scrollPane_1);
		
		
		table_2 = new JTable();
		String[][] content2 = getArrayPair(window.getLista2());
		table_2.setModel(new DefaultTableModel(
			content2,
			new String[] {
				"Titulo", "Interprete"
			}
		));
		
		centerpanel.add(Box.createHorizontalStrut(20));
		
		table_2.setPreferredScrollableViewportSize(new Dimension(260, 200));
		JScrollPane scrollPane_2 = new JScrollPane(table_2);
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		JPanel panel_2 = new JPanel();
		centerpanel.add(panel_2);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.Y_AXIS));
		
		JButton anadir = new JButton("          >>         ");
		panel_2.add(anadir);
		window.addManejadorAnadir(anadir);
		
		panel_2.add(Box.createVerticalStrut(10));
		
		JButton retirar = new JButton("          <<         ");
		panel_2.add(retirar);
		window.addManejadorRetirar(retirar);
		
		centerpanel.add(Box.createHorizontalStrut(20));
		
		centerpanel.add(scrollPane_2);
		
		centerpanel.add(Box.createHorizontalStrut(20));
		window.setFldInterprete(this.txtFldInterprete);
		window.setFldTitulo(this.txtFldTitulo);
		window.setComboBox(this.comboBox);
		window.setTable1(this.table_1);
		window.setTable2(this.table_2);
	}
	
	private String[][] getArrayPair(ArrayList<Pair<String,String>> lista) {
		String[][] content = new String[lista.size()][2];
		for(int i =0;i<lista.size();i++) {
			content[i][0] = lista.get(i).getKey();
			content[i][1] = lista.get(i).getValue();
		}
		return content;
	}

}
