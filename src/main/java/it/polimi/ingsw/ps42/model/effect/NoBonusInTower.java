package it.polimi.ingsw.ps42.model.effect;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;

/**
 * This method set a player variable to disable the Tower Bonus
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class NoBonusInTower extends Effect{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3468865507577574258L;
	
	/**
	 * Simple constructor of this effect
	 */
	public NoBonusInTower() {
		super(EffectType.NO_TOWER_BONUS);
		
	}

	/**
	 * Method used to enable this effect
	 */
	@Override
	public void enableEffect(Player player) {
		logger = Logger.getLogger(NoBonusInTower.class);
		logger.info("Effect: " + this.getTypeOfEffect() + " activated");
		this.player=player;
		player.disableBonusInTower();
	}

	/**
	 * Method used to print this effect in View
	 */
	@Override
	public String print() {
		return "Now player can't take the tower bonuses";
	}

	/**
	 * Method used to copy this effect
	 */
	@Override
	public NoBonusInTower clone() {
		return new NoBonusInTower();
	}
}
