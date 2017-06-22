package it.polimi.ingsw.ps42.message;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.exception.WrongChoiceException;
import it.polimi.ingsw.ps42.model.player.Player;

public class CouncilRequest extends Message {
	//Request used to know what kind of council player wants
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8759529288193414560L;
	private List<Obtain> choice;
	private final int quantity;
	private final List<Obtain> possibleChoice;
	
	public CouncilRequest(String playerID, List<Obtain> possibleChoice, int quantity) {
		super(playerID);
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
		if(validChoice(index)){
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

	@Override
	public void accept(Visitor v) {
		//Method used to start the visit
		v.visit(this);
	}

		
}
