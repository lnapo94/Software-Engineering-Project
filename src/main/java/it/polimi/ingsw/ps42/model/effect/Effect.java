package it.polimi.ingsw.ps42.model.effect;

import java.io.Serializable;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.model.Printable;
import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;

/**
 * This is the abstract class for all the game effects, such as Immediate, Permanent and Ban
 * effect
 * With this class was implemented the Strategy Pattern
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public abstract class Effect implements Printable, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3156543073602122174L;
	protected transient Player player;
	protected EffectType typeOfEffect;
	
	protected transient Logger logger;
	
	/**
	 * Empty constructor used to initialize the logger
	 */
	public Effect() {
		logger = Logger.getLogger(Effect.class);
	}
	
	/**
	 * Constructor for all the effects
	 * @param typeOfEffect	The type of the effect of the inherited classes
	 */
	protected Effect(EffectType typeOfEffect) {
		//Generic Constructor for all the class
		this();
		this.typeOfEffect=typeOfEffect;
		this.player = null;
	}
	
	
	/**
	 * This method is used to enable the exact kind of effect (Strategy pattern)
	 * @param player	The interested player for the inherited effect
	 */
	public abstract void enableEffect(Player player);
	
	/**
	 * Simple method which return the type of the effect
	 * 
	 * @return	The type of the effect
	 */
	public EffectType getTypeOfEffect() {
		return typeOfEffect;
	}
	
	/**
	 * Method used to clone all the effects
	 */
	@Override
	public abstract Effect clone();
	
}
