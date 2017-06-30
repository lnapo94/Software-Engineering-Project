package it.polimi.ingsw.ps42.model.action;

import java.io.Serializable;

import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;

/**
 * Prototype of an action, created when a bonus action occurs, 
 * has to be put in the player to be later better defined by the game logic
 * 
 * @author Luca Napoletano, Claudio Montanari
 */
public class ActionPrototype implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1684309278669876519L;

	private ActionType type;
	private int level;
	private Packet discount;
	
	/**
	 * Constructor of the action prototype
	 * 
	 * @param type			Type of the bonus action
	 * @param level			The level of the bonus action
	 * @param discount		A possible discount for the action
	 */
	public ActionPrototype(ActionType type, int level, Packet discount){
		this.type = type;
		this.level = level;
		this.discount = discount;
	}
	
	/**
	 * Getter for the action type
	 * @return	The action type
	 */
	public ActionType getType() {
		return type;
	}
	
	/**
	 * Getter for the level of this bonus action
	 * @return	The level of the bonus action
	 */
	public int getLevel() {
		return level;
	}
	
	/**
	 * Getter for the discount
	 * @return	The discount assigned to this action
	 */
	public Packet getDiscount() {
		return discount;
	}
	
	/**
	 * If the action passed respect the parameters of the action prototype it will pass the check
	 * @param action	The action to control
	 * @return			True if the action passed the test, otherwise False
	 */
	public boolean checkAction(Action action){

		if( checkType( action ) && action.getActionValue() <= this.level)
			return true;
		return false;
	}
	
	/**
	 * Checks if the action passed respect the prototype, considering the take-all action type
	 * @param action		The action to verify
	 * @return				True if the action type is conform, otherwise False
	 */
	private boolean checkType(Action action){
		
		if( this.type == ActionType.TAKE_ALL){
			if( action.getType() == ActionType.TAKE_BLUE || action.getType() == ActionType.TAKE_GREEN
					|| action.getType() == ActionType.TAKE_VIOLET || action.getType() == ActionType.TAKE_YELLOW)
				return true;

			else return false;
		}
		
		else return this.type == action.getType();
			
	}
	
	/**
	 * Method used to clone the action prototype to send it to the client
	 */
	@Override
	public ActionPrototype clone() {
		Packet cloneDiscount = null;
		ActionType cloneType = this.type;
		if( this.discount != null)
			cloneDiscount = discount.clone();
		return new ActionPrototype(cloneType, this.level, cloneDiscount);
	}
}
