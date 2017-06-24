package it.polimi.ingsw.ps42.message;

import java.io.Serializable;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitable;

public abstract class Message implements GenericMessage, Visitable, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 784700456898364021L;
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
