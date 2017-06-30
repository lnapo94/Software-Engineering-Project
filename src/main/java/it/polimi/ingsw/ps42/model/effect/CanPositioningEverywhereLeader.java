package it.polimi.ingsw.ps42.model.effect;

import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;

/**
 * Class to create the Can Position everywhere effect
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class CanPositioningEverywhereLeader extends Effect {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5946490102125140634L;

	/**
	 * Constructor of this class
	 */
	public CanPositioningEverywhereLeader() {
		super(EffectType.CAN_POSITIONING_EVERYWHERE);
	}
	
	/**
	 * Method used to enable this effect
	 */
	@Override
	public void enableEffect(Player player) {
		logger.info("Effect: " + this.getTypeOfEffect() + " activated");
		player.setCanPositioningEverywhere();		
	}

	/**
	 * Method to clone this effect to send it in a secure way to the Client
	 */
	@Override
	public Effect clone() {
		return this;
	}

	/**
	 * Method used to print this effect in the Client
	 */
	@Override
	public String print() {
		return getTypeOfEffect().toString();
	}


}
