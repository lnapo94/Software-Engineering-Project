package it.polimi.ingsw.ps42.model.position;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;

public class TowerPosition  extends Position {
	
	/*Implementation for the tower position, has a card that have to be 
	* setted in every round. The player can take the card if he place the 
	* familiar in the position (many checks have to be done before)
	*/
	private Card card;
	
	public TowerPosition(ActionType type, int level, Obtain bonus, int malus) {
		super(type, level, bonus, malus);
		
	}
	
	public void setCard(Card card) {
		this.card = card;
	}
	public Card getCard() {			//Returns the card, used to check if the card may be taken from the player
		return card;
	}
	public boolean hasCard(){		//Check if there is a card in the position
		return card == null;
		
	}
	public void removeCard(){		//Remove the card from the position
		this.card=null;
	}
}
