package it.polimi.ingsw.ps42.view.GUI.dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.polimi.ingsw.ps42.model.leaderCard.LeaderCard;
import it.polimi.ingsw.ps42.parser.ImageLoader;
import it.polimi.ingsw.ps42.view.GUI.GUIView;

public class LeaderCardShowDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9117390219349271997L;
	
	private GUIView view;
	private JFrame parent;
	
	private List<JLabel> cardsContainer;
	private ImageLoader loader;
	
	private List<LeaderCard> playersLeaderCards;
	private List<LeaderCard> activatedLeaderCardsList;
	
	private JButton discard;
	private JButton enable;
	private JButton cancel;
	
	public LeaderCardShowDialog(GUIView view) throws IOException {
		super(view.getMainFrame());
		this.view = view;
		this.parent = view.getMainFrame();
		
		loader = new ImageLoader("Resource//Configuration//imagePaths.json");
		
		playersLeaderCards = view.getPlayer().getLeaderCardList();
		activatedLeaderCardsList = view.getPlayer().getActivatedLeaderCard();
		
		int cardsToShow = playersLeaderCards.size() + activatedLeaderCardsList.size();

		this.setLayout(new GridLayout(2, 1));
		
		JPanel cardPanel = new JPanel(new GridLayout(1, cardsToShow));
		
		createLabel(activatedLeaderCardsList, cardPanel);
		createLabel(playersLeaderCards, cardPanel);
		
		this.add(cardPanel);
		
		enable = new JButton("Confirm");
		cancel = new JButton("Cancel");
		discard = new JButton("Discard");
		
		JPanel buttonPanel = new JPanel(new GridLayout(3, 1));
		
		buttonPanel.add(enable);
		buttonPanel.add(cancel);
		buttonPanel.add(discard);
		
		this.add(buttonPanel);
	}
	
	public void run() {
		this.pack();
		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	
	private void createLabel(List<LeaderCard> list, JPanel cardPanel) throws IOException {
		for(int i = 0; i < list.size(); i++) {
			JLabel label = new JLabel();
			cardsContainer.add(label);
			cardPanel.add(label);
			BufferedImage image = loader.loadLeaderCardImage(list.get(i).getName());
			
			label.setSize((int) (parent.getHeight() * 0.30), (int) (parent.getWidth() * 0.25));
			
			label.addMouseListener(new MyMouseListener());
			
			label.setIcon(resizeImage(image,new Dimension(label.getWidth(), label.getHeight())));
			label.setToolTipText(list.get(i).getName());
			
			label.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
		}
	}
	
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
	
	private class MyMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
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
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
