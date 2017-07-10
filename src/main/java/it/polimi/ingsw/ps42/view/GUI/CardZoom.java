package it.polimi.ingsw.ps42.view.GUI;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

/**
 * Component on the screen used to show a zoomed card to visualize it well
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class CardZoom extends FunctionalLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6201276196334825923L;
	
	private transient final BufferedImage defaultImage;
	
	/**
	 * THe constructor of the CardZoom component
	 * @param defaultImage			The default image to show
	 * @param dimension				The dimension of this component
	 */
	public CardZoom(BufferedImage defaultImage, Dimension dimension) {
		
		super();
		this.defaultImage = defaultImage;
		this.setSize(dimension);
		showDefaultImage();
	}
	
	/**
	 * Method used to show the default picture
	 */
	public void showDefaultImage(){

		this.setIcon(resizeImage(defaultImage, this.getSize()));
	}
	
	/**
	 * Method used to show the specify card picture
	 * @param cardImage		The picture to show on the CardZoom component
	 */
	public void showZoomedCard(BufferedImage cardImage){
		this.setIcon(resizeImage(cardImage, this.getSize()));
	}
	
}
