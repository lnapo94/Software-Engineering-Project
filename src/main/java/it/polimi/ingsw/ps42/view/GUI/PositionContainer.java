package it.polimi.ingsw.ps42.view.GUI;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

/**
 * Class used to create a dynamic position container to add more familiar. It is used, for example, in the council position to set a new layout
 * when the familiars are too much
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class PositionContainer extends FunctionalLabel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8026387550266457720L;
	private List<JLabel> familiarPlaced;
	private List<BufferedImage> familiarPlacedImages;
	private Dimension labelPreferredSize;
	private Dimension reducedDimension;
	private Dimension dimensionToUse;
	private boolean activated;
	
	/**
	 * Constructor for the position container
	 * @param dimension
	 * @param location
	 * @param labelPreferredSize
	 */
	public PositionContainer(Dimension dimension, Point location, Dimension labelPreferredSize) {
		super();
		this.activated = true;
		this.labelPreferredSize = labelPreferredSize;
		this.setSize(dimension);
		this.setLocation(location);
		familiarPlaced = new ArrayList<>();
		familiarPlacedImages = new ArrayList<>();
		this.setVisible(true);
		this.setLayout(new GridLayout(2, 4));
		this.reducedDimension = new Dimension((int)(labelPreferredSize.getWidth()*0.7), (int)(labelPreferredSize.getHeight()*0.7));
		this.dimensionToUse = labelPreferredSize;
	}
	
	/**
	 * Method used to place a familiar here
	 * @param familiarImage		The picture of the familiar to add to this container
	 */
	public void placeFamiliar(BufferedImage familiarImage){
		if(familiarPlaced.size() == 8)
			resizeLayout();
		buildFamiliarLabel(familiarImage);
			
	}
	
	/**
	 * Method used to build and add a familiar 
	 * @param familiarImage	The image for the familiar label to add to the container
	 */
	private void buildFamiliarLabel(BufferedImage familiarImage){
		JLabel familiarLabel = new JLabel();
		familiarLabel.setSize(dimensionToUse);
		familiarLabel.setIcon(resizeImage(familiarImage, familiarLabel.getSize()));
		familiarPlaced.add(familiarLabel);
		familiarPlacedImages.add(familiarImage);
		this.add(familiarLabel);
	}
	
	/**
	 * Method used to adjust the dimension of the component
	 */
	private void resizeLayout(){
		for (JLabel familiar : familiarPlaced) {
			this.remove(familiar);
		}
		this.setLayout(new GridLayout(3, 4));
		dimensionToUse = reducedDimension;
		for (int i=0; i<familiarPlaced.size(); i++) {
			JLabel familiar = familiarPlaced.get(i);
			familiar.setSize(dimensionToUse);
			familiar.setIcon(resizeImage(familiarPlacedImages.get(i), dimensionToUse));
			this.add(familiar);
		}
	}
	
	/**
	 * Method used to reset all the familiars in this container
	 */
	public void resetPosition(){
		for (JLabel label : familiarPlaced) {
			label.setIcon(null);
			this.remove(label);
		}
		familiarPlaced = new ArrayList<>();
		familiarPlacedImages = new ArrayList<>();
		this.setLayout(new GridLayout(2, 4));
		dimensionToUse = labelPreferredSize;
		updateUI();
	}
	
	/**
	 * Method used to remove the last familiar added here
	 */
	public void resetLastFamiliar(){
		JLabel toRemove = familiarPlaced.remove(familiarPlaced.size()-1);
		toRemove.setIcon(null);
		this.remove(toRemove);
		familiarPlacedImages.remove(familiarPlacedImages.size()-1);
		updateUI();
	}
	
	/**
	 * Method used to know the last used index in this container of positions
	 * @return The last used index
	 */
	public int getLastIndex(){
		//if (familiarPlaced.size() != 0 )
		return (familiarPlaced.size()-1);
	}
	
	/**
	 * Method used to know if the position container is empty
	 * @return	True if there isn't any familiar placed here, otherwise False
	 */
	public boolean isEmpty(){
		return familiarPlaced.isEmpty(); 
	}
	
	/**
	 * Method used to disable this component, in this way the player cannot position here his familiars
	 */
	public void disableContainer(){
		this.activated = false;
	}
	
	/**
	 * Method used to know if this component is activated
	 * @return	True if the player can position his familiars in this component, otherwise False
	 */
	public boolean isActivated(){
		return activated;
	}
}
