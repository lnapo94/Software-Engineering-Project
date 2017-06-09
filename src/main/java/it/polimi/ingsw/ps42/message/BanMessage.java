package it.polimi.ingsw.ps42.message;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.effect.Effect;

public class BanMessage extends Message{

	/* Message to set the Bans in the View when the game starts
	 */
	
	private Effect firstEffect;
	private Effect secondEffect;
	private Effect thirdEffect;
	
	public BanMessage( String playerID, Effect firstEffect, Effect secondEffect, Effect thirdEffect) {
		super(playerID);
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
