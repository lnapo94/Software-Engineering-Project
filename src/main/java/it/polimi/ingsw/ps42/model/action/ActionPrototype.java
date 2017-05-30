package it.polimi.ingsw.ps42.model.action;

import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;

public class ActionPrototype {
	/*Prototype of an action, created when a bonus action occurs, 
	 * has to be put in the player to be later better defined by the game logic
	 */
	private ActionType type;
	private int level;
	private Packet discount;
	
	public ActionPrototype(ActionType type, int level, Packet discount){
		this.type = type;
		this.level = level;
		this.discount = discount;
	}
	
	public ActionType getType() {
		return type;
	}
	public int getLevel() {
		return level;
	}
	public Packet getDiscount() {
		return discount;
	}
	
	public boolean checkAction(Action action){
		//If the action passed respect the parameters of the action prototype it will pass the check
		//TO-DO: controllo su take all
		if(action.getType() == this.type && action.getActionValue() <= this.level)
			return true;
		return false;
	}
}
