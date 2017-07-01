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

public class CouncilRequestDialog extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6440420607016105830L;
	
	private GUIView view;
	private JFrame parent;
	private List<Obtain> possibilities;
	private Integer quantity;
	
	private List<JRadioButton> radioButtonsContainer;
	private ButtonGroup group;
	
	private JButton confirm;
	
	private CouncilRequest message;
	
	private transient Logger logger = Logger.getLogger(CouncilRequestDialog.class);
	
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
		this.setLayout(new GridLayout(1, possibilities.size() + 2));
		
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
		buttonPanel.add(confirm);
		
		this.add(buttonPanel);
		
		this.getContentPane().addKeyListener(new KeyboardListener());
		buttonPanel.addKeyListener(new KeyboardListener());
		
		confirm.addActionListener(new ConfirmAction());
	}
	
	private CouncilRequestDialog(GUIView view, CouncilRequest message, List<Obtain> possibilities, Integer quantity) {
		this(view, message);
		this.possibilities = possibilities;
		this.quantity = quantity;
	}
	
	public void run() {
		this.setVisible(true);
		this.setSize((int) (parent.getWidth() * 0.5), (int) (parent.getHeight() * 0.5));
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
	}
	
	private int controlRequest() {
		for(JRadioButton button : radioButtonsContainer) {
			if(button.isSelected())
				return radioButtonsContainer.indexOf(button);
		}
		return -1;
	}
	
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
	
	private class ConfirmAction implements ActionListener {

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
	
	private class KeyboardListener implements KeyListener {

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
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
