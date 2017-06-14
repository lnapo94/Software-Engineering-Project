package it.polimi.ingsw.ps42.message;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;

public class LeaderRequest extends Message{

	private boolean isForCopy;
	private String leaderCard;
	
	public LeaderRequest(String playerID, boolean isForCopy) {
		super(playerID);
		this.isForCopy = isForCopy;
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
		// TODO Auto-generated method stub
		
	}

}
