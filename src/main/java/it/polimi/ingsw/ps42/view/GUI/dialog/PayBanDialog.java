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

public class PayBanDialog extends JDialog{

	private JButton confirm;
	private JButton cancel;
	private View view;
	private int banPeriod;
	
	private class ClickListener implements ActionListener{

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
	
	public PayBanDialog(GUIView view, Dimension dimension, Point location, BufferedImage banImage, int banPeriod) {
		
		super(view.getMainFrame());
		this.view = view;
		this.banPeriod = banPeriod;
		//Set the size of the window
		this.setSize(dimension);
		this.setLocation(location);
		BorderLayout layout = new BorderLayout(); 
		this.setLayout(layout);
		Font font = new Font("Papyrus", Font.ITALIC, (int)(dimension.getHeight()*0.07));

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
		this.setVisible(true);
	}
	
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
	
	public void close(){
		this.dispose();
	}
}
