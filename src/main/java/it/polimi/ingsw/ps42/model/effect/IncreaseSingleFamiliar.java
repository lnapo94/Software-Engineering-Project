package it.polimi.ingsw.ps42.model.effect;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.exception.WrongColorException;
import it.polimi.ingsw.ps42.model.player.Player;

/**
 * Simple method used to increment only one familiar
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class IncreaseSingleFamiliar extends Effect{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -752243268908442L;
	private int value;
	private FamiliarColor color;
	
	/**
	 * Simple constructor to initialize the logger
	 */
	public IncreaseSingleFamiliar() {
		super();
	}
	
	/**
	 * The constructor of this effect
	 * 
	 * @param value		The increment value to add to the familiar
	 * @param color		The color of the chosen familiar
	 */
	public IncreaseSingleFamiliar(int value, FamiliarColor color) {
		
		super(EffectType.INCREASE_SINGLE_FAMILIAR);
		this.value=value;
		this.color=color;
	}

	/**
	 * Method used to enable this effect
	 */
	@Override
	public void enableEffect(Player player) {
		//Increments the value of a single familiar for the current round
		this.player=player;
		logger = Logger.getLogger(IncreaseSingleFamiliar.class);
		try{
			logger.info("Effect: " + this.getTypeOfEffect() + " activated");
			player.getFamiliar(color).enableFamiliarEffect(value);
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
		return "Player has +" + this.value + " for his " + this.color.toString() + " familiar";
	}

	/**
	 * Method used to clone this effect
	 */
	@Override
	public IncreaseSingleFamiliar clone() {
		FamiliarColor cloneColor = this.color;
		return new IncreaseSingleFamiliar(this.value, cloneColor);
	}
	
}
