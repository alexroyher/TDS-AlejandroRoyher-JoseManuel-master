package umu.tds.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import javafx.util.Pair;

public class ListaRep extends JPanel {

	private JTable table_1;
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public ListaRep(JFrame frame, MainWindow window) {
		frame.getContentPane().add(this, BorderLayout.CENTER);
		this.setLayout(new BorderLayout(0, 0));
		
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
