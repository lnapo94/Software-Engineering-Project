package it.polimi.ingsw.ps42.model.effect;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.exception.WrongColorException;
import it.polimi.ingsw.ps42.model.player.Player;

/**
 * Class that represents the increase familiars point effect. This effect
 * increase all the colored familiar value
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class IncreaseFamiliarsPoint extends Effect{
	//In this effect all the familiars' points are increased
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4636122239357858799L;
	private int value;
	
	/**
	 * Simple constructor to initialize the logger
	 */
	public IncreaseFamiliarsPoint() {
		super();
	}
	
	/**
	 * The constructor of this effect
	 * 
	 * @param value		The all familiars increment value
	 */
	public IncreaseFamiliarsPoint(int value) {
		super(EffectType.INCREASE_FAMILIARS);
		this.value=value;
	}

	/**
	 * Method used to enable this effect
	 */
	@Override
	public void enableEffect(Player player) {
		//Increase the value of every familiar for the current round
		this.player=player;
		logger = Logger.getLogger(IncreaseFamiliarsPoint.class);
		try{
			logger.info("Effect: " + this.getTypeOfEffect() + " activated");
			
			player.getFamiliar(FamiliarColor.ORANGE).enableFamiliarEffect(value);
			player.getFamiliar(FamiliarColor.BLACK).enableFamiliarEffect(value);
			player.getFamiliar(FamiliarColor.WHITE).enableFamiliarEffect(value);
		}
		catch (WrongColorException e) {
			logger.error("Familiars increase failed beacause of a wrong initialization of the effect");
			logger.info(e);
		}
	}

	/**
	 * Method used to print this effect in the view
	 */
	@Override
	public String print() {
		return "Player has +" + this.value + " for all his color familiars";
	}

	/**
	 * Method used to clone this effect
	 */
	@Override
	public IncreaseFamiliarsPoint clone() {
		return new IncreaseFamiliarsPoint(this.value);
	}
	
}
