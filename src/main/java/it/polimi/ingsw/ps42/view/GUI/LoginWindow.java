package it.polimi.ingsw.ps42.view.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Class used to request a username to the player at the start of the match
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class LoginWindow extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1833081829244855714L;

	private JLabel description;		
	private JTextField userInput;
	private JButton confirm;
	private JButton cancel;
	private String warning;
	
	private GUIView view;
	private JFrame parent;
	
	public LoginWindow(GUIView view, String warning) {
		super(view.getMainFrame());
		this.parent = view.getMainFrame();
		this.view = view;
		this.warning = warning;
		
		Dimension dimension = new Dimension((int)(this.parent.getWidth()*0.3),(int)(this.parent.getHeight()*0.2));
		Font font = new Font("Papyrus", Font.ITALIC, (int)(dimension.getHeight()*0.15));
		
		//Initialization
		description = new JLabel();
		userInput = new JTextField();
		confirm = new JButton();
		cancel = new JButton();
		
		if(!warning.isEmpty()) {
			this.userInput.setText(warning);
			this.userInput.setFont(font);
			this.userInput.setForeground(Color.RED);
		} else {
			this.userInput.setText("Insert here your ID");
			this.userInput.setFont(font);
		}
		
		//Set the dialog
		this.setLayout(new GridLayout(3,1));
		this.setSize(dimension);
		this.setLocation((int)(this.parent.getWidth()*0.5), (int)(this.parent.getHeight()*0.5));
		this.setVisible(true);
		this.setTitle("Login");
		this.setResizable(false);
		
		this.description.setFont(font);
		this.description.setText("Enter your Username");
		this.add(description);
		
		this.add(userInput);
		this.userInput.setEditable(true);
		
		this.confirm.setText("Confirm");
		this.confirm.setFont(font);
		
		this.cancel.setText("Cancel");
		this.cancel.setFont(font);
		
		//New Panel for the buttons
		JPanel buttonsPanel = new JPanel(new GridLayout(1, 3));
		buttonsPanel.add(Box.createHorizontalStrut((int)(dimension.getWidth())));
		
		buttonsPanel.add(confirm);
		buttonsPanel.add(cancel);
		
		this.add(buttonsPanel);
		
		//Adding listener
		confirm.addActionListener(new ButtonAction());
		cancel.addActionListener(new ButtonAction());
		
		userInput.addKeyListener(new KeyboardListener());
		this.getContentPane().addKeyListener(new KeyboardListener());
		
		userInput.addMouseListener(new MouseEvent());
	}
	
	public void run() {
		this.setVisible(true);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
	
	private void controlInput() {
		String input = userInput.getText();
		boolean control = !input.equals("Insert here your ID") && !input.equals("") && !input.equals(warning);
		
		if(control) {
			view.setNewPlayerID(input);
			this.dispose();
		}
	}
	
	private void closeAll() {
		this.dispose();
		parent.dispose();
	}
	
	private class KeyboardListener implements KeyListener {
		
		@Override
		public void keyPressed(KeyEvent e) {
			
			if(e.getKeyCode() == KeyEvent.VK_ENTER)
				controlInput();
			
			if(e.getKeyCode() == KeyEvent.ALT_DOWN_MASK)
				if(e.getKeyCode() == KeyEvent.VK_F4)
					closeAll();
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE) 
				closeAll();
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			//Nothing to do
		}
		
	}
	
	private class ButtonAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(e.getSource() == confirm)
				controlInput();
			
			if(e.getSource() == cancel)
				closeAll();
			
		}
		
	}
	
	private class MouseEvent implements MouseListener {

		@Override
		public void mouseClicked(java.awt.event.MouseEvent e) {
			if(e.getSource() == userInput) {
				if(!warning.isEmpty() || userInput.getText().equals("Insert here your ID")) {
					userInput.setText("");
					userInput.setForeground(Color.BLACK);
					warning = "";
				}
			}
		}

		@Override
		public void mouseEntered(java.awt.event.MouseEvent e) {

		}

		@Override
		public void mouseExited(java.awt.event.MouseEvent e) {

		}

		@Override
		public void mousePressed(java.awt.event.MouseEvent e) {

		}

		@Override
		public void mouseReleased(java.awt.event.MouseEvent e) {

		}
		
	}
	
}
