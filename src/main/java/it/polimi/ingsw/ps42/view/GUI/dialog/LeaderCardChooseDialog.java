package it.polimi.ingsw.ps42.view.GUI.dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import it.polimi.ingsw.ps42.model.leaderCard.LeaderCard;
import it.polimi.ingsw.ps42.parser.ImageLoader;
import it.polimi.ingsw.ps42.view.GUI.GUIView;

public class LeaderCardChooseDialog extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6964310255996863156L;
	
	private GUIView view;
	private JFrame parent;
	private List<JLabel> cardContainer;
	private ImageLoader loader;
	
	private List<LeaderCard> list;
	
	public LeaderCardChooseDialog(GUIView view, List<LeaderCard> list) throws IOException {
		super(view.getMainFrame());
		this.view = view;
		this.parent = view.getMainFrame();
		this.list = list;
		
		loader = new ImageLoader("Resource//Configuration//imagePaths.json");
		
		int cards = list.size();
		
		//Initialize list of JLabel
		cardContainer = new ArrayList<>();
		
		for(int i = 0; i < cards; i++)
			cardContainer.add(new JLabel());
		
		this.setLayout(new GridLayout(1, cards));
		
		int i = 0;
		for(JLabel label : cardContainer) {
			
			this.add(label);
			BufferedImage image = loader.loadLeaderCardImage(list.get(i).getName());
			
			label.setSize((int) (parent.getHeight() * 0.30), (int) (parent.getWidth() * 0.25));
			label.setToolTipText(list.get(i).getName());
			
			label.addMouseListener(new MyMouseListener(label, this));
			
			label.setIcon(resizeImage(image,new Dimension(label.getWidth(), label.getHeight())));
			label.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
			i++;
		}
		this.pack();
		this.setResizable(false);
		this.setVisible(true);
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

		private JLabel label;
		private JDialog parent;
		
		public MyMouseListener(JLabel label, JDialog parent) {
			this.label = label;
			this.parent = parent;
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			int index = cardContainer.indexOf(label);
			view.setLeaderCardChoice(list, index);
			parent.dispose();
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}
		
	}

}
