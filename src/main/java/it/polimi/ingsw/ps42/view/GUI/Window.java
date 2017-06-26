package it.polimi.ingsw.ps42.view.GUI;

import javax.swing.JFrame;

public class Window extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8264927819692748833L;
	
	public Window() {
		super();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void run() {
		this.setVisible(true);	
		this.setTitle("Lorenzo il Magnifico");
		this.setSize(500, 200);
	}

}
