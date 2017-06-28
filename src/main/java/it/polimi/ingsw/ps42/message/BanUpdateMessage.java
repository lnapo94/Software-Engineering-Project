package it.polimi.ingsw.ps42.message;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;

/**
 * Message used to notify the assignment of a Ban to a Player in the View
 * @author Luca Napoletano, Claudio Montanari
 * 
 */
public class BanUpdateMessage extends Message{

	/* Message to notify the View the assignment of a Ban to a Player
	 */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6705160229187409149L;
	private int banPeriod;
	
	/**
	 * Constructor of this message
	 * 
	 * @param playerID		The ID of the interested player
	 * @param banPeriod		The period of the ban
	 */
	public BanUpdateMessage(String playerID,int banPeriod) {
		super(playerID);
		this.banPeriod = banPeriod;
	}
	
	/**
	 * Getter for the period contained in the message
	 * 
	 * @return The period in the message
	 */
	public int getBan() {
		return banPeriod;
	}
	
	/**
	 * Method used to visit this message
	 */
	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

}
