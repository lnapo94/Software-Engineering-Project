package it.polimi.ingsw.ps42.message;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.effect.Effect;

public class BanMessage extends Message{

	/* Message to set the Bans in the View when the game starts
	 */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 715916254380472322L;
	private Effect firstEffect;
	private Effect secondEffect;
	private Effect thirdEffect;
	
	public BanMessage( Effect firstEffect, Effect secondEffect, Effect thirdEffect) {
		super(null);
		this.firstEffect = firstEffect;
		this.secondEffect = secondEffect;
		this.thirdEffect = thirdEffect;
	}
	
	public Effect getFirstEffect() {
		return firstEffect;
	}
	
	public Effect getSecondEffect() {
		return secondEffect;
	}
	
	public Effect getThirdEffect() {
		return thirdEffect;
	}
	
	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

}
