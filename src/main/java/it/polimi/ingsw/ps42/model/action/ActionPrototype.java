package it.polimi.ingsw.ps42.model.action;

import java.io.Serializable;

import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;

public class ActionPrototype implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1684309278669876519L;
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

		if( checkType( action ) && action.getActionValue() <= this.level)
			return true;
		return false;
	}
	
	private boolean checkType(Action action){
		//Checks if the action passed respect the prototype, considering the take-all action type
		if( this.type == ActionType.TAKE_ALL){
			if( action.getType() == ActionType.TAKE_BLUE || action.getType() == ActionType.TAKE_GREEN
					|| action.getType() == ActionType.TAKE_VIOLET || action.getType() == ActionType.TAKE_YELLOW)
				return true;

			else return false;
		}
		
		else return this.type == action.getType();
			
	}
	
	@Override
	public ActionPrototype clone() {
		Packet cloneDiscount = null;
		ActionType cloneType = this.type;
		if( this.discount != null)
			cloneDiscount = discount.clone();
		return new ActionPrototype(cloneType, this.level, cloneDiscount);
	}
}
