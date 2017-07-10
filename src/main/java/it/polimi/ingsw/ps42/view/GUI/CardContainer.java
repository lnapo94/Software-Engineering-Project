package it.polimi.ingsw.ps42.view.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.border.LineBorder;

/**
 * Class used to create a FunctionalLabel to contain a Card
 * @author Luca Napoletano, Claudio Montanari
 *
 */
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
	
	private JLayeredPane mainPane;
	private Dimension cardDimension;
	
	/**
	 * Private class used to handle a mouse event
	 * @author Luca Napoletano, Claudio Montanari
	 *
	 */
	private class MouseClickListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			//If the source has an Image then swap the entire List of Cards
			if(violetCard.contains(e.getSource()) )
				swap(violetCard);
			else if(blueCard.contains(e.getSource()))
				swap(blueCard);
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// Nothing to do
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// Nothing to do
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// Nothing to do
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// Nothing to do
			
		}
		
	}
	
	/**
	 * Constructor for the card container
	 * @param dimension				The dimension of the FunctionalLabel
	 * @param location				The location on the screen
	 * @param cardZoom				The CardZoom component on the screen to show the card zoomed
	 * @param image					The image of the card
	 * @param tableCardDimension	The dimension of the card on the table
	 */
	public CardContainer(Dimension dimension, Point location, CardZoom cardZoom, BufferedImage image, Dimension tableCardDimension ) {
		
		super();
		greenCard = new ArrayList<>();
		yellowCard = new ArrayList<>();
		blueCard = new ArrayList<>();
		violetCard = new ArrayList<>();
		this.setSize(dimension);
		this.setLocation(location);
		this.cardZoom = cardZoom;
		
		mainPane = new JLayeredPane();
		mainPane.setBounds(0, 0, (int)this.getWidth(),(int) this.getHeight());
		this.add(mainPane);
		
		JLabel backGround = new JLabel();
		int shift = (int) (dimension.getWidth() * 0.03);
		backGround.setSize(new Dimension((int)(dimension.getWidth()*0.8), (int)dimension.getHeight()));
		backGround.setLocation(shift,0);
		backGround.setIcon(resizeImage(image, backGround.getSize()));
		mainPane.add(backGround, -1);
		this.cardDimension = new Dimension((int)(tableCardDimension.getWidth()*1.1), (int)(tableCardDimension.getHeight()*1.1));
		buildCardPositions(mainPane, cardDimension);
		
	}
	
	/**
	 * Method used to add the card to the player's cards picture
	 * @param image		The image of the card to add
	 */
	public void addBonusBarLabel(BufferedImage image){
		JLabel bonusBar = new JLabel();
		bonusBar.setSize((int)(this.getSize().getWidth()*0.042), (int)(this.getSize().getHeight() ));
		bonusBar.setLocation(0,0);
		bonusBar.setIcon(resizeImage(image, bonusBar.getSize()));
		this.add(bonusBar, 0);
		this.updateUI();
	}
	
	/**
	 * Build the positions for placing the cards
	 * @param mainPane			The LayeredPane where the card goes
	 * @param cardDimension		The dimension of the card
	 */
	private void buildCardPositions(JLayeredPane mainPane, Dimension cardDimension){
		placeCardPositions(yellowCard, mainPane, cardDimension, cardZoom, 0);
		placeCardPositions(greenCard, mainPane, cardDimension, cardZoom, (int)(this.getHeight()*0.2 + cardDimension.getHeight()));
	}
	
	/**
	 * Method used to add a deck of CardLabel to the correct positions on the screen
	 * @param deck				The list of CardLabel to add
	 * @param mainPane			The main JLayeredPane of the app
	 * @param cardDimension		The dimension of a single card
	 * @param cardZoom			The CardZoom component
	 * @param downShift			The vertical gap between two cards
	 */
	private void placeCardPositions(List<CardLabel> deck, JLayeredPane mainPane, Dimension cardDimension, CardZoom cardZoom, int downShift) {
		
		int deltaX = (int)(this.getWidth()*0.02 + (int) (this.getSize().getWidth() * 0.03));
		int deltaY = (int)(this.getHeight()*0.02 + downShift);
		for(int i=0; i<6; i++){
			CardLabel card = new CardLabel(deltaX, deltaY, cardDimension, cardZoom); 
			deck.add(card);
			mainPane.add(card, 0);
			if(i == 2)
				deltaX = (int)(deltaX * 1.03);
			deltaX += (int)(this.getWidth()*0.022) + cardDimension.getWidth();
		}
	}

	/**
	 * Method used to create and add the first card, after that you can call the placeCardPosition method to place the other cards
	 * @param deck			The deck of CardLabel to position
	 * @param downShift		The vertical gap between two cards
	 * @return
	 */
	private CardLabel newFirstCard(List<CardLabel> deck, int downShift){
				
		int deltaX = (int)(this.getWidth()*0.8 + this.getSize().getWidth() * 0.03 + deck.size() * (cardDimension.getWidth()*0.04) );
		int deltaY = (int)(this.getHeight()*0.02 + downShift + deck.size() * (cardDimension.getHeight()*0.02) );
		
		CardLabel card = new CardLabel(deltaX, deltaY, cardDimension, cardZoom);
		deck.add(card);
		card.setBorder(new LineBorder(Color.BLACK,(int) (cardDimension.getWidth()*0.01)));
		card.addMouseListener(new MouseClickListener() );
		mainPane.add(card, 0);
		return card;
	}
	
	/**
	 * Method used to place a green card on the screen
	 * @param cardTaken		The picture to add to the screen
	 */
	public void addGreenCard(BufferedImage cardTaken){
		CardLabel firstFree = searchFirstFree(greenCard);
		if(firstFree != null)
			firstFree.placeCard(cardTaken);
	}

	/**
	 * Method used to place a yellow card on the screen
	 * @param cardTaken		The picture to add to the screen
	 */
	public void addYellowCard(BufferedImage cardTaken){
		CardLabel firstFree = searchFirstFree(yellowCard);
		if(firstFree != null)
			firstFree.placeCard(cardTaken);
	}

	/**
	 * Method used to place a blue card on the screen
	 * @param cardTaken		The picture to add to the screen
	 */
	public void addBlueCard(BufferedImage cardTaken){
		CardLabel firstFree = newFirstCard(blueCard, (int)(this.getHeight()*0.12 + cardDimension.getHeight()));
		if(firstFree != null){
			firstFree.placeCard(cardTaken);
			firstFree.setBorder(new LineBorder(Color.BLACK,(int) (firstFree.getWidth()*0.02)));
		}
	}	

	/**
	 * Method used to place a violet card on the screen
	 * @param cardTaken		The picture to add to the screen
	 */
	public void addVioletCard(BufferedImage cardTaken){
		CardLabel firstFree = newFirstCard(violetCard, 0);
		if(firstFree != null){
			firstFree.placeCard(cardTaken);
			firstFree.setBorder(new LineBorder(Color.BLACK,(int) (firstFree.getWidth()*0.02)));
		}
	}
	
	/**
	 * Method used to search the first free position of the deck of card labels
	 * @param deck
	 * @return
	 */
	private CardLabel searchFirstFree(List<CardLabel> deck){
		for (CardLabel card : deck) {
			if(!card.hasACard())
				return card;
		}
		return null;
	}
	
	/**
	 * Method used to swap the picture of the deck on the screen
	 * @param deck	The deck to swap	
	 */
	private void swap(List<CardLabel> deck){
		
		BufferedImage imageToSwap = deck.get(0).getCardImage();
		BufferedImage imageToSave;
		CardLabel temp;
		for(int i=0; i<deck.size()-1; i++){
			temp = deck.get(i+1);
			if(temp.getCardImage() != null){
				imageToSave = temp.getCardImage();
				temp.placeCard(imageToSwap);
				imageToSwap = imageToSave;
			}
			
		}
		deck.get(0).placeCard(imageToSwap);
		cardZoom.showZoomedCard(deck.get(deck.size()-1).getCardImage());
	}
}
