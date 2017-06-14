package it.polimi.ingsw.ps42.message;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.effect.Effect;

public class BanUpdateMessage extends Message{

	/* Message to notify the View the assignment of a Ban to a Player
	 */
	
	private Effect ban;
	private int banPeriod;
	
	public BanUpdateMessage(String playerID, Effect ban) {
		super(playerID);
		this.ban = ban;
	}
	
	public int getBan() {
		return banPeriod;
	}
	
	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

}
