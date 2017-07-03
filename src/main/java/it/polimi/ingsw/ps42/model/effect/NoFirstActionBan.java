package it.polimi.ingsw.ps42.model.effect;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;

/**
 * Set a player variable, then the GameLogic knows the player cannot play the first action
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class NoFirstActionBan extends Effect {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4347788955119043485L;
	
	/**
	 * Simple constructor of this effect
	 */
	public NoFirstActionBan() {
		super(EffectType.NO_FIRST_ACTION_BAN);
	}

	/**
	 * Method used to enable this effect
	 */
	@Override
	public void enableEffect(Player player) {
		logger = Logger.getLogger(NoFirstActionBan.class);
		logger.info("Effect: " + this.getTypeOfEffect() + " activated");
		this.player=player;
		player.setCanPlay(false);
	}

	/**
	 * Method used to print this effect in View
	 */
	@Override
	public String print() {
		return "Player can't to his first action";
	}

	/**
	 * Method used to copy this effect
	 */
	@Override
	public NoFirstActionBan clone() {
		return new NoFirstActionBan();
	}
}
