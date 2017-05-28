package it.polimi.ingsw.ps42.model.request;

import java.util.List;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.Printable;

public abstract class CardRequest implements RequestInterface {

	/*Class for asking a request to the player, 
	 * e.g. choose between two effects or two costs or
	 * if wants to pay for a effect
	 */
	//List of possible choice to show to the user
	protected List<Printable> possibleChoice;
	
	//Possible choice index in card
	protected List<Integer> possibleChoiceIndex;
	
	protected Card card;
	
	public CardRequest(Card card, List<Integer> possibleChoiceIndex, List<Printable> possibleChoice) {
		this.card = card;
		this.possibleChoice = possibleChoice;
		this.possibleChoiceIndex = possibleChoiceIndex;
	}
	
	public abstract void apply();
}
