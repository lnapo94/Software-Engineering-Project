package it.polimi.ingsw.ps42.model.effect;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;

public class NoMilitaryRequirementsLeader extends Effect{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2889343737369528024L;
	
	/**
	 * Simple constructor of this effect
	 */
	public NoMilitaryRequirementsLeader() {
		super(EffectType.NO_MILITARY_REQUIREMENTS);
	}

	/**
	 * Method used to enable this effect
	 */
	@Override
	public void enableEffect(Player player) {
		logger = Logger.getLogger(NoMilitaryRequirementsLeader.class);
		logger.info("Effect: " + this.getTypeOfEffect() + " activated");
		player.setMilitaryRequirements();
	}

	/**
	 * Method used to copy this effect
	 */
	@Override
	public Effect clone() {
		return new NoMilitaryRequirementsLeader();
	}

	/**
	 * Method used to print this effect in View
	 */
	@Override
	public String print() {
		return "Player can not satisfy the military requirements to take green cards";
	}

}
