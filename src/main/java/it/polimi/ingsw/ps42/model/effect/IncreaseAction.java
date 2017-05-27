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
		
		if(type==action.getType()){
			action.addIncrement(value);
			action.addDiscount(discount);
		}
	}
	

}
