package it.polimi.ingsw.ps42.message;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitable;
import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;

public abstract class Message implements Visitable {
	protected String playerID;
	
	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
}
