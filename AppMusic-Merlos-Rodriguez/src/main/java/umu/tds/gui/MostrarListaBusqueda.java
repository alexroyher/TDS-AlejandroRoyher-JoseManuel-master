package umu.tds.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
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

public class MostrarListaBusqueda extends JPanel {

	private JTextField txtFldInterprete;
	private JTextField txtFldTitulo;
	private JComboBox<String> comboBox;
	
	private JTable table_1;
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public MostrarListaBusqueda(JFrame frame, MainWindow window) {
		frame.getContentPane().add(this, BorderLayout.CENTER);
		this.setLayout(new BorderLayout(0, 0));
		
		JPanel searchPanel = new JPanel();
		this.add(searchPanel, BorderLayout.NORTH);
		frame.remove(searchPanel);
		searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
		
		Box horizontalBox = Box.createHorizontalBox();
		searchPanel.add(horizontalBox);
		
		JPanel panel_3 = new JPanel();
		horizontalBox.add(panel_3);
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.X_AXIS));
		panel_3.add(Box.createHorizontalStrut(10));
		
		txtFldInterprete = new JTextField();
		txtFldInterprete.setText("Interprete");
		window.addMouseEraser(txtFldInterprete);
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
		
		
		JPanel panel = new JPanel();
		searchPanel.add(panel);
		
		JPanel panel_4 = new JPanel();
		panel.add(panel_4);
		
		JButton btnBuscar = new JButton("Buscar");
		
		window.addManejadorBuscar(btnBuscar);
		
		panel_4.add(btnBuscar);
		
		JButton btnCanelar = new JButton("Cancelar");
		window.addManejadorCancelar(btnCanelar);
		panel_4.add(btnCanelar);
		
		JPanel mediaPanel = new MediaPanel(frame, window);
		this.add(mediaPanel, BorderLayout.SOUTH);
		
		JPanel centerpanel = new JPanel();
		this.add(centerpanel, BorderLayout.CENTER);
		
		
		table_1 = new JTable();
		
		String[][] content = getArrayPair(window.getLista1());
		table_1.setModel(new DefaultTableModel(content,
			new String[] {
				"Titulo", "Interprete"
			}
		));
		table_1.setPreferredScrollableViewportSize(new Dimension(260, 200));
		JScrollPane scrollPane_1 = new JScrollPane(table_1);
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		centerpanel.add(scrollPane_1);
		
		window.setFldInterprete(this.txtFldInterprete);
		window.setFldTitulo(this.txtFldTitulo);
		window.setComboBox(this.comboBox);
		window.setTable1(this.table_1);
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
