package it.polimi.ingsw.ps42.model.effect;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.model.action.Action;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;

/**
 * Create a particular effect that increments the level of an action.
 * @author Luca Napoletano, Claudio Montanari
 */
public class IncreaseAction extends Effect {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4630650466329490163L;
	private ActionType type;
	private int value;
	private Packet discount;
	
	//Variable used to know if the effect is activated yet or not
	private boolean yetActivated;
	
	/**
	 * Simple empty constructor used to initialize the logger
	 */
	public IncreaseAction() {
		super();
	}

	/**
	 * The constructor of this effect
	 * 
	 * @param type		The type of this action to increase
	 * @param value		The increment value
	 * @param discount	The discount to give to the incremented action
	 */
	public IncreaseAction(ActionType type, int value, Packet discount) {
		super(EffectType.INCREASE_ACTION);
		this.type=type;
		this.value=value;
		this.discount=discount;
		yetActivated=false;
	}

	/**
	 * Method used to enable this effect. In this case it means to insert this
	 * effect into a variable in player. This effect will be really activated at the correct
	 * moment. Like the Professor Oak said: There is a time and a place for everything,
	 * but not here and not now
	 */
	@Override
	public void enableEffect(Player player) {
		//Like the Professor Oak said: There is a time and a place for everything,
		//but not here and not now
		logger = Logger.getLogger(IncreaseAction.class);
		
		if(yetActivated==false){		//If not activated yet then add the effect to the player
			logger.info("Effect: " + this.getTypeOfEffect() + " activated");
			this.player=player;
			player.addIncreaseEffect(this);
			yetActivated=true; 
		
		}
	}
	
	/**
	 * Method used to enable this effect at the correct time. It is called in actions
	 * @param action	The action to check and increase
	 */
	public void activeIncrease(Action action){
		//Increase the current action and add a discount if equals the type of the effect
		logger = Logger.getLogger(IncreaseAction.class);
		
		if( checkType(action)){
			logger.info("Effect: " + this.getTypeOfEffect() + " activated its increment");
			action.addIncrement(value);
			action.addDiscount(discount);
		}
	}

	/**
	 * Private method to check if the passed action is to enable by checking the type
	 * @param action	The action to check
	 * @return			True if the action is interested by this effect, False in other cases
	 */
	private boolean checkType(Action action){
		if( type == ActionType.TAKE_ALL){
			if( action.getType() == ActionType.TAKE_BLUE || action.getType() == ActionType.TAKE_GREEN
					|| action.getType() == ActionType.TAKE_VIOLET || action.getType() == ActionType.TAKE_YELLOW)
				return true;
			else return false;
		}
		else return type == action.getType();
	}
	
	/**
	 * Method used to print this effect in the view
	 */
	@Override
	public String print() {
		return "Player can improve his " + this.type.toString() + " actions \n" +
				"Increment: " + this.value + "\n" + 
				"Discount: " + this.discount.print() + "\n";
	}
	
	/**
	 * Method used to copy this effect
	 */
	@Override
	public IncreaseAction clone() {
		ActionType cloneType = this.type;
		Packet cloneDiscount = null;
		if( discount != null)
			cloneDiscount = discount.clone();
		return new IncreaseAction(cloneType, this.value, cloneDiscount);
	}

}
