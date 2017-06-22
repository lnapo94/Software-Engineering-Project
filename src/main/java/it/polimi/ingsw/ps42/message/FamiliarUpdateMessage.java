package it.polimi.ingsw.ps42.message;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;

public class FamiliarUpdateMessage extends Message {
	//Used to update the position of one familiar in the view
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2882450080963747924L;
	private ActionType action;
	private FamiliarColor color;
	private int position;
	
	public FamiliarUpdateMessage(String playerID, FamiliarColor color, ActionType action, int position) {
		super(playerID);
		this.action = action;
		this.color = color;
		this.position = position;
	}
	
	public ActionType getAction() {
		return this.action;
	}
	
	public FamiliarColor getColor() {
		return this.color;
	}
	
	public int getPosition() {
		return this.position;
	}
	
	@Override
	public void accept(Visitor v) {
		//Method used to start the visit
		v.visit(this);
	}

}
