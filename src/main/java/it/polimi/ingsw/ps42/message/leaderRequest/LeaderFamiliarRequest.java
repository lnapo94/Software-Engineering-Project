package it.polimi.ingsw.ps42.message.leaderRequest;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.leaderCard.LeaderCard;

public class LeaderFamiliarRequest extends LeaderRequest {
	
	private FamiliarColor color;

	public LeaderFamiliarRequest(String playerID, LeaderCard card) {
		super(playerID, card);
	}

	@Override
	public void apply() {
		if(color != null)
			card.enableOnceARoundEffect(color);
	}
	
	public void setFamiliarColor(FamiliarColor color) {
		this.color = color;
	}
	
	public FamiliarColor getColor() {
		return this.color;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

}
