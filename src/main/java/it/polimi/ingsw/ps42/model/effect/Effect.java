package it.polimi.ingsw.ps42.model.effect;

import it.polimi.ingsw.ps42.model.Printable;
import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;

public abstract class Effect implements Printable{
	//This is the abstract class for all the game effects, such as Immediate, Permanent and Ban
	//effect
	
	protected Player player;
	protected EffectType typeOfEffect;
	
	protected Effect(EffectType typeOfEffect) {
		//Generic Constructor for all the class
		this.typeOfEffect=typeOfEffect;
		this.player = null;
	}
	
	
	public abstract void enableEffect(Player player);
	//This method is used to enable the exact kind of effect (Strategy pattern)
	
	public EffectType getTypeOfEffect() {
		return typeOfEffect;
	}
	
}
