package it.polimi.ingsw.ps42.model.position;

import java.util.ArrayList;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.player.Familiar;

public class YieldAndProductPosition extends Position {
	
	/*Implementation for both the Yield and Product position since 
	 * they do the same job but on different type of cards:
	 * they allow the player to do a Yeld/Product action
	*/
	private ArrayList<Familiar> bonusFamiliar;
	
	public YieldAndProductPosition(ActionType type, int level, Obtain bonus, int malus) {
		super(type, level, bonus, malus);
	}
	
	public void enableCards(ArrayList<Card> cards){		//Enables the permanent effect of all the cards in the arrayList
		
	}
	
	public void setBonusFamiliar(Familiar familiar) {		//Adds a bonus Familiar in the position, requires a Leader Card activation
		
	}
	
	public ArrayList<Familiar> getBonusFamiliar() {			//Returns all the bonus Familiar in the position
		return bonusFamiliar;
	}
	

}
