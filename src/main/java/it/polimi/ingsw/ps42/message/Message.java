package it.polimi.ingsw.ps42.message;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitable;

public abstract class Message implements Visitable {
	
	protected String playerID;
	private boolean isRetrasmission;
	
	public Message( String playerID){
		
		this.playerID = playerID;
		this.isRetrasmission = false;
	}
	
	public String getPlayerID() {
		return playerID;
	}
	
	public void setRetrasmission() {
		this.isRetrasmission = true;
	}
	
	public boolean isRetrasmission() {
		return isRetrasmission;
	}
}
