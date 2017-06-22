package it.polimi.ingsw.ps42.message;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;

public class PlayerMove extends Message{
	//Class that contains the informations given from the player
	//The game logic parse these information to create a complete Action
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4496592260286365702L;
	private ActionType type;
	private FamiliarColor familiarColor;
	
	//This variable is used to know the correct position in the arraylist<position>
	private int position;
	
	//Increase value of an Action paying with slaves
	private int increaseValue;
	
	public PlayerMove(String playerID, ActionType type, FamiliarColor familiarColor, int position, int increaseValue) {
		//Construct the player move
		super(playerID);
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

	@Override
	public void accept(Visitor v) {
		//Method used to start the visit
		v.visit(this);
	}
}
