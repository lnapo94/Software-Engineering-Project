package it.polimi.ingsw.ps42.model.request;

import java.util.List;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.exception.WrongChoiceException;

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
	
	public void setChoice(int choice) throws WrongChoiceException {
		if(validChoice(choice)){
			this.choice = choice;
		}
		else throw new WrongChoiceException("The choice for this request is not valid");
	}
	
	public boolean validChoice(int choice){
		return possibleChoice.contains(choice);
		
	}
	public int getChoice() {
		return choice;
	}
	
	public void enableImmediateEffect(){
		card.enableImmediateEffect(choice);
	}
	
	public void enablePermanentEffect(){
		card.enablePermanentEffect(choice);
	}
	
	public void finalEffect(){
		card.enableFinalEffect(choice);
	}
	
	
}
