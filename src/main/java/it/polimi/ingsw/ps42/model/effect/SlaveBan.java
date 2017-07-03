package it.polimi.ingsw.ps42.model.effect;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;

/**
 * A player need more slaves to increase his familiars. This ban depends on the divisory.
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class SlaveBan extends Effect {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5371903224725167322L;
	private int divisory;
	
	/**
	 * Simple constructor used to enable the logger
	 */
	public SlaveBan() {
		super();
	}

	/**
	 * Constructor of this effect
	 * 
	 * @param divisory	The value used to divide the slave used by a player. E.G. if this effect is
	 * 					activated, in the default case, the player needs 2 slaves to increment
	 * 					an action of 1 point. The divisory of the example will be 2
	 */
	public SlaveBan(int divisory) {
		super(EffectType.SLAVE_BAN);
		this.divisory=divisory;
		
	}

	/**
	 * Method used to enable this effect
	 */
	@Override
	public void enableEffect(Player player) {
		logger = Logger.getLogger(SlaveBan.class);
		logger.info("Effect: " + this.getTypeOfEffect() + " activated");
		logger.info("Divisory: " + this.divisory);
		this.player=player;
		
		player.setDivisory(divisory);
		
	}

	/**
	 * Method used to print this effect in the view
	 */
	@Override
	public String print() {
		return "Player can increment something paying " + 1 / this.divisory + " slaves for 1 point";
	}
	
	/**
	 * Method used to copy this effect
	 */
	@Override
	public SlaveBan clone() {
		return new SlaveBan(divisory);
	}
}
