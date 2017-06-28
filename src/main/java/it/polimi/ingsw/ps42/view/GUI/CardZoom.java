package it.polimi.ingsw.ps42.view.GUI;

import java.awt.Dimension;
import java.awt.image.BufferedImage;


public class CardZoom extends FunctionalLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6201276196334825923L;
	
	private final BufferedImage defaultImage;
	
	public CardZoom(BufferedImage defaultImage, Dimension dimension) {
		
		super();
		this.defaultImage = defaultImage;
		this.setSize(dimension);
		showDefaultImage();
	}
	
	public void showDefaultImage(){

		this.setIcon(resizeImage(defaultImage, this.getSize()));
	}
	
	public void showZoomedCard(BufferedImage cardImage){
		this.setIcon(resizeImage(cardImage, this.getSize()));
	}
	
}
