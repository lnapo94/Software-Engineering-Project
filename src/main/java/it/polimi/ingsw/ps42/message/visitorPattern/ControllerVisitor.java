package it.polimi.ingsw.ps42.message.visitorPattern;

import it.polimi.ingsw.ps42.controller.GameLogic;
import it.polimi.ingsw.ps42.message.BanMessage;
import it.polimi.ingsw.ps42.message.BanRequest;
import it.polimi.ingsw.ps42.message.BanUpdateMessage;
import it.polimi.ingsw.ps42.message.BonusBarMessage;
import it.polimi.ingsw.ps42.message.CardRequest;
import it.polimi.ingsw.ps42.message.CardUpdateMessage;
import it.polimi.ingsw.ps42.message.CardsMessage;
import it.polimi.ingsw.ps42.message.CouncilRequest;
import it.polimi.ingsw.ps42.message.DiceMessage;
import it.polimi.ingsw.ps42.message.FamiliarUpdateMessage;
import it.polimi.ingsw.ps42.message.LeaderCardMessage;
import it.polimi.ingsw.ps42.message.LeaderCardUpdateMessage;
import it.polimi.ingsw.ps42.message.PlayerMove;
import it.polimi.ingsw.ps42.message.PlayerToken;
import it.polimi.ingsw.ps42.message.ResourceUpdateMessage;

public class ControllerVisitor implements Visitor {

	private GameLogic gameLogic;
	
	public ControllerVisitor(GameLogic gameLogic){
		
		this.gameLogic = gameLogic;
	}
	
	//TO-DO: aggiungere un messaggio per la scelta sulle scomuniche(aggiungere ask in player)
	
	@Override
	public void visit(BonusBarMessage message) {
		/* Response message from the View about a BonusBar choice.
		 * Give to the Game Logic the BonusBar chosen and the related player
		 */
		
		
		
	}

	@Override
	public void visit(LeaderCardMessage message) {
		/* Response Message from the View about the Leader Card choice.
		 * Give to the Game Logic the Leader Card chosen and the related player
		 */
		
	}

	@Override
	public void visit(BanMessage message) {
		/* Nothing to do (this message is only received by the View)
		 */
		
	}

	@Override
	public void visit(DiceMessage message) {
		/*Nothing to do (this message is only received by the View)
		 */
		
	}

	@Override
	public void visit(CardsMessage message) {
		/*Nothing to do (this message is only received by the View)
		 */
		
	}

	@Override
	public void visit(ResourceUpdateMessage message) {
		/*Nothing to do (this message is only received by the View)
		 */
		
	}

	@Override
	public void visit(FamiliarUpdateMessage message) {
		/*Nothing to do (this message is only received by the View)
		 */
		
	}

	@Override
	public void visit(CardUpdateMessage message) {
		/*Nothing to do (this message is only received by the View)
		 */
		
	}

	@Override
	public void visit(BanUpdateMessage message) {
		/*Nothing to do (this message is only received by the View)
		 */
		
	}

	@Override
	public void visit(PlayerMove message) {
		/*Response Message from the View about the Player move.
		 * Build the correct Action and send it to the Game Logic with a method
		 */
		
	}

	@Override
	public void visit(CardRequest message) {
		/*Response Message by the View to a specific request. 
		 * Send with a method to the GameLogic
		 */
		
		
	}

	@Override
	public void visit(CouncilRequest message) {
		/*Response Message by the View to a Council request.
		 * Send to the Game Logic with a method
		 */
		
	}

	@Override
	public void visit(PlayerToken message) {
		/*Nothing to do (this message is only received by the View)
		 */
		
	}

	@Override
	public void visit(LeaderCardUpdateMessage message) {
		/*
		 * Someone requires to enable a specific card
		 * Check if the card can be activated, if so move the card in the correct
		 * arraylist in player and create a message
		 */
	}

	@Override
	public void visit(BanRequest message) {
		/*
		 * Enable the chosen ban if the variable is set to false,
		 * else reduce the faith point and assign the victory point to player 
		 */
	}


}
