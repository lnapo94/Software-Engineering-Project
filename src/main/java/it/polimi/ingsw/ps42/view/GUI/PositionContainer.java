package it.polimi.ingsw.ps42.view.GUI;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

public class PositionContainer extends FunctionalLabel{

	private List<JLabel> familiarPlaced;
	private Dimension labelPreferredSize;
	
	public PositionContainer(Dimension dimension, Point location, Dimension labelPreferredSize) {
		super();
		this.setSize(dimension);
		this.setLocation(location);
		familiarPlaced = new ArrayList<>();
		this.setLayout(new GridLayout(2, 4));
	}
	
	public void placeFamiliar(BufferedImage familiarImage){
		if(familiarPlaced.size() == 8)
			resizeLayout();
		JLabel familiarLabel = new JLabel();
		familiarLabel.setSize(labelPreferredSize);
		familiarPlaced.add(familiarLabel);
		this.add(familiarLabel);
			
	}
	
	private void resizeLayout(){
		
	}
	
	public void resetPosition(){
		for (JLabel label : familiarPlaced) {
			label.setIcon(null);
		}
	}
	
	public void resetLastFamiliar(){
		familiarPlaced.get(familiarPlaced.size()-1).setIcon(null);
	}
}
