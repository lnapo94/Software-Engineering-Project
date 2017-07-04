package it.polimi.ingsw.ps42.view.GUI;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

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
	
	public PositionContainer(Dimension dimension, Point location, Dimension labelPreferredSize) {
		super();
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
	
	public void placeFamiliar(BufferedImage familiarImage){
		if(familiarPlaced.size() == 8)
			resizeLayout();
		buildFamiliarLabel(familiarImage);
			
	}
	
	private void buildFamiliarLabel(BufferedImage familiarImage){
		JLabel familiarLabel = new JLabel();
		familiarLabel.setSize(dimensionToUse);
		familiarLabel.setIcon(resizeImage(familiarImage, familiarLabel.getSize()));
		familiarPlaced.add(familiarLabel);
		familiarPlacedImages.add(familiarImage);
		this.add(familiarLabel);
	}
	
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
	
	public void resetLastFamiliar(){
		JLabel toRemove = familiarPlaced.remove(familiarPlaced.size()-1);
		toRemove.setIcon(null);
		this.remove(toRemove);
		familiarPlacedImages.remove(familiarPlacedImages.size()-1);
		updateUI();
	}
	
	public int getLastIndex(){
		return (familiarPlaced.size()-1);
	}
	
	public boolean isEmpty(){
		return familiarPlaced.isEmpty();
	}
}
