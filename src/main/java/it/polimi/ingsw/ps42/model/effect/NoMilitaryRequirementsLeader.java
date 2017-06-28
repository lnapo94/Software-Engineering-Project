package it.polimi.ingsw.ps42.model.effect;

import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;

public class NoMilitaryRequirementsLeader extends Effect{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2889343737369528024L;
	
	public NoMilitaryRequirementsLeader() {
		super(EffectType.NO_MILITARY_REQUIREMENTS);
	}

	@Override
	public void enableEffect(Player player) {
		logger.info("Effect: " + this.getTypeOfEffect() + " activated");
		player.setMilitaryRequirements();
	}

	@Override
	public Effect clone() {
		return this;
	}

	@Override
	public String print() {
		return "Player can not satisfy the military requirements to take green cards";
	}

}
