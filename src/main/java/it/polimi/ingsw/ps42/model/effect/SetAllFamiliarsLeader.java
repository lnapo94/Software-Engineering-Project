package it.polimi.ingsw.ps42.model.effect;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.player.Player;

/**
 * Class that represents the leader effect that set all familiars values, e.g. the "Ludovico il Moro" effect.
 * When applied, this effect set all the colored familiars value to the specify one
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class SetAllFamiliarsLeader extends Effect {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3681714471119908383L;
	private int value;
	
	/**
	 * Simple constructor used to enable the logger
	 */
	public SetAllFamiliarsLeader() {
		super();
	}

	/**
	 * The constructor of this effect
	 * @param value		The value to set to the colored familiars
	 */
	public SetAllFamiliarsLeader(int value) {
		super(EffectType.SET_ALL_FAMILIARS_LEADER);
		this.value = value;
	}
	
	/**
	 * Method to enable this effect
	 */
	@Override
	public void enableEffect(Player player) {
		logger = Logger.getLogger(SetAllFamiliarsLeader.class);
		logger.info("Effect: " + this.getTypeOfEffect() + " activated");
		player.getFamiliar(FamiliarColor.ORANGE).setValue(value);
		player.getFamiliar(FamiliarColor.BLACK).setValue(value);
		player.getFamiliar(FamiliarColor.WHITE).setValue(value);
	}

	/**
	 * Method used to copy this effect
	 */
	@Override
	public Effect clone() {
		return new SetAllFamiliarsLeader(value);
	}

	/**
	 * Method used to print this effect in the view
	 */
	@Override
	public String print() {
		return "Player's colored familiars have the value = " + this.value + " from now"; 
	}

}
