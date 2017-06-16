package it.polimi.ingsw.ps42.model.leaderCard;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.StaticList;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

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
	
	public boolean satisfyRequirement(Player owner) {
		//Check if it is possible to enable the leader card by the control
		//of this requirements
		
		if(color != null) {
			StaticList<Card> ownerCard = owner.getCardList(color);
			if(ownerCard.size() > cardRequirements)
				return false;
		}
		if(resourceRequirements != null)
			for(Unit unit : resourceRequirements) {
				if(unit.getQuantity() > owner.getResource(unit.getResource()))
					return false;
			}
		
		return true;
	}
	
	@Override
	public LeaderRequirements clone() {
		Packet tempResourceRequirements = null;
		
		if(resourceRequirements != null)
			tempResourceRequirements = resourceRequirements.clone();
		return new LeaderRequirements(tempResourceRequirements, color, cardRequirements);
	}
}
