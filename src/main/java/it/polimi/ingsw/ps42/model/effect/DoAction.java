package it.polimi.ingsw.ps42.model.effect;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.model.action.ActionPrototype;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;

/**
 * Allow player to do another action
 * This creates an ActionPrototype, that will be consumed by the GameLogic
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class DoAction extends Effect{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4081441943590977198L;
	private ActionType type;
	private int actionLevel;
	private Packet discount;
	private Obtain obtainEffect;

	/**
	 * Empty constructor of this class
	 */
	public DoAction() {
		super();
	}
	
	/**
	 * The constructor of this effect
	 * 
	 * @param type				The type of the bonus action
	 * @param actionLevel		The level of the action
	 * @param discount			A possible discount of the action
	 * @param obtainEffect		A possible obtain effect for the action
	 */
	public DoAction(ActionType type, int actionLevel, Packet discount, Obtain obtainEffect) {
		super(EffectType.DO_ACTION);
		this.type=type;
		this.actionLevel=actionLevel;
		this.discount=discount;
		this.obtainEffect = obtainEffect;
	}

	/**
	 * Method used to enable this effect
	 */
	@Override
	public void enableEffect(Player player) {
		this.player=player;
		
		logger = Logger.getLogger(DoAction.class);
		
		logger.info("Effect: " + this.getTypeOfEffect() + " activated");
		
		player.setBonusAction(new ActionPrototype(type, actionLevel, discount));
		if(obtainEffect != null)
			obtainEffect.enableEffect(player);
	}

	/**
	 * Method used to print this effect in the Client
	 */
	@Override
	public String print() {
		return "Player can do a bonus Action: \n" + "ActionType: " + this.type.toString() + "\n" +
				"Level: " + this.actionLevel + "\n" + 
				"Discount: " + this.discount.print() + "\n" +
				"and player also has: " + this.obtainEffect.print();
	}
	
	/**
	 * Method to clone this effect to send it in a secure way to the Client
	 */
	@Override
	public DoAction clone() {
		Packet cloneDiscount = null;
		Obtain obtainCopy = null;
		if(obtainEffect != null)
			obtainCopy = obtainEffect.clone();
		if(discount != null)
			cloneDiscount = discount.clone(); 
		ActionType cloneType = this.type;
		return new DoAction(cloneType, this.actionLevel, cloneDiscount, obtainCopy);
	}

}
