package it.polimi.ingsw.ps42.message;

import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;

public class PlayerMove {
	//Class that contains the informations given from the player
	//The gamelogic parse these information to create a complete Action
	
	private ActionType type;
	private FamiliarColor familiarColor;
	
	//This variable is used to know the correct position in the arraylist<position>
	private int position;
	
	//Increase value of an Action paying with slaves
	private int increaseValue;
	
	public PlayerMove(ActionType type, FamiliarColor familiarColor, int position, int increaseValue) {
		//Construct the player move
		this.type = type;
		this.familiarColor = familiarColor;
		this.position = position;
		this.increaseValue = increaseValue;
	}
	
	public ActionType getActionType() {
		return type;
	}
	
	public FamiliarColor getFamiliarColor() {
		return familiarColor;
	}
	
	public int getPosition() {
		return position;
	}
	
	public int getIncrementWithSlave() {
		return increaseValue;
	}
}
