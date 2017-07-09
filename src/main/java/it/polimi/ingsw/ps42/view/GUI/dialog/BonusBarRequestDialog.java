package it.polimi.ingsw.ps42.view.GUI.dialog;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import it.polimi.ingsw.ps42.model.player.BonusBar;
import it.polimi.ingsw.ps42.view.GUI.GUIView;


/**
 * Class for asking the player which BonusBar want to play with
 * 
 * @author Luca Napoletano, Claudio Montanari
 */
public class BonusBarRequestDialog extends JDialog{

	private static final long serialVersionUID = 3444158853208641010L;
	
	private GUIView view;
	private JFrame parent;
	
	private ButtonGroup group;
	private List<JRadioButton> radioButtonsContainer;
	
	private List<BonusBar> list;
	
	private JButton confirm;
	
	
	/**
	 * Builder of the window
	 * @param view the GUIview where the window will by displayed
	 * @param list the list of BonusBar to choose from
	 */
	public BonusBarRequestDialog(GUIView view, List<BonusBar> list) {
		super(view.getMainFrame());
		this.view = view;
		this.parent = view.getMainFrame();
		
		group = new ButtonGroup();
		radioButtonsContainer = new ArrayList<>();
		
		//Set the font
		Dimension dimension = new Dimension((int)(this.parent.getWidth()*0.3),(int)(this.parent.getHeight()*0.2));
		Font font = new Font("Papyrus", Font.ITALIC, (int)(dimension.getHeight()*0.15));
		
		this.list = list;
		
		//Set the layout
		this.setLayout(new GridLayout(this.list.size() + 1, 1, 0, (int) (this.getHeight() * 0.2)));
		
		for(BonusBar bar : list) {
			JRadioButton button = new JRadioButton();
			button.setFont(font);
			button.setSize((int) (this.getWidth() * 0.1), (int) (this.getHeight() * 0.1));
			radioButtonsContainer.add(button);
			group.add(button);
			
			button.setText(bar.toString());
			
			button.addKeyListener(new KeyboardListener());
			
			
			this.add(button);
		}
		
		//Add a button to a pane and add all to the dialog
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 2));
		buttonPanel.add(Box.createHorizontalStrut((int)(this.getWidth() / 2)));
		confirm = new JButton("Confirm");
		confirm.setFont(font);
		buttonPanel.add(confirm);
		
		this.add(buttonPanel);
		
		//Set the listener
		this.getContentPane().addKeyListener(new KeyboardListener());
		this.addKeyListener(new KeyboardListener());
		confirm.addActionListener(new ConfirmAction());
	}
	
	/**
	 * Method to enable the window, then the window will be visible and usable
	 */
	public void run() {
		this.setSize((int)(parent.getWidth() * 0.4), (int)(parent.getHeight() * 0.4));
		this.setVisible(true);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
	}
	
	/**
	 * Private method used to send a response to the View
	 * @return the index of the choice made in the list of BonusBar
	 */
	private int sendRequest() {
		for(JRadioButton radio : radioButtonsContainer) {
			if(radio.isSelected()) {
				return radioButtonsContainer.indexOf(radio);
			}
		}
		return -1;
	}
	
	/**
	 * Public method to delete the window
	 */
	public void close() {
		this.dispose();
	}
	
	/**
	 * Private Class for button pressed action
	 * 
	 * @author Luca Napoletano, Claudio Montanari
	 *
	 */
	private class ConfirmAction implements ActionListener {

		/**
		 * Send the choice selected to the view
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			int index = sendRequest();
			if(index != -1) {
				view.setBonusBarChoice(list, index);
				close();
			}
		}
		
	}
	
	/**
	 * Private class for keyboard events
	 * 
	 * @author Luca Napoletano, Claudio Montanari
	 *
	 */
	private class KeyboardListener implements KeyListener {
		
		/**
		 * If enter if pressed then send the respose to the view
		 */
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				int index = sendRequest();
				if(index != -1) {
					view.setBonusBarChoice(list, index);
					close();
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			
		}

		/**
		 * If number pressed the send the corresponding value as a response
		 */
		@Override
		public void keyTyped(KeyEvent e) {
			int keyEvent = e.getKeyCode();
			
			if(KeyEvent.VK_1 < keyEvent && keyEvent < KeyEvent.VK_9) {
				if(radioButtonsContainer.size() > keyEvent)
					radioButtonsContainer.get(keyEvent).setSelected(true);
			}
		}
		
	}

}
