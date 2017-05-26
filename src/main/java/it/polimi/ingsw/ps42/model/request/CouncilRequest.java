package it.polimi.ingsw.ps42.model.request;

import java.util.ArrayList;
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
		choice=new ArrayList<>();
	}
	
	public List<Obtain> getPossibleChoice() {
		return possibleChoice;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void addChoice(int index) throws WrongChoiceException {
		//Adds a choice only if this is present in the possibleChoice ArrayList
		if(validChoice(index) && possibleChoice.size()>quantity){
			this.choice.add(possibleChoice.remove(index));
		}
		else throw new WrongChoiceException("The choice for this request is not valid");
		
	}
	
	private boolean validChoice(int choice){
		//Checks if the choice made is a valid index
		return possibleChoice.size()>choice;
	}
	public boolean apply(Player player) {
		
		if(choice.size()==quantity){
			for (Obtain obtain : choice) {
				obtain.enableEffect(player);
			}
			return true;
		}
		else return false;
	}

	
}
