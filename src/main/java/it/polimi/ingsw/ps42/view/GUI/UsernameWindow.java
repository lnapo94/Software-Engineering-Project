package it.polimi.ingsw.ps42.view.GUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UsernameWindow extends JDialog{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2825307417634274850L;
	
	private JLabel description;
	private JTextField textField;
	private JButton confirmButton;
	private JButton cancelButton;
	private GUIView view;
	private JFrame mainFrame;

	public UsernameWindow(GUIView view, JFrame mainFrame, String warning) {
		super(mainFrame);
		this.mainFrame = mainFrame;
		this.view = view;
		Dimension dimension = new Dimension((int)(mainFrame.getWidth()*0.3),(int)(mainFrame.getHeight()*0.2));
		this.setLayout(new GridLayout(3,1));
		this.setSize(dimension);
		this.setLocation((int)(mainFrame.getWidth()*0.5), (int)(mainFrame.getHeight()*0.5));
		this.setVisible(true);
		this.setTitle("Login");
		Font font = new Font("Papyrus", Font.ITALIC, (int)(dimension.getHeight()*0.15));
		//Set the description label
		description = new JLabel();
		description.setFont(font);
		description.setText("Enter your name:");
		this.add(description);
		//Set the input component and the Buttons
		textField = new JTextField();
		//textField.setSize((int)(dimension.getWidth()*0.3),(int)(dimension.getHeight()*0.2));
		textField.setEditable(true);
		textField.setFont(font);
		this.add(textField);
		
		JPanel buttonsPanel = new JPanel(new GridLayout(1, 3));
		
		buttonsPanel.add(Box.createHorizontalStrut((int)(dimension.getWidth())));
		confirmButton = new JButton("Confirm");
		confirmButton.addActionListener(new ConfirmListener(this));
		this.textField.addKeyListener(new ConfirmListener(this));
		this.textField.addMouseListener(new ConfirmListener(this));
		//button.setSize((int)(dimension.getWidth()*0.3),(int)(dimension.getHeight()*0.2));
		buttonsPanel.add(confirmButton);
		
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new CancelListener(this));
		buttonsPanel.add(cancelButton);
		
		this.add(buttonsPanel);
		
		this.setResizable(false);
	}
	
	private class ConfirmListener implements ActionListener, KeyListener, MouseListener {
		
		private UsernameWindow father;
		
		public ConfirmListener(UsernameWindow father) {
			this.father = father;
			textField.setText("Insert here your user id...");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			controlLogin();
		}
		
		private void controlLogin() {
			if(!textField.getText().equals("") && !textField.getText().equals("Insert here your user id...")) {
				//TODO view.setNewPlayerID(textField.getText());
				textField.setText("");
				father.dispose();
			}
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER)
				controlLogin();
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if(textField.getText().equals("Insert here your user id..."))
				textField.setText("");
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			textField.setText("");
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class CancelListener implements ActionListener {
		private UsernameWindow father;
		private JFrame view;
		
		public CancelListener(UsernameWindow father) {
			this.father = father;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			father.dispose();
			mainFrame.dispose();
		}		
	}
}
