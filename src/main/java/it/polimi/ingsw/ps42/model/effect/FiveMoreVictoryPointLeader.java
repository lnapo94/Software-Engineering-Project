package it.polimi.ingsw.ps42.model.effect;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;

/**
 * Class that represents the leader effect which give to the user five more
 * victory points every time he pays his faith point
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class FiveMoreVictoryPointLeader extends Effect{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3933038593087389609L;
	
	/**
	 * Simple empty constructor for this effect
	 */
	public FiveMoreVictoryPointLeader() {
		super(EffectType.FIVE_MORE_VICTORY_POINT);
	}

	/**
	 * Method used to apply this effect
	 */
	@Override
	public void enableEffect(Player player) {
		logger = Logger.getLogger(FiveMoreVictoryPointLeader.class);
		logger.info("Effect: " + this.getTypeOfEffect() + " activated");
		player.setFiveMoreVictoryPoint();
	}

	/**
	 * Clone this effect
	 */
	@Override
	public Effect clone() {
		return new FiveMoreVictoryPointLeader();
	}

	/**
	 * Method used to print this effect in the view
	 */
	@Override
	public String print() {
		return "Player has five more victory points every time he uses his faithpoint";
	}

}
