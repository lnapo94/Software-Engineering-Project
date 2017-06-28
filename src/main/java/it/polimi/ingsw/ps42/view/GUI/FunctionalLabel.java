package it.polimi.ingsw.ps42.view.GUI;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public abstract class FunctionalLabel extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8294248962646491734L;

	public FunctionalLabel() {
		super();
	}
	
	protected ImageIcon resizeImage(BufferedImage imageToResize, Dimension newDimension){
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
}
