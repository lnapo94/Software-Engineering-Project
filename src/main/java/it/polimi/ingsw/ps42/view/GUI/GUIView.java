package it.polimi.ingsw.ps42.view.GUI;

import java.util.List;

import it.polimi.ingsw.ps42.message.CardRequest;
import it.polimi.ingsw.ps42.message.PlayerMove;
import it.polimi.ingsw.ps42.message.leaderRequest.LeaderFamiliarRequest;
import it.polimi.ingsw.ps42.model.action.ActionPrototype;
import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.leaderCard.LeaderCard;
import it.polimi.ingsw.ps42.model.player.BonusBar;
import it.polimi.ingsw.ps42.view.View;

public class GUIView extends View {

	@Override
	protected int chooseBonusBar(List<BonusBar> bonusBarList) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int chooseLeaderCard(List<LeaderCard> leaderCardList) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int chooseCouncilConversion(List<Obtain> possibleConversions) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected PlayerMove choosePlayerMove(ActionPrototype prototype) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean chooseIfPayBan(int banPeriod) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected int answerCardRequest(CardRequest message) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected FamiliarColor chooseFamiliarColor(LeaderFamiliarRequest message) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void notifyLeaderCardActivation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void notifyLeaderCardDiscard() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String askPlayerID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String askIfWantToPlay() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void showResult(List<String> finalChart) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		Window window = new Window();
		window.run();
	}

}
