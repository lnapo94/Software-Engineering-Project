package it.polimi.ingsw.ps42.view.GUI;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

public class CardContainer extends JLabel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5602274278988895636L;
	
	//Cards taken by the Player
	private List<CardZoom> greenCard;
	private List<CardZoom> yellowCard;
	private List<CardZoom> blueCard;
	private List<CardZoom> violetCard;
	private CardZoom cardZoom;
	
	public CardContainer(Dimension dimension, Point location, CardZoom cardZoom) {
		
		super();
		greenCard = new ArrayList<>();
		yellowCard = new ArrayList<>();
		blueCard = new ArrayList<>();
		violetCard = new ArrayList<>();
		this.setSize(dimension);
		this.setLocation(location);
		this.cardZoom = cardZoom;
		buildCardPositions();
	}
	
	private void buildCardPositions(){
		//Build the positions for placing the cards
		
	}
	
	
}
