package it.polimi.ingsw.ps42.model.request;

import java.util.List;

import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.exception.WrongChoiceException;
import it.polimi.ingsw.ps42.model.player.Player;

public class CouncilRequest {
	
	private List<Obtain> choice;
	private final int quantity;
	private final List<Obtain> possibleChoice;
	
	public CouncilRequest(List<Obtain> possibleChoice, int quantity) {
		this.possibleChoice=possibleChoice;
		this.quantity=quantity;
	}
	
	public List<Obtain> getPossibleChoice() {
		return possibleChoice;
	}
	
	public void addChoice(int index) throws WrongChoiceException{
		if(validChoice(index)){
			this.choice.add(possibleChoice.remove(index));
		}
		else throw new WrongChoiceException("The choice for this request is not valid");
		
	}
	
	private boolean validChoice(int choice){
		
		return false;
	}
	public void apply(Player player) {
		
	}

	
}
