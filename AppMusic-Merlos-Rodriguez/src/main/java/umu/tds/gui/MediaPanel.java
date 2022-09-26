package umu.tds.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MediaPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public MediaPanel(JFrame frame, MainWindow window) {
		
		JPanel MediaButtons = new JPanel();
		this.add(MediaButtons);
		MediaButtons.setLayout(new BorderLayout(0, 0));
		
		JButton btnNewButton = new JButton("▶");
		MediaButtons.add(btnNewButton, BorderLayout.NORTH);
		window.addManejadorPlay(btnNewButton);
		
		JPanel Southbutons = new JPanel();
		MediaButtons.add(Southbutons, BorderLayout.SOUTH);
		Southbutons.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnNewButton_2 = new JButton("<<");
		Southbutons.add(btnNewButton_2);
		window.addManejadorRetrocederCancion(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("▌▌");
		Southbutons.add(btnNewButton_3);
		window.addManejadorPause(btnNewButton_3);
		
		
		JButton btnNewButton_5 = new JButton(">>");
		Southbutons.add(btnNewButton_5);
		window.addManejadorAvanzarCancion(btnNewButton_5);
	}

}
