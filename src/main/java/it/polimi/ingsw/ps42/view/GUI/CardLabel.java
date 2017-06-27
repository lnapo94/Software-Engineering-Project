package it.polimi.ingsw.ps42.view.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class CardLabel extends JLabel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2232406506047461027L;
	
	private BufferedImage cardImage;
	private JLabel zoomLabel;
	
	public CardLabel(int x, int y, Dimension dimension, JLabel zoomLabel) {
		super();
		this.zoomLabel = zoomLabel;
		this.setBounds(x, y, (int)dimension.getWidth(), (int)dimension.getHeight());
		this.addMouseListener(mouseAdapter);
	}
	
	public void addZoomLabel(JLabel zoomLabel){
		this.zoomLabel = zoomLabel;
	}
	
	public void placeCard(BufferedImage cardImage){
		this.cardImage = cardImage;
		this.setIcon(resizeImage(cardImage, new Dimension(this.getWidth(), this.getHeight())));
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
	
	public BufferedImage getCardImage() {
		return cardImage;
	}
	
	private MouseAdapter mouseAdapter = new MouseAdapter() {
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if(cardImage != null)
				zoomImage(cardImage);
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			if(cardImage != null)
				zoomImage(cardImage);
		}
		@Override
		public void mouseExited(MouseEvent e) {
			zoomLabel.setIcon(null);
		}
	};
	
	private void zoomImage(BufferedImage imageToZoom){
		
		zoomLabel.setIcon(resizeImage(imageToZoom, new Dimension((int)zoomLabel.getWidth(), (int)zoomLabel.getHeight())));
	}
}
