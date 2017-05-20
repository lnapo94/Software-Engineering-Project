package it.polimi.ingsw.ps42.model.effect;

import it.polimi.ingsw.ps42.model.Player;
import it.polimi.ingsw.ps42.model.enumeration.EffectType;

public abstract class Effect {
	//This is the abstract class for all the game effects, such as Immediate, Permanent and Ban
	//effect
	
	protected Player player;
	private EffectType typeOfEffect;
	
	public Effect(EffectType typeOfEffect) {
		//Generic Constructor for all the class
	}
	
	
	public abstract void enableEffect(Player player);
	//This method is used to enable the exact kind of effect

}
