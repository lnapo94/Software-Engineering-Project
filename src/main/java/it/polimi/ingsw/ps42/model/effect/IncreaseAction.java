package it.polimi.ingsw.ps42.model.effect;

import it.polimi.ingsw.ps42.model.action.Action;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;

public class IncreaseAction extends Effect {
	/*Create a particular effect that increments the level of another Effect.
	 * Be careful to distinguish the immediate from the permanent effect
	*/
	
	private ActionType type;
	private int value;
	private Packet discount;
	
	//Variable used to know if the effect is activated yet or not
	private boolean yetActivated;

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
		
			this.player=player;
			player.addIncreaseEffect(this);
			yetActivated=true; 
		
		}
	}
	public void activeIncrease(Action action){
		//Increase the current action and add a discount if equals the type of the effect
		
		if( checkType(action)){
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
		// TODO Auto-generated method stub
		return null;
	}
	

}
