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

public class ShowBonusAction extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7495493885551395022L;
	
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
		
		JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
		buttonPanel.add(Box.createHorizontalStrut((int)this.getWidth()/2));
		buttonPanel.add(continueButton);
		
		this.add(buttonPanel);
		
		
	}
	
	public void run() {
		this.pack();
		this.setVisible(true);		
	}
	
	private void close() {
		this.dispose();
	}
	
	private class ContinueAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			close();
		}
		
	}
	
	private class KeyboardListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				close();
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
