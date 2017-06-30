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

public class BonusBarRequestDialog extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3444158853208641010L;
	
	private GUIView view;
	private JFrame parent;
	
	private ButtonGroup group;
	private List<JRadioButton> radioButtonsContainer;
	
	private List<BonusBar> list;
	
	private JButton confirm;
	
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
		confirm.addActionListener(new ConfirmAction());
	}
	
	public void run() {
		this.setSize((int)(parent.getWidth() * 0.4), (int)(parent.getHeight() * 0.4));
		this.setVisible(true);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
	}
	
	private int sendRequest() {
		for(JRadioButton radio : radioButtonsContainer) {
			if(radio.isSelected()) {
				return radioButtonsContainer.indexOf(radio);
			}
		}
		return -1;
	}
	
	public void close() {
		this.dispose();
	}
	
	private class ConfirmAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int index = sendRequest();
			if(index != -1) {
				view.setBonusBarChoice(list, index);
				close();
			}
		}
		
	}
	
	private class KeyboardListener implements KeyListener {
		
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
