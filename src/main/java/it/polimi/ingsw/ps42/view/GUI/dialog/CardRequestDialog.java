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

import it.polimi.ingsw.ps42.message.CancelCardRequest;
import it.polimi.ingsw.ps42.message.CardRequest;
import it.polimi.ingsw.ps42.message.PayRequest;
import it.polimi.ingsw.ps42.model.Printable;
import it.polimi.ingsw.ps42.view.GUI.GUIView;


/**
 * Class for Card Request handling it exposes to the player the possible choice that can be made
 * 
 * @author Luca Napoletano, Claudio Montanari
 */
public class CardRequestDialog extends JDialog{

	private static final long serialVersionUID = 1426132029705455118L;
	
	private GUIView view;
	private JFrame parent;
	
	private CardRequest message;
	private List<Printable> possibleChoice;
	
	private List<JRadioButton> radioButtonsContainer;
	private ButtonGroup group;
	
	private JButton confirm;
	private JButton cancel;
	
	/**
	 * Constructor for the window given the GUIview where it will be displayed and the CardRequest message
	 * @param view
	 * @param message
	 */
	public CardRequestDialog(GUIView view, CardRequest message) {
		super(view.getMainFrame());
		this.view = view;
		this.parent = view.getMainFrame();
		group = new ButtonGroup();
		radioButtonsContainer = new ArrayList<>();
		
		//Set the font
		Dimension dimension = new Dimension((int)(this.parent.getWidth()*0.3),(int)(this.parent.getHeight()*0.2));
		Font font = new Font("Papyrus", Font.ITALIC, (int)(dimension.getHeight()*0.15));

		this.message = message;
		
		possibleChoice = message.showChoice();
		
		//Set the layout
		this.setLayout(new GridLayout(possibleChoice.size() + 1, 1, 0, (int) (this.getHeight() * 0.2)));
		
		//Create the button for the choice
		for(Printable choice : possibleChoice) {
			
			JRadioButton button = new JRadioButton();
			button.setFont(font);
			button.setSize(dimension);
			radioButtonsContainer.add(button);
			group.add(button);
			
			button.setText(choice.print());
			
			button.addKeyListener(new KeyboardListener());
			
			this.add(button);
		}
		
		//Add a button to a pane and add all to the dialog
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 3));
		buttonPanel.add(Box.createHorizontalStrut((int)(this.getWidth() / 2)));
		
		confirm = new JButton("Confirm");
		confirm.setFont(font);
		buttonPanel.add(confirm);
		
		cancel = new JButton("Cancel");
		cancel.setFont(font);
		buttonPanel.add(cancel);
		
		this.add(buttonPanel);
		
		//Set the listener
		this.getContentPane().addKeyListener(new KeyboardListener());
		this.addKeyListener(new KeyboardListener());
		confirm.addActionListener(new ConfirmAction());
		cancel.addActionListener(new CancelAction());
		
		if(message instanceof PayRequest)
			cancel.setEnabled(false);
	}
	
	/**
	 * Method to start the window, then it will be visible and usable
	 */
	public void run() {
		this.setSize((int)(parent.getWidth() * 0.4), (int)(parent.getHeight() * 0.4));
		this.setVisible(true);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
	}
	
	/**
	 * Private method used to catch the response of the player
	 * 
	 * @return the index of the response from the list of possibilities
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
	 * Method to close the window 
	 */
	public void close() {
		this.dispose();
	}
	
	/**
	 * Private class for confirm button pressed actions 
	 * 
	 * @author Luca Napoletano, Claudio Montanari
	 *
	 */
	private class ConfirmAction implements ActionListener {

		/**
		 * If the button if pressed, then send the response to the view and close the window
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			int index = sendRequest();
			if(index != -1) {
				view.setCardRequestResponse(message, index);
				close();
			}
		}
		
	}
	
	/**
	 * Proivate class for delete button pressed actions
	 * 
	 * @author Luca Napoletano, Claudio Montanari
	 *
	 */
	private class CancelAction implements ActionListener {

		/**
		 * If the button if pressed then, send a cancelREquest message and close the window
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			view.sendCancelRequest(new CancelCardRequest(view.getViewPlayerID(), true));
			close();
		}
		
	}
	
	/**
	 * Private class for keyboard events handling
	 * 
	 * @author Luca Napoletano, Claudio Montanari
	 *
	 */
	private class KeyboardListener implements KeyListener {
		
		/**
		 * if enter is pressed then get the choice made and send it
		 */
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				int index = sendRequest();
				if(index != -1) {
					view.setCardRequestResponse(message, index);
					close();
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			
		}

		/**
		 * Enable numbers for selecting choice from the window
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
