package it.polimi.ingsw.ps42.message;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.effect.Effect;

/**
 * Message used to set the Bans in the View when the game starts
 * @author Luca Napoletano
 *
 */
public class BanMessage extends Message{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 715916254380472322L;
	
	private Effect firstEffect;
	private Effect secondEffect;
	private Effect thirdEffect;
	
	/**
	 * Constructor of the BanMessage
	 * 
	 * @param firstEffect		The first casual ban of the current match
	 * @param secondEffect		The second casual ban of the current match
	 * @param thirdEffect		The third casual ban of the current match
	 */
	public BanMessage( Effect firstEffect, Effect secondEffect, Effect thirdEffect) {
		super(null);
		this.firstEffect = firstEffect;
		this.secondEffect = secondEffect;
		this.thirdEffect = thirdEffect;
	}
	
	/**
	 * Getter
	 * 
	 * @return The first Ban in the message
	 */
	public Effect getFirstEffect() {
		return firstEffect;
	}
	
	/**
	 * Getter
	 * 
	 * @return The second Ban in the message
	 */
	public Effect getSecondEffect() {
		return secondEffect;
	}
	
	/**
	 * Getter
	 * 
	 * @return The third Ban in the message
	 */
	public Effect getThirdEffect() {
		return thirdEffect;
	}
	
	/**
	 * Method called to visit this message
	 */
	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

}
