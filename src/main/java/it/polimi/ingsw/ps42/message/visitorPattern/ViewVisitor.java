package it.polimi.ingsw.ps42.message.visitorPattern;

import it.polimi.ingsw.ps42.message.BanMessage;
import it.polimi.ingsw.ps42.message.BanRequest;
import it.polimi.ingsw.ps42.message.BanUpdateMessage;
import it.polimi.ingsw.ps42.message.BonusBarMessage;
import it.polimi.ingsw.ps42.message.CardUpdateMessage;
import it.polimi.ingsw.ps42.message.CardsMessage;
import it.polimi.ingsw.ps42.message.CouncilRequest;
import it.polimi.ingsw.ps42.message.DiceMessage;
import it.polimi.ingsw.ps42.message.FamiliarUpdateMessage;
import it.polimi.ingsw.ps42.message.LeaderCardMessage;
import it.polimi.ingsw.ps42.message.LeaderCardUpdateMessage;
import it.polimi.ingsw.ps42.message.PlayerMove;
import it.polimi.ingsw.ps42.message.PlayerToken;
import it.polimi.ingsw.ps42.message.RequestInterface;
import it.polimi.ingsw.ps42.message.ResourceUpdateMessage;
import it.polimi.ingsw.ps42.view.View;

public class ViewVisitor implements Visitor {

	private View view;
	
	public ViewVisitor( View view) {
		this.view = view;
	}
	
	@Override
	public void visit(BonusBarMessage message) {
		/*Message forwarded by the Model, the visitor has to 
		 * call a method of the view for asking a BonusBar from
		 * the possible choice (then re-forwarded to the game logic).
		 */
		
	}

	@Override
	public void visit(LeaderCardMessage message) {
		/*Message forwarded by the Model, the visitor has to 
		 * call a method of the view for asking a LeaderCard from
		 * the possible choice (then re-forwarded to the game logic).
		 */
		
	}

	@Override
	public void visit(BanMessage message) {
		/*Message forwarded by the Model at the start of the game, the visitor 
		 * has to call a method of the View for setting the new Bans.
		 */
		if(message != null){	
			this.view.setFirstBan(message.getFirstEffect());
			this.view.setSecondBan(message.getSecondEffect());
			this.view.setThirdBan(message.getThirdEffect());
		}
		
	}

	@Override
	public void visit(DiceMessage message) {
		/*Message forwarded by the Model every round, the visitor 
		 * has to call a method of the View for setting the dice value to every familiars.
		 */

		if(message != null){	
			this.view.setFirstBan(message.getFirstEffect());
			this.view.setSecondBan(message.getSecondEffect());
			this.view.setThirdBan(message.getThirdEffect());
		}
	}

	@Override
	public void visit(CardsMessage message) {
		/*Message forwarded by the Model every round, the visitor has to
		 * call a method of the View for setting the Cards received on the proper Tower.
		 */
		
	}

	@Override
	public void visit(ResourceUpdateMessage message) {
		/*Message forwarded by the Model every time a call to the syncResources method in 
		 * player is made, the visitor has to call a method of the View that update the 
		 * resources of that player.
		 */
		
	}

	@Override
	public void visit(FamiliarUpdateMessage message) {
		/*Message forwarded by the Model every time a Familiar is moved by a
		 * player, the visitor has to call a method of the View that update the 
		 * familiar's position of that player.
		 */
		
	}

	@Override
	public void visit(CardUpdateMessage message) {
		/*Message forwarded by the Model every time a Card is taken by a
		 * player, the visitor has to call a method of the View that update the 
		 * cards of that Player and the state of the Table.
		 */
		
	}

	@Override
	public void visit(BanUpdateMessage message) {
		/*Message forwarded by the Model at the end of a Period if the player
		 * do not has the required faithPoint, the visitor has to call a method of the View that update the 
		 * bans of that player.
		 */
		
	}

	@Override
	public void visit(PlayerMove message) {
		/*Message forwarded by the Model every time a player has to perform a move, 
		 * the visitor has to call a method of the View that ask a move to that player.
		 */
		
	}

	@Override
	public void visit(RequestInterface message) {
		/*Message forwarded by the Model every time a player has some Requests, 
		 * the visitor has to call a method of the View that ask to that player a choice.
		 */
		
	}

	@Override
	public void visit(CouncilRequest message) {
		/*Message forwarded by the Model every time a player has some Council Requests, 
		 * the visitor has to call a method of the View that ask to that player a choice.
		 */		
		
	}

	@Override
	public void visit(PlayerToken message) {
		/*Message forwarded by the Model every time a player has to perform a move, 
		 * the visitor has to call a method of the View that ask a move to that player.
		 */		
		
	}

	@Override
	public void visit(LeaderCardUpdateMessage message) {
		/*	
		 * Set the card to the enabledLeaderCard player's array
		 */
	}

	@Override
	public void visit(BanRequest message) {
		/*
		 * Ask to the player if he wants pay for the ban or not
		 */		
	}


}
