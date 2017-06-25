package it.polimi.ingsw.ps42.model.effect;

import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.exception.WrongColorException;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;

public class IncreaseSingleFamiliar extends Effect{
	//This effect increase only one familiar's value
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -752243268908442L;
	private int value;
	private FamiliarColor color;
	
	//Logger
	private transient Logger logger = Logger.getLogger(IncreaseSingleFamiliar.class);

	public IncreaseSingleFamiliar(int value, FamiliarColor color) {
		
		super(EffectType.INCREASE_SINGLE_FAMILIAR);
		this.value=value;
		this.color=color;
	}

	@Override
	public void enableEffect(Player player) {
		//Increments the value of a single familiar for the current round
		this.player=player;
		try{
			logger.info("Effect: " + this.getTypeOfEffect() + " activated");
			Familiar playerFamiliar=player.getFamiliar(color);
			playerFamiliar.setIncrement(value);
		}
		catch (WrongColorException e) {
			logger.error("Familiars increase failed beacause of a wrong initialization of the effect");
		}
	}

	@Override
	public String print() {
		return "Player has +" + this.value + " for his " + this.color.toString() + " familiar";
	}

	@Override
	public IncreaseSingleFamiliar clone() {
		FamiliarColor cloneColor = this.color;
		return new IncreaseSingleFamiliar(this.value, cloneColor);
	}
	
}
