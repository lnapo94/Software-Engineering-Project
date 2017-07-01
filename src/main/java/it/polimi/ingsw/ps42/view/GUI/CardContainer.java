package it.polimi.ingsw.ps42.view.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.border.LineBorder;

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
	private class MouseClickListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			//If the source has an Image then swap the entire List of Cards
			if(e.getSource() == violetCard)
				swap(violetCard);
			else if(e.getSource() == blueCard)
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
		backGround.setSize(new Dimension((int)(dimension.getWidth()*0.8), (int)dimension.getHeight()));
		backGround.setLocation(0,0);
		backGround.setIcon(resizeImage(image, backGround.getSize()));
		mainPane.add(backGround, -1);
		
		buildCardPositions(mainPane, new Dimension((int)(cardDimension.getWidth()*1.1), (int)(cardDimension.getHeight()*1.1)));
		
	}
	
	private void buildCardPositions(JLayeredPane mainPane, Dimension cardDimension){
		//Build the positions for placing the cards
		
		
		try {
			placeCardPositions(yellowCard, mainPane, cardDimension, cardZoom, 0);
			placeCardPositions(greenCard, mainPane, cardDimension, cardZoom, (int)(this.getHeight()*0.2 + cardDimension.getHeight()));
			placeOverlappedCards(violetCard, mainPane, cardDimension, cardZoom, 0);
			placeOverlappedCards(blueCard, mainPane, cardDimension, cardZoom, (int)(this.getHeight()*0.12 + cardDimension.getHeight()));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void placeCardPositions(List<CardLabel> deck, JLayeredPane mainPane, Dimension cardDimension, CardZoom cardZoom, int downShift) throws IOException{
		
		int deltaX = (int)(this.getWidth()*0.02);
		int deltaY = (int)(this.getHeight()*0.02 + downShift);
		for(int i=0; i<6; i++){
			CardLabel card = new CardLabel(deltaX, deltaY, cardDimension, cardZoom); 
			deck.add(card);
			//card.setIcon(resizeImage(ImageIO.read(CardContainer.class.getResource("/Images/Cards/FirstPeriod/Blue/1.png")), cardDimension));
			mainPane.add(card, 0);
			if(i == 2)
				deltaX = (int)(deltaX * 1.03);
			deltaX += (int)(this.getWidth()*0.022) + cardDimension.getWidth();
		}
	}
	
	private void placeOverlappedCards(List<CardLabel> deck, JLayeredPane mainPane, Dimension cardDimension, CardZoom cardZoom, int downShift) throws IOException{
		
		int deltaX = (int)(this.getWidth()*0.8);
		int deltaY = (int)(this.getHeight()*0.02 + downShift);
		for(int i=0; i<6; i++){
			CardLabel card = new CardLabel(deltaX, deltaY, cardDimension, cardZoom);
			deck.add(card);
			card.setBorder(new LineBorder(Color.BLACK,(int) (cardDimension.getWidth()*0.01)));
			//card.setIcon(resizeImage(ImageIO.read(CardContainer.class.getResource("/Images/Cards/FirstPeriod/Blue/1.png")), cardDimension));
			card.addMouseListener(new MouseClickListener() );
			mainPane.add(card, 0);
			deltaX += (int)(cardDimension.getWidth()*0.04);
			deltaY += (int)(cardDimension.getHeight()*0.02);
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
		CardLabel firstFree = searchFirstFree(blueCard);
		if(firstFree != null){
			firstFree.placeCard(cardTaken);
			firstFree.setBorder(new LineBorder(Color.BLACK,(int) (firstFree.getWidth()*0.01)));
		}
	}	
	
	public void addVioletCard(BufferedImage cardTaken){
		CardLabel firstFree = searchFirstFree(violetCard);
		if(firstFree != null){
			firstFree.placeCard(cardTaken);
			firstFree.setBorder(new LineBorder(Color.BLACK,(int) (firstFree.getWidth()*0.01)));
		}
	}
	
	private CardLabel searchFirstFree(List<CardLabel> deck){
		for (CardLabel card : deck) {
			if(!card.hasACard())
				return card;
		}
		return null;
	}
	
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
		
	}
}
