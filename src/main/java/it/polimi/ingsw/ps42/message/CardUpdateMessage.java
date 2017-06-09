package it.polimi.ingsw.ps42.message;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;

public class CardUpdateMessage extends Message{

	/* Message to notify the View of the movement of a Card (only if a player performs a takeCard Action)
	 */
	
	private ActionType type;
	private int position;
	
	public CardUpdateMessage(String playerID, ActionType type, int position) {
		super(playerID);
		this.type = type;
		this.position = position;
	}
	
	public int getPosition() {
		return position;
	}
	
	public ActionType getType() {
		return type;
	}
	
	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

}
