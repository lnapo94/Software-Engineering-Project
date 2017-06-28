package it.polimi.ingsw.ps42.message;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.StaticList;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;

/**
 * Class used to notify the cards placed in a tower
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class CardsMessage extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5326429757767619958L;
	private CardColor color;
	private StaticList<Card> deck;
	
	/**
	 * Constructor of this class
	 * 
	 * @param deck		The 4 cards placer in the tower
	 * @param color		The color of the cards
	 */
	public CardsMessage( StaticList<Card> deck, CardColor color) {
		super(null);
		this.deck = deck;
		this.color = color;
	}
	
	/**
	 * 
	 * @return	The color of the cards in this message
	 */
	public CardColor getColor() {
		return color;
	}
	
	/**
	 * 
	 * @return	The StaticList of cards in this message
	 */
	public StaticList<Card> getDeck() {
		return deck;
	}
	
	/**
	 * Method used to visit this message
	 */
	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

}
