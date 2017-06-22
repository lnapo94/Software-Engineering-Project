package it.polimi.ingsw.ps42.model.effect;

import java.io.Serializable;

import it.polimi.ingsw.ps42.model.Printable;
import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;

public abstract class Effect implements Printable, Serializable{
	//This is the abstract class for all the game effects, such as Immediate, Permanent and Ban
	//effect
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3156543073602122174L;
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
	
	@Override
	public abstract Effect clone();
	
}
