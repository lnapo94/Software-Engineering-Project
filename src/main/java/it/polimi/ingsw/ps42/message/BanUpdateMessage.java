package it.polimi.ingsw.ps42.message;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.effect.Effect;

public class BanUpdateMessage extends Message{

	/* Message to notify the View the assignment of a Ban to a Player
	 */
	
	private Effect ban;
	
	public BanUpdateMessage(String playerID, Effect ban) {
		super(playerID);
		this.ban = ban;
	}
	
	public Effect getBan() {
		return ban;
	}
	
	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

}
