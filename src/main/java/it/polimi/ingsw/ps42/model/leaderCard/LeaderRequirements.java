package it.polimi.ingsw.ps42.model.leaderCard;

import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;

public class LeaderRequirements {
	
	//Used in leader card for normal requirements
	private Packet resourceRequirements;
	
	//Used in case the leader card has some cards requirements
	private CardColor color;
	private int cardRequirements;
	
	public LeaderRequirements(Packet resourceRequirements, CardColor color, int cardRequirements) {
		this.resourceRequirements = resourceRequirements;
		this.color = color;
		this.cardRequirements = cardRequirements;
	}
	
	@Override
	public LeaderRequirements clone() {
		return null;
	}
}
