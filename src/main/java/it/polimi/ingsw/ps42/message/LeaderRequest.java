package it.polimi.ingsw.ps42.message;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;

public class LeaderRequest extends Message{
	
	private FamiliarColor color;
	private boolean isForCopy;
	private String leaderCard;
	
	public LeaderRequest(String playerID, boolean isForCopy) {
		super(playerID);
		this.isForCopy = isForCopy;
	}
	
	public void setColor(FamiliarColor color) {
		this.color = color;
	}
	
	public FamiliarColor getColor() {
		return this.color;
	}
 	
	public boolean isForCopy() {
		return this.isForCopy;
	}
	
	public String getLeaderCard() {
		return this.leaderCard;
	}
	
	public void setLeaderCard(String leaderCard) {
		this.leaderCard = leaderCard;
	}
	
	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

}
