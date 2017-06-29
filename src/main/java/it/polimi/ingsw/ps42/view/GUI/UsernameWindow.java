package it.polimi.ingsw.ps42.view.GUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class UsernameWindow extends JDialog{

	
	public UsernameWindow(JFrame mainFrame, String title) {
		super(mainFrame);
		Dimension dimension = new Dimension((int)(mainFrame.getWidth()*0.3),(int)(mainFrame.getHeight()*0.2));
		this.setLayout(new GridLayout(2,2));
		this.setSize(dimension);
		this.setLocation((int)(mainFrame.getWidth()*0.5), (int)(mainFrame.getHeight()*0.5));
		this.setVisible(true);
		this.setTitle(title);
		Font font = new Font("Papyrus", Font.ITALIC, (int)(dimension.getHeight()*0.1));
		//Set the description label
		JLabel description = new JLabel();
		description.setFont(font);
		description.setText("Enter your name:");
		this.add(description);
		//Set the input component and the Buttons
		JTextField textField = new JTextField();
		//textField.setSize((int)(dimension.getWidth()*0.3),(int)(dimension.getHeight()*0.2));
		textField.setEditable(true);
		textField.setFont(font);
		this.add(textField);
		this.add(Box.createHorizontalStrut((int)(dimension.getWidth())));
		JButton button = new JButton("Confirm");
		//button.setSize((int)(dimension.getWidth()*0.3),(int)(dimension.getHeight()*0.2));
		this.add(button);
		
		this.setResizable(false);
	}
}
