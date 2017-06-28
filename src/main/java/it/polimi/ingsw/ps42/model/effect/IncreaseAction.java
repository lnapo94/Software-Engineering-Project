package it.polimi.ingsw.ps42.model.effect;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.model.action.Action;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;

public class IncreaseAction extends Effect {
	/*Create a particular effect that increments the level of another Effect.
	 * Be careful to distinguish the immediate from the permanent effect
	*/
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4630650466329490163L;
	private ActionType type;
	private int value;
	private Packet discount;
	
	private transient Logger logger = Logger.getLogger(IncreaseAction.class);
	
	//Variable used to know if the effect is activated yet or not
	private boolean yetActivated;
	
	public IncreaseAction() {
		super();
	}

	public IncreaseAction(ActionType type, int value, Packet discount) {
		super(EffectType.INCREASE_ACTION);
		this.type=type;
		this.value=value;
		this.discount=discount;
		yetActivated=false;
	}

	@Override
	public void enableEffect(Player player) {
		if(yetActivated==false){		//If not activated yet then add the effect to the player
			logger.info("Effect: " + this.getTypeOfEffect() + " activated");
			this.player=player;
			player.addIncreaseEffect(this);
			yetActivated=true; 
		
		}
	}
	public void activeIncrease(Action action){
		//Increase the current action and add a discount if equals the type of the effect
		
		if( checkType(action)){
			logger.info("Effect: " + this.getTypeOfEffect() + " activated its increment");
			action.addIncrement(value);
			action.addDiscount(discount);
		}
	}

	private boolean checkType(Action action){
		if( type == ActionType.TAKE_ALL){
			if( action.getType() == ActionType.TAKE_BLUE || action.getType() == ActionType.TAKE_GREEN
					|| action.getType() == ActionType.TAKE_VIOLET || action.getType() == ActionType.TAKE_YELLOW)
				return true;
			else return false;
		}
		else return type == action.getType();
	}
	@Override
	public String print() {
		return "Player can improve his " + this.type.toString() + " actions \n" +
				"Increment: " + this.value + "\n" + 
				"Discount: " + this.discount.print() + "\n";
	}
	
	@Override
	public IncreaseAction clone() {
		ActionType cloneType = this.type;
		Packet cloneDiscount = null;
		if( discount != null)
			cloneDiscount = discount.clone();
		return new IncreaseAction(cloneType, this.value, cloneDiscount);
	}

}
