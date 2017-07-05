package it.polimi.ingsw.ps42.message;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.leaderCard.LeaderCard;

public class DiscardLeaderCard extends Message{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1934774281863810604L;
	
	private LeaderCard card;

	public DiscardLeaderCard(String playerID, LeaderCard card) {
		super(playerID);
		this.card = card;
	}
	
	public LeaderCard getCard() {
		return this.card;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

}
