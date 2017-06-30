package it.polimi.ingsw.ps42.model.effect;

import org.apache.log4j.Logger;

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
		logger = Logger.getLogger(FiveMoreVictoryPointLeader.class);
		logger.info("Effect: " + this.getTypeOfEffect() + " activated");
		player.setFiveMoreVictoryPoint();
	}

	@Override
	public Effect clone() {
		return this;
	}

	@Override
	public String print() {
		return "Player has five more victory points every time he uses his faithpoint";
	}

}
