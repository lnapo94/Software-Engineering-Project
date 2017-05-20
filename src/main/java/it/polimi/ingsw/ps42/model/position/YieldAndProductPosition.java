package it.polimi.ingsw.ps42.model.position;

import java.util.ArrayList;

public class YieldAndProductPosition extends Position {
	
	/*Implementation for both the Yield and Product position since 
	 * they do the same job but on different type of cards:
	 * they allow the player to do a Yeld/Product action
	*/
	private ArrayList<Familiar> bonusFamiliar;
	
	public void enableCards(ArrayList<Card> cards){		//Enables the permanent effect of all the cards in the arrayList
		
	}
	
	public void setBonusFamiliar(Familiar familiar) {		//Adds a bonus Familiar in the position, requires a Leader Card activation
		
	}
	
	public ArrayList<Familiar> getBonusFamiliar() {			//Returns all the bonus Familiar in the position
		return bonusFamiliar;
	}
	

}
