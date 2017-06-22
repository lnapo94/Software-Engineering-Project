package it.polimi.ingsw.ps42.model.effect;

import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;

public class FiveMoreVictoryPointLeader extends Effect{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3933038593087389609L;

	public FiveMoreVictoryPointLeader() {
		super(EffectType.FIVE_MORE_VICTORY_POINT);
	}

	@Override
	public void enableEffect(Player player) {
		player.setFiveMoreVictoryPoint();
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
