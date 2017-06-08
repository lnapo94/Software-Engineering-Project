package it.polimi.ingsw.ps42.message.visitorPattern;

import it.polimi.ingsw.ps42.message.BanMessage;
import it.polimi.ingsw.ps42.message.BanUpdateMessage;
import it.polimi.ingsw.ps42.message.BonusBarMessage;
import it.polimi.ingsw.ps42.message.CardUpdateMessage;
import it.polimi.ingsw.ps42.message.CardsMessage;
import it.polimi.ingsw.ps42.message.CouncilRequest;
import it.polimi.ingsw.ps42.message.DiceMessage;
import it.polimi.ingsw.ps42.message.FamiliarUpdateMessage;
import it.polimi.ingsw.ps42.message.LeaderCardMessage;
import it.polimi.ingsw.ps42.message.PlayerMove;
import it.polimi.ingsw.ps42.message.RequestInterface;
import it.polimi.ingsw.ps42.message.ResourceUpdateMessage;

public class ViewVisitor implements Visitor {

	@Override
	public void visit(BonusBarMessage message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(LeaderCardMessage message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(BanMessage message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DiceMessage message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(CardsMessage message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ResourceUpdateMessage message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(FamiliarUpdateMessage message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(CardUpdateMessage message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(BanUpdateMessage message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(PlayerMove message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(RequestInterface message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(CouncilRequest message) {
		// TODO Auto-generated method stub
		
	}


}
