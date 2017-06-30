package it.polimi.ingsw.ps42.view.GUI;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

public class CardContainer extends FunctionalLabel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5602274278988895636L;
	
	//Cards taken by the Player
	private List<CardLabel> greenCard;
	private List<CardLabel> yellowCard;
	private List<CardLabel> blueCard;
	private List<CardLabel> violetCard;
	private CardZoom cardZoom;
	private BufferedImage image;
	
	public CardContainer(Dimension dimension, Point location, CardZoom cardZoom, BufferedImage image, Dimension cardDimension ) {
		
		super();
		greenCard = new ArrayList<>();
		yellowCard = new ArrayList<>();
		blueCard = new ArrayList<>();
		violetCard = new ArrayList<>();
		this.image = image;		
		this.setSize(dimension);
		this.setLocation(location);
		this.cardZoom = cardZoom;
		
		JLayeredPane mainPane = new JLayeredPane();
		mainPane.setBounds(0, 0, (int)this.getWidth(),(int) this.getHeight());
		this.add(mainPane);
		
		JLabel backGround = new JLabel();
		backGround.setSize(dimension);
		backGround.setLocation(0,0);
		backGround.setIcon(resizeImage(image, dimension));
		mainPane.add(backGround, -1);
		
		buildCardPositions(mainPane, cardDimension);
		
	}
	
	private void buildCardPositions(JLayeredPane mainPane, Dimension cardDimension){
		//Build the positions for placing the cards
		
		placeCardPositions(yellowCard, mainPane, cardDimension, cardZoom, 0);
		placeCardPositions(greenCard, mainPane, cardDimension, cardZoom, (int)(this.getHeight()*0.2 + cardDimension.getHeight()));

	}
	
	private void placeCardPositions(List<CardLabel> deck, JLayeredPane mainPane, Dimension cardDimension, CardZoom cardZoom, int downShift){
		
		int deltaX = (int)(this.getWidth()*0.025);
		int deltaY = (int)(this.getHeight()*0.06 + downShift);
		for(int i=0; i<6; i++){
			CardLabel card = new CardLabel(deltaX, deltaY, cardDimension, cardZoom); 
			deck.add(card);
			mainPane.add(card, 0);
			deltaX += (int)(this.getWidth()*0.042) + cardDimension.getWidth();
		}
	}
	
	public void addGreenCard(BufferedImage cardTaken){
		CardLabel firstFree = searchFirstFree(greenCard);
		if(firstFree != null)
			firstFree.placeCard(cardTaken);
	}
	
	public void addYellowCard(BufferedImage cardTaken){
		CardLabel firstFree = searchFirstFree(yellowCard);
		if(firstFree != null)
			firstFree.placeCard(cardTaken);
	}
	
	public void addBlueCard(BufferedImage cardTaken){
		
	}	
	
	public void addVioletCard(BufferedImage cardTaken){
		
	}
	
	private CardLabel searchFirstFree(List<CardLabel> deck){
		for (CardLabel card : deck) {
			if(!card.hasACard())
				return card;
		}
		return null;
	}
}
