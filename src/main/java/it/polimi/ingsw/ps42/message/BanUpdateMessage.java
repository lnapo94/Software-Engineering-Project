package it.polimi.ingsw.ps42.message;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;

public class BanUpdateMessage extends Message{

	/* Message to notify the View the assignment of a Ban to a Player
	 */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6705160229187409149L;
	private int banPeriod;
	
	public BanUpdateMessage(String playerID,int banPeriod) {
		super(playerID);
		this.banPeriod = banPeriod;
	}
	
	public int getBan() {
		return banPeriod;
	}
	
	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

}
