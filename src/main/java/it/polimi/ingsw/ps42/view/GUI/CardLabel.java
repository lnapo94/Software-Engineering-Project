package it.polimi.ingsw.ps42.view.GUI;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * Class that represents a FunctionalLabel which contains a Card
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class CardLabel extends FunctionalLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2232406506047461027L;
	
	private transient BufferedImage cardImage;
	private CardZoom zoomLabel;
	
	/**
	 * Private class used to handle a mouse event
	 * @author Luca Napoletano, Claudio Montanari
	 *
	 */
	private transient MouseAdapter mouseAdapter = new MouseAdapter() {
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if(cardImage != null)
				zoomLabel.showZoomedCard(cardImage);		
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
	
	/**
	 * The constructor of a card label
	 * @param x				The horizontal value used to set a bound
	 * @param y				The vertical value used to set a bound
	 * @param dimension		The dimension of the FunctionalLabel
	 * @param zoomLabel		The CardZoom component
	 */
	public CardLabel(int x, int y, Dimension dimension, CardZoom zoomLabel) {
		super();
		this.cardImage = null;
		this.zoomLabel = zoomLabel;
		this.setBounds(x, y, (int)dimension.getWidth(), (int)dimension.getHeight());
		this.addMouseListener(mouseAdapter);
	}
	
	/**
	 * Method used to add to a card a CardZoom component
	 * @param zoomLabel	The CardZoom component to add
	 */
	public void addZoomLabel(CardZoom zoomLabel){
		this.zoomLabel = zoomLabel;
	}
	
	/**
	 * Method used to add the picture to show
	 * @param cardImage		The picture to show in the FunctionalLable
	 */
	public void placeCard(BufferedImage cardImage){
		this.cardImage = cardImage;
		this.setIcon(resizeImage(cardImage, new Dimension(this.getWidth(), this.getHeight())));
	}
	
	/**
	 * Method used to set the picture of the FunctionalLable to null
	 */
	public void removeCard(){
		this.cardImage = null;
		this.setIcon(null);
	}
	
	/**
	 * Method used to get the picture of the card
	 * @return	The picture of the card
	 */
	public BufferedImage getCardImage() {
		return cardImage;
	}
	
	/**
	 * Method used to know if the CardLabel has a card
	 * @return	True if the CardLabel has a card, otherwise False
	 */
	public boolean hasACard(){
		return cardImage != null;
	}

}
