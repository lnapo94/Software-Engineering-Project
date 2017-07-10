package it.polimi.ingsw.ps42.view.GUI.dialog;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import it.polimi.ingsw.ps42.message.PlayerMove;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.view.GUI.GUIView;

/**
 * Class that implements a JDialog to show the move the player wants to do. Moreover, this window is used also to increase 
 * the action that the player is doing
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class IncrementWindow extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7959414300010255228L;
	
	private GUIView view;
	private JFrame mainFrame;
	private JSlider slider;
	private JButton confirmButton;
	private JButton deleteButton;
	
	private JLabel incrementLabel;
	
	private ActionType type;
	private FamiliarColor familiarColor;
	private int position;
	private int increment;
	
	/**
	 * Private class used to implement the increment operation with a slider
	 * @author Luca Napoletano, Claudio Montanari
	 *
	 */
	private class SliderListener implements ChangeListener{

		public void stateChanged(ChangeEvent event)
        {
           // update text field when the slider value changes
           JSlider source = (JSlider) event.getSource();
           increment = source.getValue();
           incrementLabel.setText(" Increment:" + increment);
        }
     };
	
     /**
      * Private class used to know when the user press a key on his keyboard
      * @author Luca Napoletano, Claudio Montanari
      *
      */
	private class KeyboardListener implements KeyListener {
		
		@Override
		public void keyPressed(KeyEvent e) {
			
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				trySendMove();
				close();
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE){ 
				cancelMove();
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				trySendMove();
				close();
			}		
		}
		
	}
	
	/**
	 * Private class to perform a button action. This class is used both for a confirm button and for a cancel button
	 * @author Luca Napoletano, Claudio Montanari
	 *
	 */
	private class ButtonAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(e.getSource() == confirmButton){
				trySendMove();
				close();
			}
			else if(e.getSource() == deleteButton){
				view.cancelMove(type, position);
				close();
			}
		}
		
	}
    
	/**
	 * Constructor of the IncrementWindow
	 * @param view					The current GUIView on the screen
	 * @param type					The type of the action chosen by the user
	 * @param familiarColor			The Color of the moved familiar
	 * @param position				The position where this familiar will be (e.g 2 floor of the green tower: ActionType = Take_Green, position = 1)
	 * @param actionValue			The value of the chosen action
	 * @param maxIncrement			The max increment to set correctly the slider (This is the number of slaves that the player has)
	 */
	public IncrementWindow(GUIView view, ActionType type, FamiliarColor familiarColor,int position,int actionValue , int maxIncrement) {
		super(view.getMainFrame());
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.view = view;
		this.mainFrame = view.getMainFrame();
		this.type = type;
		this.familiarColor = familiarColor;
		this.position = position;
		
		Dimension dimension = new Dimension((int)(mainFrame.getWidth()*0.3), (int)(mainFrame.getHeight()*0.4));
		this.setLayout(new GridLayout(8, 1));
		this.setSize(dimension);
		this.setLocation((int)(mainFrame.getWidth()*0.4),(int)(mainFrame.getHeight()*0.3));
		this.setVisible(true);
		this.setTitle("Your Move Resume");
		
		Font font = new Font("Papyrus", Font.ITALIC, (int)(dimension.getHeight()*0.08));
		//Set the Resume TextArea
		JLabel first = new JLabel(" Your current move:");
		first.setFont(font); 
		this.add(first);
		JLabel second = new JLabel(" Action type: "+ type.toString());
		second.setFont(font);
		this.add(second);
		JLabel third = new JLabel(" Action value: "+ actionValue);
		third.setFont(font);
		this.add(third);
		if(familiarColor != null){
			JLabel fourth = new JLabel(" Familiar color: "+ familiarColor.toString());
			fourth.setFont(font);
			this.add(fourth);
		}	
		JLabel fifth = new JLabel(" Position value: "+ position);
		fifth.setFont(font);
		this.add(fifth);
		
		incrementLabel= new JLabel(" Increment: "+ increment);
		incrementLabel.setFont(font);
		this.add(incrementLabel);
		
		
		//Set the increment component for the Action
		slider = new JSlider(0, maxIncrement, 0);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setFont(font);
		this.add(slider);
		
		JPanel buttonPane = new JPanel(new GridLayout(1, 2));
		buttonPane.setVisible(true);
		//Set the Confirm and Delete Buttons
		deleteButton = new JButton("Cancel");
		deleteButton.setFont(font);
		buttonPane.add(deleteButton);
		confirmButton = new JButton("Confirm");
		confirmButton.setFont(font);
		buttonPane.add(confirmButton);
		this.add(buttonPane);
		
		//Add all the listener
		slider.addChangeListener(new SliderListener());
		confirmButton.addActionListener(new ButtonAction());
		confirmButton.addKeyListener(new KeyboardListener());
		deleteButton.addActionListener(new ButtonAction());
		this.mainFrame.addKeyListener(new KeyboardListener());
	}
	
	/**
	 * Method used to cancel a chosen move and to do another one
	 */
	private void cancelMove(){
		this.view.cancelMove(type, position);
		this.dispose();
	}
	
	/**
	 * Method used to send a complete move to the Controller
	 */
	private void trySendMove(){
		PlayerMove move = new PlayerMove(view.getViewPlayerID(), type, familiarColor, position, increment);
		this.view.setNewMove(move);
	}
	
	/**
	 * Method used to close this window
	 */
	private void close(){
		dispose();
	}
}
