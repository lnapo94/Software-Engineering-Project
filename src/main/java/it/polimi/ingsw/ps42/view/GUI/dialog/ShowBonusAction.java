package it.polimi.ingsw.ps42.view.GUI.dialog;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.polimi.ingsw.ps42.model.action.ActionPrototype;
import it.polimi.ingsw.ps42.view.GUI.GUIView;

/**
 * Class for showing the action prototype of a bonus action 
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class ShowBonusAction extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7495493885551395022L;
	
	/**
	 * Build a bonus action window given the action prototype to show and the GUIView where it will be displayed
	 * 
	 * @param view the GUIView where the window will be displayed 
	 * @param bonusAction the action prototype to show
	 */
	public ShowBonusAction(GUIView view, ActionPrototype bonusAction) {
		super(view.getMainFrame());
		
		this.setLayout(new GridLayout(5, 1));
		
		//Set the font
		Dimension dimension = new Dimension((int)(view.getMainFrame().getWidth()*0.3),(int)(view.getMainFrame().getHeight()*0.2));
		Font font = new Font("Papyrus", Font.ITALIC, (int)(dimension.getHeight()*0.15));
				
		
		JLabel initialText = new JLabel("You have a bonus action:");
		initialText.setFont(font);
		this.add(initialText);
		
		JLabel actionType = new JLabel("Type: " + bonusAction.getType().toString());
		actionType.setFont(font);
		this.add(actionType);
		
		JLabel actionLevel = new JLabel("Level of this action: " + bonusAction.getLevel());
		actionLevel.setFont(font);
		this.add(actionLevel);
		
		JLabel actionDiscount = new JLabel("Discount: " + bonusAction.getDiscount().print());
		actionDiscount.setFont(font);
		this.add(actionDiscount);
		
		//Adding the continue button
		JButton continueButton = new JButton("Continue");
		continueButton.addActionListener(new ContinueAction());
		continueButton.addKeyListener(new KeyboardListener());
		continueButton.setFont(font);
		
		JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
		buttonPanel.add(Box.createHorizontalStrut((int)this.getWidth()/2));
		buttonPanel.add(continueButton);
		
		this.add(buttonPanel);
		
		
	}
	
	/**
	 * Method to enable the window to be seen 
	 */
	public void run() {
		this.pack();
		this.setVisible(true);		
	}
	
	/**
	 * Method that close the current window
	 */
	private void close() {
		this.dispose();
	}
	
	/**
	 * Private class for the continue button event handling
	 * 
	 * @author Luca Napoletano, Claudio Montanari
	 *
	 */
	private class ContinueAction implements ActionListener {

		/**
		 * When clicked close the window
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			close();
		}
		
	}
	
	/**
	 * Private class for keyboard event handling 
	 * 
	 * @author Luca Napoletano, Claudio Monatanari
	 *
	 */
	private class KeyboardListener implements KeyListener {

		/**
		 * If enter is pressed then close the window
		 */
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				close();
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// Nothings to do
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// Nothings to do
			
		}
		
	}

}
