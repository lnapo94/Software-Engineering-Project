package it.polimi.ingsw.ps42.view.GUI.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.polimi.ingsw.ps42.view.View;
import it.polimi.ingsw.ps42.view.GUI.GUIView;

/**
 * Class for choosing if pay the ban at the end of the period
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class PayBanDialog extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 278687938788809639L;
	
	private JButton confirm;
	private JButton cancel;
	private transient View view;
	private int banPeriod;
	
	/**
	 * Private class for button events handling
	 * 
	 * @author Luca Napoletano, Claudio Montanari
	 *
	 */
	private class ClickListener implements ActionListener{

		/**
		 * If button clicked then send a confirm or delete message given the source of the event
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			//If a button is clicked check if is a cancel or confirm button and then send the response to the View
			boolean response = false;
			if(e.getSource() == confirm){
				response = true;
			}
			else if(e.getSource() == cancel){
				response = false;
			}
			//Delete the Window and send the message
			view.setPayBanResponse(response, banPeriod);
			close();
		}
		
	}
	
	/**
	 * Constructor of the window given the GUIView where it will be displayed and the information about the ban to pay
	 * 
	 * @param view the GUIView where the window will be displayed
	 * @param dimension the dimension of the window
	 * @param location the location of the window
	 * @param banImage the ban image to show to the player
	 * @param banPeriod the period of the ban 
	 */
	public PayBanDialog(GUIView view, Dimension dimension, Point location, BufferedImage banImage, int banPeriod) {
		
		super(view.getMainFrame());
		this.view = view;
		this.banPeriod = banPeriod;
		//Set the size of the window
		this.setSize(dimension);
		this.setLocation(location);
		BorderLayout layout = new BorderLayout(); 
		this.setLayout(layout);
		Font font = new Font("Papyrus", Font.ITALIC, (int)(dimension.getWidth()*0.025));

		//Add the description JLabel
		JLabel description = new JLabel();
		description.setFont(font);
		description.setText("The end of this period has come, now You have to show your loyalty to the Church");
		this.add(description, BorderLayout.NORTH);
		
		//Add empty component
		this.add( Box.createHorizontalStrut((int)(this.getWidth() * 0.38)), BorderLayout.WEST);
		this.add(Box.createHorizontalStrut((int)(this.getWidth() * 0.39)), BorderLayout.EAST);

		//Add the Ban Image
		JLabel banLabel = new JLabel();
		banLabel.setSize((int)(dimension.getWidth() * 0.22), (int)(dimension.getHeight() * 0.65));
		banLabel.setIcon(resizeImage(banImage, banLabel.getSize()));
		this.add(banLabel, BorderLayout.CENTER);
		
		//Add the Confirm and Delete JButton
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new GridLayout(1, 2));
		this.add(buttonPane, BorderLayout.SOUTH);
		cancel = new JButton("Take the Ban");
		cancel.setFont(font);
		buttonPane.add(cancel);
		confirm = new JButton("Be Loyal");
		confirm.setFont(font);
		buttonPane.add(confirm);
		//Add the action listener
		cancel.addActionListener(new ClickListener());
		confirm.addActionListener(new ClickListener());
		
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
	
	/**
	 * Method used to show this window
	 */
	public void run() {
		this.setVisible(true);
	}
	
	/**
	 * Private method to resize an image
	 * @param imageToResize the BufferedImage to resize
	 * @param newDimension the preferred new dimension
	 * @return the ImageIcon of the image passed properly resized
	 */
	private ImageIcon resizeImage(BufferedImage imageToResize, Dimension newDimension){
		Image cardResized = null;
		if(imageToResize != null){
			int width = (int)newDimension.getWidth();
			int height = (int)newDimension.getHeight();
			int scaledHeight = height;
			int scaledWidth= (int)(width*scaledHeight/(height));
			cardResized = imageToResize.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
		}
		
		return new ImageIcon(cardResized);
	}
	
	/**
	 * Method that close the current window
	 */
	public void close(){
		this.dispose();
	}
}
