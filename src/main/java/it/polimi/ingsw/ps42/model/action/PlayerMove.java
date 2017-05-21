package it.polimi.ingsw.ps42.model.action;

import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.Color;

public class PlayerMove {
	//Class that contains the informations given from the player
	//The gamelogic parse these information to create a complete Action
	
	private ActionType type;
	private Color familiarColor;
	
	//This variable is used to know the correct position in the arraylist<position>
	private int position;
	
	//Increase value of an Action paying with slaves
	private int increaseValue;
	
	public PlayerMove(ActionType type, Color familiarColor, int position, int increaseValue) {
		//Construct the player move
	}
	
	public Action parse() {
		
	}
	
	

}
