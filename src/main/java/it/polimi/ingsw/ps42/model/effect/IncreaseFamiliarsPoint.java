package it.polimi.ingsw.ps42.model.effect;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.exception.WrongColorException;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;

public class IncreaseFamiliarsPoint extends Effect{
	//In this effect all the familiars' points are increased
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4636122239357858799L;
	private int value;
	
	public IncreaseFamiliarsPoint() {
		super();
	}
	public IncreaseFamiliarsPoint(int value) {
		super(EffectType.INCREASE_FAMILIARS);
		this.value=value;
	}

	@Override
	public void enableEffect(Player player) {
		//Increase the value of every familiar for the current round
		this.player=player;
		logger = Logger.getLogger(IncreaseFamiliarsPoint.class);
		try{
			logger.info("Effect: " + this.getTypeOfEffect() + " activated");
			Familiar playerFamiliar=player.getFamiliar(FamiliarColor.ORANGE);
			playerFamiliar.setIncrement(value);
			playerFamiliar=player.getFamiliar(FamiliarColor.BLACK);
			playerFamiliar.setIncrement(value);
			playerFamiliar=player.getFamiliar(FamiliarColor.WHITE);
			playerFamiliar.setIncrement(value);
		}
		catch (WrongColorException e) {
			logger.error("Familiars increase failed beacause of a wrong initialization of the effect");
			logger.info(e);
		}
	}

	@Override
	public String print() {
		return "Player has +" + this.value + " for all his color familiars";
	}

	@Override
	public IncreaseFamiliarsPoint clone() {
		return new IncreaseFamiliarsPoint(this.value);
	}
	
}
