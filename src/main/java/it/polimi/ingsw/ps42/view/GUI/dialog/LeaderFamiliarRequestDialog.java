package it.polimi.ingsw.ps42.view.GUI.dialog;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import it.polimi.ingsw.ps42.message.leaderRequest.LeaderFamiliarRequest;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.view.GUI.GUIView;

public class LeaderFamiliarRequestDialog extends JDialog{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3853801646684305891L;
	
	private GUIView view;
	private JFrame parent;
	
	private JRadioButton orange;
	private JRadioButton black;
	private JRadioButton white;
	private JRadioButton neutral;
	
	private JButton confirm;
	
	private ButtonGroup group;
	
	private LeaderFamiliarRequest message;
	
	public LeaderFamiliarRequestDialog(GUIView view, LeaderFamiliarRequest message) {
		super(view.getMainFrame());
		this.view = view;
		this.parent = view.getMainFrame();
		this.message = message;
		
		//Set the font
		Dimension dimension = new Dimension((int)(this.parent.getWidth()*0.3),(int)(this.parent.getHeight()*0.2));
		Font font = new Font("Papyrus", Font.ITALIC, (int)(dimension.getHeight()*0.15));
		
		this.setLayout(new GridLayout(6, 1));
		
		//Initialize the group
		this.group = new ButtonGroup();
		
		//Initialize the buttons
		orange = new JRadioButton("Orange");
		orange.setFont(font);
		group.add(orange);
		
		white = new JRadioButton("White");
		white.setFont(font);
		group.add(white);
		
		black = new JRadioButton("Black");
		black.setFont(font);
		group.add(black);
		
		neutral = new JRadioButton("Neutral");
		neutral.setFont(font);
		group.add(neutral);
		
		JLabel text = new JLabel("Select the familiar you want to increase");
		text.setFont(font);
		
		confirm = new JButton("Confirm");
		confirm.addActionListener(new ConfirmAction());
		
		//Adding all the view objects to the layout
		this.add(text);
		this.add(orange);
		this.add(black);
		this.add(white);
		this.add(neutral);
		
		//Create a Panel for the button
		JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
		buttonPanel.add(Box.createHorizontalStrut((int)(this.getWidth()/2)));
		buttonPanel.add(confirm);
		
		this.add(buttonPanel);
	}
	
	public void run() {
		this.pack();
		this.setResizable(false);
		this.setVisible(true);
	}
	
	private void sendRequest() {
		if(orange.isSelected()) {
			view.setLeaderFamiliarRequestResponse(FamiliarColor.ORANGE, this.message);
			close();
		}
		if(black.isSelected()) {
			view.setLeaderFamiliarRequestResponse(FamiliarColor.BLACK, this.message);
			close();
		}
		if(white.isSelected()) {
			view.setLeaderFamiliarRequestResponse(FamiliarColor.WHITE, this.message);
			close();
		}
		if(neutral.isSelected()) {
			view.setLeaderFamiliarRequestResponse(FamiliarColor.NEUTRAL, this.message);
			close();
		}
	}
	
	private void close() {
		this.dispose();
	}
	
	private class ConfirmAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			sendRequest();
		}
		
	}

}
