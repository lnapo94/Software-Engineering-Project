package it.polimi.ingsw.ps42.message;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;

public class EmptyMove extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5956002235593087386L;

	public EmptyMove(String playerID) {
		super(playerID);
		
	}

	@Override
	public void accept(Visitor v) {
		
		v.visit(this);
	}

}
