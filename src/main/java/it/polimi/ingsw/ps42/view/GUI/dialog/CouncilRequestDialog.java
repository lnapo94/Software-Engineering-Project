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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.message.CouncilRequest;
import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.exception.WrongChoiceException;
import it.polimi.ingsw.ps42.view.GUI.GUIView;

/**
 * Class for council privilege conversion decision
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class CouncilRequestDialog extends JDialog{

	
	private static final long serialVersionUID = 6440420607016105830L;
	
	private transient GUIView view;
	private JFrame parent;
	private List<Obtain> possibilities;
	private Integer quantity;
	
	private List<JRadioButton> radioButtonsContainer;
	private ButtonGroup group;
	
	private JButton confirm;
	
	private CouncilRequest message;
	
	private transient Logger logger = Logger.getLogger(CouncilRequestDialog.class);
	
	/**
	 * Constructor of the window starting from the GUIView and the council request
	 * 
	 * @param view the GUIview where the window will be displayed
	 * @param message the council privilege related message
	 */
	public CouncilRequestDialog(GUIView view, CouncilRequest message) {
		super(view.getMainFrame());
		this.view = view;
		this.parent = view.getMainFrame();
		
		this.message = message;
		this.possibilities = message.getPossibleChoice();
		this.quantity = message.getQuantity();
		
		//Common font for all the component
		Dimension dimension = new Dimension((int)(this.parent.getWidth()*0.3),(int)(this.parent.getHeight()*0.2));
		Font font = new Font("Papyrus", Font.ITALIC, (int)(dimension.getHeight()*0.15));
		
		//Initialize the visual component
		radioButtonsContainer = new ArrayList<>();
		group = new ButtonGroup();
		
		//Set the general layout
		this.setLayout(new GridLayout(possibilities.size() + 2, 1));
		
		JLabel label = new JLabel("Select one of this possibilities");
		label.setFont(font);
		this.add(label);
		
		for(Obtain council : possibilities) {
			JRadioButton button = new JRadioButton(council.print());
			button.setFont(font);
			group.add(button);
			radioButtonsContainer.add(button);
			button.setSize(dimension);
			
			button.addKeyListener(new KeyboardListener());
			
			this.add(button);
		}
		
		JPanel buttonPanel = new JPanel(new GridLayout(2, 0));
		buttonPanel.add(Box.createHorizontalStrut(this.getWidth()/2));
		
		confirm = new JButton("Confirm");
		confirm.setFont(font);
		buttonPanel.add(confirm);
		
		this.add(buttonPanel);
		
		this.getContentPane().addKeyListener(new KeyboardListener());
		buttonPanel.addKeyListener(new KeyboardListener());
		
		confirm.addActionListener(new ConfirmAction());
	}
	
	/**
	 * Method to rebuild the same window when other choice has to be made
	 * @param view the GUIView where the window will be displayed
	 * @param message the council privilege related message
	 * @param possibilities the list of remaining possibility to chose from
	 * @param quantity the quantity of choice to be made
	 */
	private CouncilRequestDialog(GUIView view, CouncilRequest message, List<Obtain> possibilities, Integer quantity) {
		this(view, message);
		this.possibilities = possibilities;
		this.quantity = quantity;
	}
	
	/**
	 * Method to enable the window to be seen and used
	 */
	public void run() {
		this.setVisible(true);
		this.setSize((int) (parent.getWidth() * 0.3), (int) (parent.getHeight() * 0.7));
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
	
	/**
	 * Private method to check the response and give back the corresponding index of the list of possibilities 
	 * 
	 * @return the index of the choice made of the list of possibilities 
	 */
	private int controlRequest() {
		for(JRadioButton button : radioButtonsContainer) {
			if(button.isSelected())
				return radioButtonsContainer.indexOf(button);
		}
		return -1;
	}
	
	/**
	 * Private method to send to the view the choice made by the player
	 * @param choice the index of the choice made
	 * @throws WrongChoiceException if the index exceeds the possible choice size
	 */
	private void sendRequest(Integer choice) throws WrongChoiceException {
		
		this.message.addChoice(choice);
		
		if(quantity > 1) {
			CouncilRequestDialog dialog = new CouncilRequestDialog(this.view, this.message, this.possibilities, this.quantity - 1);
			dialog.run();
			this.dispose();
		} else {
			view.setCouncilRequestResponse(message);
			this.dispose();
		}
	}
	
	/**
	 * Private class for confirm button pressed actions
	 * 
	 * @author Luca Napoletano, Claudio Montanari
	 *
	 */
	private class ConfirmAction implements ActionListener {

		/**
		 * If the button is pressed then send the response to the view
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			int choice = controlRequest();
			if(choice >= 0) {
				try {
					sendRequest(choice);
				} catch (WrongChoiceException e1) {
					logger.error("Wrong choice, error in council request dialog");
					logger.info(e1);
				}
			}
		}
		
	}
	
	/**
	 * Private class for keybord events handling
	 * 
	 * @author Luca Napoletano, Claudio Montanari
	 *
	 */
	private class KeyboardListener implements KeyListener {

		/**
		 * If enter is pressed then send the response to the view
		 */
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				int choice = controlRequest();
				if(choice >= 0) {
					try {
						sendRequest(choice);
					} catch (WrongChoiceException e1) {
						logger.error("Wrong choice, error in council request dialog");
						logger.info(e1);
					}
				}
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
