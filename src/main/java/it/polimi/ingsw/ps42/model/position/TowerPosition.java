package it.polimi.ingsw.ps42.model.position;

public class TowerPosition  extends Position {
	
	/*Implementation for the tower position, has a card that have to be 
	* setted in every round. The player can take the card if he place the 
	* familiar in the position (many checks have to be done before)
	*/
	private Card card;
	
	public void setCard(Card card) {
		this.card = card;
	}
	public Card getCard() {			//Returns the card, used to check if the card may be taken from the palyer
		return card;
	}
	public boolean hasCard(Card card){		//Check if there is a card in the position
		
		
	}
	public void removeCard(){		//Remove the card from the position
		
	}
}
