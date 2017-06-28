package it.polimi.ingsw.ps42.message;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.leaderCard.LeaderCard;

/**
 * Class used to know which leader card the player wants to enable
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class LeaderCardUpdateMessage extends Message {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2533166029286132972L;
	LeaderCard card;

	public LeaderCardUpdateMessage(String playerID, LeaderCard card) {
		super(playerID);
		this.card = card;
	}
	
	public LeaderCard getCard() {
		return this.card;
	}
	
	/**
	 * Method used to visit this message
	 */
	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

}
