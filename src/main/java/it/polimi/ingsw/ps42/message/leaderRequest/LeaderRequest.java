package it.polimi.ingsw.ps42.message.leaderRequest;

import it.polimi.ingsw.ps42.message.Message;
import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.leaderCard.LeaderCard;

public abstract class LeaderRequest extends Message{
	
	protected LeaderCard card;
	
	public LeaderRequest(String playerID, LeaderCard card) {
		super(playerID);
		this.card = card;
	}

	public abstract void apply();
	
	@Override
	public abstract void accept(Visitor v);

}
