package it.polimi.ingsw.ps42.view.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import it.polimi.ingsw.ps42.message.PlayerMove;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;

public class IncrementWindow extends JDialog{

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
	
	private class SliderListener implements ChangeListener{

		public void stateChanged(ChangeEvent event)
        {
           // update text field when the slider value changes
           JSlider source = (JSlider) event.getSource();
           increment = source.getValue();
           incrementLabel.setText(" Increment:" + increment);
        }
     };
	
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
			}		}
		
	}
	
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
    
	public IncrementWindow(GUIView view, ActionType type, FamiliarColor familiarColor,int position,int actionValue , int maxIncrement) {
		super(view.getMainFrame());
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.view = view;
		this.mainFrame = view.getMainFrame();
		this.type = type;
		this.familiarColor = familiarColor;
		this.position = position;
		
		Dimension dimension = new Dimension((int)(mainFrame.getWidth()*0.3), (int)(mainFrame.getHeight()*0.4));
		this.setLayout(new GridLayout(7, 1));
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
		JLabel fourth = new JLabel(" Familiar color: "+ familiarColor.toString());
		fourth.setFont(font);
		this.add(fourth);
		incrementLabel= new JLabel(" Increment: "+ increment);
		incrementLabel.setFont(font);
		this.add(incrementLabel);
		
		
		//Set the increment component for the Action
		slider = new JSlider(0, maxIncrement, (int)(maxIncrement/2));
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setFont(font);
		this.add(slider);
		
		//Set the Confirm and Delete Buttons
		deleteButton = new JButton("Cancel");
		confirmButton.setFont(font);
		this.add(deleteButton);
		confirmButton = new JButton("Confirm");
		confirmButton.setFont(font);
		this.add(confirmButton);
		
		//Add all the listener
		slider.addChangeListener(new SliderListener());
		confirmButton.addActionListener(new ButtonAction());
		confirmButton.addKeyListener(new KeyboardListener());
		deleteButton.addActionListener(new ButtonAction());
		this.getContentPane().addKeyListener(new KeyboardListener());
	}
	
	private void cancelMove(){
		this.view.cancelMove(type, position);
		this.dispose();
	}
	
	private void trySendMove(){
		PlayerMove move = new PlayerMove(view.getViewPlayerID(), type, familiarColor, position, increment);
		this.view.setNewMove(move);
	}
	
	private void close(){
		dispose();
	}
}
