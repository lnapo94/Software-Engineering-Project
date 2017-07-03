package it.polimi.ingsw.ps42.message;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;

/**
 * Message used to know in View which ban is controlled in this moment by the Controller
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class BanRequest extends Message {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 214665373242179569L;
	private int banNumber;
	private boolean wantPayForBan;

	/**
	 * Constructor
	 * 
	 * @param playerID	The ID player of this ban
	 * @param banNumber	The number of the ban (1, 2 or 3, like the period)
	 */
	public BanRequest(String playerID, int banNumber) {
		super(playerID);
		this.banNumber = banNumber;
	}
	
	/**
	 * Getter for the ban number
	 * 
	 * @return	the number of the ban (1, 2, 3)
	 */
	public int getBanNumber() {
		return banNumber;
	}
	
	/**
	 * Getter for the player's choice
	 * 
	 * @return	True if the player wants to pay faith points for the ban, False otherwise
	 */
	public boolean wantPayForBan() {
		return wantPayForBan;
	}

	/**
	 * Setter for the Player choice, true if the Player wants to pay for the current Ban
	 * 
	 * @param wantPayForBan
	 */
	public void setWantPayForBan(boolean wantPayForBan) {
		this.wantPayForBan = wantPayForBan;
	}
	
	/**
	 * Method used to visit this message
	 */
	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

}
