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
	
	public void checkAction(Action action){
		
	}
}
