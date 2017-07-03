package it.polimi.ingsw.ps42.model.effect;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;

public class NoMoneyMalusLeader extends Effect{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6454523967505855024L;
	
	/**
	 * Simple constructor of this effect
	 */
	public NoMoneyMalusLeader() {
		super(EffectType.NO_MONEY_MALUS);
	}

	/**
	 * Method used to enable this effect
	 */
	@Override
	public void enableEffect(Player player) {
		logger = Logger.getLogger(NoMoneyMalusLeader.class);
		logger.info("Effect: " + this.getTypeOfEffect() + " activated");
		player.setNoMoneyMalus();
	}

	/**
	 * Method used to copy this effect
	 */
	@Override
	public Effect clone() {
		return new NoMoneyMalusLeader();
	}

	/**
	 * Method used to print this effect in View
	 */
	@Override
	public String print() {
		return "Player will not pay if there is another familiar in tower";
	}

}
