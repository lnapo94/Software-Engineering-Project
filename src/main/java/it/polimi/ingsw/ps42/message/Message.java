package it.polimi.ingsw.ps42.message;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitable;

public abstract class Message implements Visitable {
	protected String playerID;
	
	public Message( String playerID){
		
		this.playerID = playerID;
	}
	
	public String getPlayerID() {
		return playerID;
	}
}
