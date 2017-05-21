package it.polimi.ingsw.ps42.model.effect;

import it.polimi.ingsw.ps42.model.enumeration.Color;
import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;

public class IncreaseSingleFamiliar extends Effect{
	//This effect increase only one familiar's value
	
	private int value;
	private Color color;

	public IncreaseSingleFamiliar(EffectType typeOfEffect) {
		super(typeOfEffect);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void enableEffect(Player player) {
		// TODO Auto-generated method stub
		
	}

}
