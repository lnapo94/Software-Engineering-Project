package it.polimi.ingsw.ps42.model.position;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;

/**Implementation for the tower position, has a card that have to be 
* setted in every round. The player can take the card if he place the 
* familiar in the position (many checks have to be done before)
* 
* @author Luca Napoletano, Claudio Montanari
*/
public class TowerPosition  extends Position {
	
	private Card card;
	
	/**
	 * Constructor for basic TowerPosition
	 * @param type the Type of Tower
	 * @param level the Tower Position level
	 * @param bonus the Tower Position bonus
	 * @param malus the Tower Position malus
	 */
	public TowerPosition(ActionType type, int level, Obtain bonus, int malus) {
		super(type, level, bonus, malus);
		
	}
	
	/**
	 * Setter for the Card 
	 * @param card the card to add to the Position
	 */
	public void setCard(Card card) {
		this.card = card;
	}
	
	/**
	 * Getter for the Position Card, used to check if the card may be taken from the player
	 * @return the card in the Position
	 */
	public Card getCard() {			
		return card;
	}
	
	/**
	 * Check if there is a card in the position
	 * @return true if the Position has a Card
	 */
	public boolean hasCard(){		
		return card != null;
		
	}
	
	/**
	 * Remove the card from the position
	 */
	public void removeCard(){		
		this.card=null;
	}
}
