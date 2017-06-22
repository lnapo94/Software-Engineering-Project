package it.polimi.ingsw.ps42.message;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.StaticList;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;

public class CardsMessage extends Message {

	/* Message to notify the View of the new cards on a tower for the next round
	 */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5326429757767619958L;
	private CardColor color;
	private StaticList<Card> deck;
	
	public CardsMessage( StaticList<Card> deck, CardColor color) {
		super(null);
		this.deck = deck;
		this.color = color;
	}
	
	public CardColor getColor() {
		return color;
	}
	
	public StaticList<Card> getDeck() {
		return deck;
	}
	
	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

}
