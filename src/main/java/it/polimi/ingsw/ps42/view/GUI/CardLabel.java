package it.polimi.ingsw.ps42.view.GUI;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;


public class CardLabel extends FunctionalLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2232406506047461027L;
	
	private BufferedImage cardImage;
	private CardZoom zoomLabel;
	
	private MouseAdapter mouseAdapter = new MouseAdapter() {
		
		@Override
		public void mouseClicked(MouseEvent e) {
			//Nothing to do
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			if(cardImage != null)
				zoomLabel.showZoomedCard(cardImage);
		}
		@Override
		public void mouseExited(MouseEvent e) {
			zoomLabel.showDefaultImage();
		}
	};
	
	
	public CardLabel(int x, int y, Dimension dimension, CardZoom zoomLabel) {
		super();
		this.cardImage = null;
		this.zoomLabel = zoomLabel;
		this.setBounds(x, y, (int)dimension.getWidth(), (int)dimension.getHeight());
		this.addMouseListener(mouseAdapter);
	}
	
	public void addZoomLabel(CardZoom zoomLabel){
		this.zoomLabel = zoomLabel;
	}
	
	public void placeCard(BufferedImage cardImage){
		this.cardImage = cardImage;
		this.setIcon(resizeImage(cardImage, new Dimension(this.getWidth(), this.getHeight())));
	}
	
	public void removeCard(){
		this.cardImage = null;
		this.setIcon(null);
	}
	
	public BufferedImage getCardImage() {
		return cardImage;
	}

	public boolean hasACard(){
		return cardImage != null;
	}

}
