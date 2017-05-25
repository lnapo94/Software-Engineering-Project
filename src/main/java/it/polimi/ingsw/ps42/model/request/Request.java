package it.polimi.ingsw.ps42.model.request;

import java.util.List;

import it.polimi.ingsw.ps42.model.Card;

public class Request {

	/*Class for asking a request to the player, 
	 * e.g. choose between two effects or two costs or
	 * if wants to pay for a effect
	 */
	private Card card;
	private final List<Integer> possibleChoice;
	private int choice;
	
	public Request(Card card, List<Integer> possibleChoice){
		this.card=card;
		this.possibleChoice=possibleChoice;
		
	}
	
	public List<Integer> getPossibleChoice() {
		return possibleChoice;
	}
	
	public void setChoice(int choice) {
		//TO-DO: fai controlli
		this.choice = choice;
		
	}
	
	public int getChoice() {
		return choice;
	}
	
	public void enableImmediateEffect(){
		
	}
	
	public void enablePermanentEffect(){
		
	}
	
	public void finalEffect(){
		
	}
	
	
}
