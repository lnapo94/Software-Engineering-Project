package it.polimi.ingsw.ps42.model.effect;

import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;

public class CanPositioningEverywhereLeader extends Effect{

	protected CanPositioningEverywhereLeader(EffectType typeOfEffect) {
		super(EffectType.CAN_POSITIONING_EVERYWHERE);
	}
	
	@Override
	public void enableEffect(Player player) {
		player.setCanPositioningEverywhere();		
	}

	@Override
	public Effect clone() {
		return this;
	}

	@Override
	public String print() {
		// TODO Auto-generated method stub
		return null;
	}


}
