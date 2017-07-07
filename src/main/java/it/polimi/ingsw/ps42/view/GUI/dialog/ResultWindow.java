package it.polimi.ingsw.ps42.view.GUI.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.polimi.ingsw.ps42.view.GUI.GUIView;

public class ResultWindow extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8184158263384479430L;
	private GUIView view;
	private JFrame parent;
	private List<String> finalChart;
	
	
	private class EndGameListener implements WindowListener{

		@Override
		public void windowActivated(WindowEvent e) {
			// Nothing to do
			
		}

		@Override
		public void windowClosed(WindowEvent e) {
			//Close all the windows of the game GUI
			closeAll();
			
		}

		@Override
		public void windowClosing(WindowEvent e) {
			// Nothing to do
			closeAll();
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// Nothing to do			
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// Nothing to do			
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// Nothing to do			
		}

		@Override
		public void windowOpened(WindowEvent e) {
			// Nothing to do			
		}
		
		
	}
	public ResultWindow(GUIView view, List<String> finalChart, Dimension dimension, Point location) {
		
		super(view.getMainFrame());
		this.view = view;
		this.parent = view.getMainFrame();
		this.finalChart = finalChart;
		//Set up the JDialog size and location 
		this.setSize(dimension);
		this.setLocation(location);
		this.setLayout(new GridLayout(2+finalChart.size(), 1));
		
		Font font = new Font("Papyrus", Font.ITALIC, (int)(dimension.getWidth()*0.025));
		Font bigFont = new Font("Papyrus", Font.ITALIC, (int)(dimension.getWidth()*0.03));

		//Add the Result Label and Pane
		JPanel resultPanel = new JPanel();
		resultPanel.setLayout(new GridLayout(1, 3));
		this.add(resultPanel);
		
		
		JLabel resultLabel = new JLabel();
		resultLabel.setFont(bigFont);
		if(finalChart.get(0).equals(view.getPlayer().getPlayerID()))
			resultLabel.setText("YOU WIN!!!");
		else
			resultLabel.setText("YOU LOST!!!");
		resultPanel.add(Box.createHorizontalStrut((int)(this.getWidth() * 0.3)));
		resultPanel.add(resultLabel);
		resultPanel.add(Box.createHorizontalStrut((int)(this.getWidth() * 0.5)));
		
		JLabel description = new JLabel(" Final chart:");
		description.setFont(font);
		this.add(description);
		
		//Add the final chart label
		int i=1;
		for (String name : finalChart) {
			JLabel playerLabel = new JLabel();
			playerLabel.setFont(font);
			playerLabel.setText("  "+i+" position:  "+name);
			this.add(playerLabel);
			i++;
		}
		
		//Add the listener
		this.addWindowListener(new EndGameListener());
		this.setVisible(true);
	}
	
	private void closeAll(){
		
		this.dispose();
		parent.dispose();
	}
}
