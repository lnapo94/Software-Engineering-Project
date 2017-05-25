package it.polimi.ingsw.ps42.model.request;

import java.util.List;

import it.polimi.ingsw.ps42.model.Card;

public abstract class Request {

	/*Abstract Class for asking a request to the player, 
	 * e.g. choose between two effects or two costs or
	 * if wants to pay for a effect
	 */
	private Card card;
	
	public Request(Card card){
		this.card=card;
	}
	
	public abstract void apply(); //Apply the choice the player made (Strategy Pattern)
	
	
}
