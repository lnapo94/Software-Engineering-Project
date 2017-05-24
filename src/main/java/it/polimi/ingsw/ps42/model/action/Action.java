package it.polimi.ingsw.ps42.model.action;

import java.util.List;

import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.position.Position;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;

public abstract class Action {	
	
	/*Class for basic action, requires the implementation of checkAction,
	* doAction, createRequest
	*/
	
	private ActionType type;
	private Familiar familiar;
	protected Player player;
	private int positionValue;
	private List<Position> tableLocation;
	private Packet discount;
	
	
	public Action(ActionType type, Familiar familiar,List<Position> tablePosition, int positionInTableList){
		//Constructor for normal action, player is get from familiar
	}
	public Action(ActionType type, Player player,List<Position> tablePosition, int positionInTableList){
		//Constructor for bonus action (no familiar involved, so requires the player) 
		
	}
	
	public abstract void checkAction();		//Does all the required checks before the action is applicated 
	
	public abstract void doAction();		//Apply the player action 
	
	public void modifyActionValue(int value){		//Increments the value of the action 
		
		
	}
	
	private void checkIncreaseEffect(){			//Checks if the player has some increase effects active and apply them
		
		
	}
	
	public void setDiscount(Packet discount) {		//Adds a discount at the cost of the action
		this.discount = discount;
	}
	
	public abstract void createRequest();		//Create a request to better define the action and put it in the player 
	
	public ActionType getType() {
		return type;
	}
	
	public void addIncrement(int increment){
		
	}
	
}
