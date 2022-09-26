package umu.tds.gui;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PanelPreBusqueda extends JPanel {


	private JTextField txtFldInterprete;
	private JTextField txtFldTitulo;
	private JComboBox<String> comboBox;
	
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public PanelPreBusqueda(JFrame frame, MainWindow window) {
		
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
			
			JButton btnCancelar = new JButton("Cancelar");
			window.addManejadorCancelar(btnCancelar);
			panel_4.add(btnCancelar);
			
			window.setFldInterprete(this.txtFldInterprete);
			window.setFldTitulo(this.txtFldTitulo);
			window.setComboBox(this.comboBox);
			
	}

}
