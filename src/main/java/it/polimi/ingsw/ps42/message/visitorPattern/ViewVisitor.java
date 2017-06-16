package it.polimi.ingsw.ps42.message.visitorPattern;


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
import it.polimi.ingsw.ps42.message.leaderRequest.LeaderFamiliarRequest;
import it.polimi.ingsw.ps42.view.View;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.exception.ElementNotFoundException;
import it.polimi.ingsw.ps42.model.exception.WrongChoiceException;


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
		
		if(message != null ){
			
			try {
				this.view.askBonusBar( message);
			} catch (WrongChoiceException e) {
				System.out.println("Error in Viewvisitor BonusBarMessage");
			}
		}
	}

	@Override
	public void visit(LeaderCardMessage message) {
		/*Message forwarded by the Model, the visitor has to 
		 * call a method of the view for asking a LeaderCard from
		 * the possible choice (then re-forwarded to the game logic).
		 */
		if(message != null)
			try{
				this.view.askLeaderCard(message);
			}
		catch(WrongChoiceException e){
			System.out.println("Error in Viewvisitor LeaderCardMessage");
		}
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
			this.view.setBlackDie(message.getBlackDie());
			this.view.setWhiteDie(message.getWhiteDie());
			this.view.setOrangeDie(message.getOrangeDie());
		}
	}

	@Override
	public void visit(CardsMessage message) {
		/*Message forwarded by the Model every round, the visitor has to
		 * call a method of the View for setting the Cards received on the proper Tower.
		 */
		if(message != null){
			switch(message.getColor()){
				case GREEN:
					this.view.setGreenCards(message.getDeck());
					break;
				case BLUE:
					this.view.setBlueCards(message.getDeck());
					break;
				case YELLOW:
					this.view.setYellowCards(message.getDeck());
					break;
				case VIOLET:
					this.view.setVioletCards(message.getDeck());
					break;
				default:
					System.out.println("[DEBUG] wrong message from GameLogic");
			}
			
		}
		
	}

	@Override
	public void visit(ResourceUpdateMessage message) {
		/*Message forwarded by the Model every time a call to the syncResources method in 
		 * player is made, the visitor has to call a method of the View that update the 
		 * resources of that player.
		 */
		if(message != null){
			
			this.view.setResources(message.getResources(), message.getPlayerID());
		}
			
		
	}

	@Override
	public void visit(FamiliarUpdateMessage message) {
		/*Message forwarded by the Model every time a Familiar is moved by a
		 * player, the visitor has to call a method of the View that update the 
		 * familiar's position of that player.
		 */
		if( message != null){
			String playerID = message.getPlayerID();
			int position = message.getPosition();
			FamiliarColor color = message.getColor();
			try{
				switch(message.getAction()){
				 	case TAKE_BLUE:
				 		this.view.setFamiliarInBlueTower(playerID, color, position);
				 		break;
				 	case TAKE_GREEN:
				 		this.view.setFamiliarInGreenTower(playerID, color, position);
				 		break;
				 	case TAKE_VIOLET:
				 		this.view.setFamiliarInVioletTower(playerID, color, position);
				 		break;
				 	case TAKE_YELLOW:
				 		this.view.setFamiliarInYellowTower(playerID, color, position);
				 		break;
				 	case COUNCIL:
				 		this.view.setFamiliarInCouncil(playerID, color);
				 		break;
				 	case MARKET:
				 		this.view.setFamiliarInMarket(playerID, color, position);
				 		break;
				 	case YIELD:
				 		this.view.setFamiliarInYield(playerID, color, position);
				 		break;
				 	case PRODUCE:
				 		this.view.setFamiliarInProduce(playerID, color, position);
				 		break;
				 	default:
						System.out.println("[DEBUG] wrong message from GameLogic");
				}	
			}
			catch( ElementNotFoundException e){
				System.out.println("Error in Viewvisitor FamiliarUpdateMessage");
			}
			
		}
		
	}

	@Override
	public void visit(CardUpdateMessage message) {
		/*Message forwarded by the Model every time a Card is taken by a
		 * player, the visitor has to call a method of the View that update the 
		 * cards of that Player and the state of the Table.
		 */
		if(message != null){
			String playerID = message.getPlayerID();
			int position = message.getPosition();
			try{
				switch(message.getType()){
				case TAKE_BLUE:
					this.view.setBlueCard(playerID, position);
					break;
				case TAKE_GREEN:
					this.view.setGreenCard(playerID, position);
					break;
				case TAKE_VIOLET:
					this.view.setVioletCard(playerID, position);
					break;
				case TAKE_YELLOW:
					this.view.setYellowCard(playerID, position);
					break;
				default:
					System.out.println("[DEBUG] wrong message from GameLogic");
				}	
			}
			catch(ElementNotFoundException e){
				System.out.println("Error in Viewvisitor CardUpdateMessage");
			}
		}
		
	}

	@Override
	public void visit(BanUpdateMessage message) {
		/*Message forwarded by the Model at the end of a Period if the player
		 * do not has the required faithPoint, the visitor has to call a method of the View that update the 
		 * bans of that player.
		 */
		if(message != null){
			try {
				this.view.setBanToPlayer( message.getPlayerID(), message.getBan());
			} catch (ElementNotFoundException e) {
				System.out.println("Error in Viewvisitor BanUpdateMessage");
			}
		}
		
	}

	@Override
	public void visit(CouncilRequest message) {
		/*Message forwarded by the Model every time a player has some Council Requests, 
		 * the visitor has to call a method of the View that ask to that player a choice.
		 */		
		if( message != null)
			try {
				this.view.askCouncilRequest(message);
			} catch (WrongChoiceException e) {
				System.out.println("Error in Viewvisitor CouncilRequest");
			}
		
	}

	@Override
	public void visit(PlayerToken message) {
		/*Message forwarded by the Model every time a player has to perform a move, 
		 * the visitor has to call a method of the View that ask a move to that player.
		 */		
		if(message != null)
			this.view.askPlayerMove(message);
	}

	@Override
	public void visit(LeaderCardUpdateMessage message) {
		/*	
		 * Set the card to the enabledLeaderCard player's array
		 */
		if(message != null){
			try {
				this.view.setEnabledLeaderCard(message.getPlayerID(), message.getCard());
			} catch (ElementNotFoundException e) {
				System.out.println("Error in Viewvisitor LeaderCardUpdateMessage");
			}
		}
			
		
	}

	@Override
	public void visit(BanRequest message) {
		/*
		 * Ask to the player if he wants pay for the ban or not
		 */		
		if( message != null)
			this.view.askPayBan(message);
	}

	@Override
	public void visit(CardRequest message) {
		/*
		 * Ask to the Player to answer the Request		
		 */
		
		if( message != null){
			this.view.askCardRequest( message);
		}
	}

	@Override
	public void visit(PlayerMove message) {
		//Nothings to do since this Message is never received
		
	}

	@Override
	public void visit(LeaderFamiliarRequest message) {
		// TODO Auto-generated method stub
		
	}


}
