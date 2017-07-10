package it.polimi.ingsw.ps42.view.GUI.dialog;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.model.leaderCard.LeaderCard;
import it.polimi.ingsw.ps42.parser.ImageLoader;
import it.polimi.ingsw.ps42.view.GUI.GUIView;

/**
 * Class for leader Cards interacting during the game
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class LeaderCardShowDialog extends JDialog {

	private static final long serialVersionUID = 9117390219349271997L;
	
	private transient GUIView view;
	private JFrame parent;
	
	//Player's leader cards list
	private List<LeaderCard> leaderCardList;
	private List<LeaderCard> activatedList;
	private List<LeaderCard> totalCardList;
	
	private List<JLabel> cardLabels;
	private List<JLabel> activatedCardLabel;
	private List<JLabel> totalLabels;
	
	private JButton enable;
	private JButton cancel;
	private JButton discard;
	
	private transient ImageLoader loader;
	
	private int index = -1;
	
	private transient Logger logger = Logger.getLogger(LeaderCardShowDialog.class);
	
	/**
	 * Constructor for the window starting from the corresponding GUIView
	 * 
	 * @param view the GUIView where the window will be displayed
	 * @throws IOException if any problem in leaderCard opening occurs
	 */
	public LeaderCardShowDialog(GUIView view) throws IOException {
		super(view.getMainFrame());
		//Set the variables
		this.view = view;
		this.parent = view.getMainFrame();
		
		//Set the player's lists
		this.leaderCardList = view.getPlayer().getLeaderCardList();
		this.activatedList = view.getPlayer().getActivatedLeaderCard();
		
		//TotalCards list
		this.totalCardList = new ArrayList<>();
		this.totalCardList.addAll(activatedList);
		this.totalCardList.addAll(leaderCardList);
		
		//Create the cardLabels list
		cardLabels = new ArrayList<>();
		activatedCardLabel = new ArrayList<>();
		
		//Total label list
		totalLabels = new ArrayList<>();
		
		//Create the loader
		loader = new ImageLoader("Resource//Configuration//imagePaths.json");
		
		//Set the main layout
		this.setLayout(new GridLayout(1, leaderCardList.size() + activatedList.size() + 1));
		
		//Show the activated leader cards before
		loadImage(totalCardList, this.getContentPane());
		
		//Add the buttons to the layout
		JPanel buttonPanel = new JPanel(new GridLayout(3, 1));
		
		//Set the font
		Dimension dimension = new Dimension((int)(this.parent.getWidth()*0.3),(int)(this.parent.getHeight()*0.2));
		Font font = new Font("Papyrus", Font.ITALIC, (int)(dimension.getHeight()*0.15));
		
		enable = new JButton("Enable");
		enable.setFont(font);
		
		discard = new JButton("Discard");
		discard.setFont(font);
		
		cancel = new JButton("Cancel");
		cancel.setFont(font);
		
		enable.addActionListener(new EnableAction());
		discard.addActionListener(new DiscardAction());
		cancel.addActionListener(new CancelAction());
		
		buttonPanel.add(enable);
		buttonPanel.add(discard);
		buttonPanel.add(cancel);
		
		this.add(buttonPanel);
	}
	
	/**
	 * Method for enabling the window to be visible and usable
	 */
	public void run() {
		this.pack();
		this.setVisible(true);
		this.setResizable(false);
	}
	
	/**
	 * Private method for loading the leader Cards of the player
	 * 
	 * @param list the list of LeaderCards of the player
	 * @param container the Container for the leader Cards
	 * @throws IOException if any problem in leader Cards image opening occurs
	 */
	private void loadImage(List<LeaderCard> list, Container container) throws IOException {
		for(int i = 0; i < totalCardList.size(); i++) {
			JLabel label = new JLabel();
		
			totalLabels.add(label);
			BufferedImage image = loader.loadLeaderCardImage(list.get(i).getName());
			
			label.setSize((int) (parent.getHeight() * 0.30), (int) (parent.getWidth() * 0.25));
			label.setToolTipText(list.get(i).getName());
			
			label.setIcon(resizeImage(image,new Dimension(label.getWidth(), label.getHeight())));
			label.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
			
			if(activatedList.contains(list.get(i))) {
				label.setBorder(BorderFactory.createLineBorder(Color.GREEN, 5));
				activatedCardLabel.add(label);
			} else {
				cardLabels.add(label);
			}
			
			label.addMouseListener(new CardsMouseListener(label));
			container.add(label);
			label.setVisible(true);
		}
	}
	
	/**
	 * Private method for resizing an image given a preferred new dimension
	 * 
	 * @param imageToResize the Buffered Image to resize
	 * @param newDimension the preferred new dimension of the image
	 * @return the Image Icon of the resized image
	 */
	private ImageIcon resizeImage(BufferedImage imageToResize, Dimension newDimension){
		Image cardResized = null;
		if(imageToResize != null){
			int width = (int)newDimension.getWidth();
			int height = (int)newDimension.getHeight();
			int scaledWidth= width;
			int scaledHeight = (int)(height*scaledWidth/(width));
			cardResized = imageToResize.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
		}
		
		return new ImageIcon(cardResized);
	}
	
	/**
	 * Private method to delete the window
	 */
	private void close() {
		this.dispose();
	}
	
	/**
	 * Private class for enabling a Leader Card
	 * 
	 * @author Luca Napoletano, Claudio Montanari
	 *
	 */
	private class EnableAction implements ActionListener {

		/**
		 * Send a leader card enabling message to the view
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(index != -1 && !activatedList.contains(totalCardList.get(index))) {
				view.sendLeaderCardUpdate(totalCardList.get(index));
				close();
			}
		}
		
	}
	
	/**
	 * Private class for discarding a Leader Card
	 * 
	 * @author Luca Napoletano, Claudio Montanari
	 *
	 */
	private class DiscardAction implements ActionListener {

		/**
		 * Send a leader card discard message to the view
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(index != -1 && !activatedList.contains(totalCardList.get(index))) {
				view.sendDiscardRequest(totalCardList.get(index));
				close();
			}
		}
		
	}
	
	/**
	 * Private class for closing the window
	 * 
	 * @author Luca Napoletano, Claudio Motanari
	 *
	 */
	private class CancelAction implements ActionListener {

		/**
		 * Close the window
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) {
			close();			
		}
		
	}
	
	/**
	 * Private class for mouse event handling
	 * 
	 * @author Luca Napoletano, Claudio Montanari
	 *
	 */
	private class CardsMouseListener implements MouseListener {
		
		private JLabel label;
		
		/**
		 * Constructor of the listener that associate the listener to a label
		 * 
		 * @param label the reference label for the listener
		 */
		public CardsMouseListener(JLabel label) {
			this.label = label;
		}
		
		/**
		 * When click on it change the border of the JLabel
		 */
		@Override
		public void mouseClicked(MouseEvent arg0) {
			label.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 10));
			
			for(JLabel otherLabel : totalLabels) {
				if(otherLabel != label) {
					if(activatedCardLabel.contains(otherLabel)) {
						otherLabel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 5));
					} else {
						otherLabel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
					}
				}
			}
			
			index = totalLabels.indexOf(label);
			logger.info("Index in leader card show dialog: " + index);
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// Nothings to do
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// Nothings to do
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// Nothings to do
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// Nothings to do
			
		}
		
	}
	
}