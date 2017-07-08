package it.polimi.ingsw.ps42.model.effect;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.player.Player;

/**
 * Effect used in leader cards to set one familiar value
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class SetSingleFamiliarLeader extends Effect{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3007031091349849711L;
	private FamiliarColor color;
	private int value;
	
	/**
	 * Simple constructor to enable the logger
	 */
	public SetSingleFamiliarLeader() {
		super();
	}
	
	/**
	 * The constructor of this effect
	 * @param value		The value to increase to the specify familiar
	 */
	public SetSingleFamiliarLeader(int value) {
		super(EffectType.SET_SINGLE_FAMILIAR_LEADER);
		this.value = value;
	}

	/**
	 * Method used to enable this effect. In this case, this method is used only to create
	 * a particular request to send to the player. Then the player chooses a familiar to apply this effect
	 */
	@Override
	public void enableEffect(Player player) {
		logger = Logger.getLogger(SetSingleFamiliarLeader.class);
		logger.info("Effect: " + this.getTypeOfEffect() + " activated");
		if(this.color != null && this.color != FamiliarColor.NEUTRAL)
			player.getFamiliar(color).setValue(value);
	}
	
	/**
	 * Method used to specify the familiar to apply this effect
	 * @param color
	 */
	public void setFamiliarColor(FamiliarColor color) {
		if(this.color == null)
			this.color = color;
	}
	
	/**
	 * Getter for the value of this effect
	 * @return		The value of this increment
	 */
	public int getIncrementValue() {
		return this.value;
	}

	/**
	 * Method used to copy this effect
	 */
	@Override
	public Effect clone() {
		return new SetSingleFamiliarLeader(value);
	}

	/**
	 * Method used to print this effect in the view
	 */
	@Override
	public String print() {
		return "Player can set a familiar with value = " + this.value;
	}

}
