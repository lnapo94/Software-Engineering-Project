package it.polimi.ingsw.ps42.model.effect;

import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;

public class NoMoneyMalusLeader extends Effect{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6454523967505855024L;
	
	public NoMoneyMalusLeader() {
		super(EffectType.NO_MONEY_MALUS);
	}

	@Override
	public void enableEffect(Player player) {
		logger.info("Effect: " + this.getTypeOfEffect() + " activated");
		player.setNoMoneyMalus();
	}

	@Override
	public Effect clone() {
		return this;
	}

	@Override
	public String print() {
		return "Player will not pay if there is another familiar in tower";
	}

}
