package it.polimi.ingsw.ps42.model.effect;

import it.polimi.ingsw.ps42.model.action.ActionPrototype;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;

public class DoAction extends Effect{
	//Allow player to do another action
	//This creates an ActionPrototype, that will be consumed by the GameLogic
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4081441943590977198L;
	private ActionType type;
	private int actionLevel;
	private Packet discount;
	private Obtain obtainEffect;
	
	public DoAction(ActionType type, int actionLevel, Packet discount, Obtain obtainEffect) {
		super(EffectType.DO_ACTION);
		this.type=type;
		this.actionLevel=actionLevel;
		this.discount=discount;
		this.obtainEffect = obtainEffect;
	}

	@Override
	public void enableEffect(Player player) {
		this.player=player;
		
		player.setBonusAction(new ActionPrototype(type, actionLevel, discount));
		if(obtainEffect != null)
			obtainEffect.enableEffect(player);
	}

	@Override
	public String print() {
		return "Player can do a bonus Action: \n" + "ActionType: " + this.type.toString() + "\n" +
				"Level: " + this.actionLevel + "\n" + 
				"Discount: " + this.discount.print() + "\n" +
				"and player also has: " + this.obtainEffect.print();
	}
	
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
