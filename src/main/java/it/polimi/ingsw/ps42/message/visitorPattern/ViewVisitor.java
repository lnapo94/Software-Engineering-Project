package it.polimi.ingsw.ps42.message.visitorPattern;


import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.message.BanMessage;
import it.polimi.ingsw.ps42.message.BanRequest;
import it.polimi.ingsw.ps42.message.BanUpdateMessage;
import it.polimi.ingsw.ps42.message.BonusBarMessage;
import it.polimi.ingsw.ps42.message.CancelCardRequest;
import it.polimi.ingsw.ps42.message.CardRequest;
import it.polimi.ingsw.ps42.message.CardUpdateMessage;
import it.polimi.ingsw.ps42.message.CardsMessage;
import it.polimi.ingsw.ps42.message.CouncilRequest;
import it.polimi.ingsw.ps42.message.DiceMessage;
import it.polimi.ingsw.ps42.message.DiscardLeaderCard;
import it.polimi.ingsw.ps42.message.EmptyMove;
import it.polimi.ingsw.ps42.message.FamiliarUpdateMessage;
import it.polimi.ingsw.ps42.message.LeaderCardMessage;
import it.polimi.ingsw.ps42.message.LeaderCardUpdateMessage;
import it.polimi.ingsw.ps42.message.PlayerMove;
import it.polimi.ingsw.ps42.message.PlayerToken;
import it.polimi.ingsw.ps42.message.ReconnectMessage;
import it.polimi.ingsw.ps42.message.ResourceUpdateMessage;
import it.polimi.ingsw.ps42.message.WinnerMessage;
import it.polimi.ingsw.ps42.message.leaderRequest.LeaderFamiliarRequest;
import it.polimi.ingsw.ps42.view.View;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.exception.ElementNotFoundException;

/**
 * Class that implements all the method used by a visitor in the view
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class ViewVisitor implements Visitor {

	private View view;
	
	//Logger
	private transient Logger logger = Logger.getLogger(ControllerVisitor.class);
	
	public ViewVisitor( View view) {
		this.view = view;
	}
	
	/** 
	 * Message forwarded by the Model, the visitor has to 
	 * call a method of the view for asking a BonusBar from
	 * the possible choice (then re-forwarded to the game logic).
	 */
	@Override
	public void visit(BonusBarMessage message) {
		
		if(message != null ){	
			this.view.askBonusBar( message);
		}
	}

	/**
	 * Message forwarded by the Model, the visitor has to 
	 * call a method of the view for asking a LeaderCard from
	 * the possible choice (then re-forwarded to the game logic).
	 */
	@Override
	public void visit(LeaderCardMessage message) {
		
		if(message != null)
			this.view.askLeaderCard(message);

	}

	/**
	 * Message forwarded by the Model at the start of the game, the visitor 
	 * has to call a method of the View for setting the new Bans.
	 */
	@Override
	public void visit(BanMessage message) {
		
		if(message != null){	
			this.view.setGameBans(message);
		}
		
	}

	/**
	 * Message forwarded by the Model every round, the visitor 
	 * has to call a method of the View for setting the dice value to every familiars.
	 */
	@Override
	public void visit(DiceMessage message) {
		
		view.resetTable();
		
		if(message != null){	
			this.view.setBlackDie(message.getBlackDie());
			this.view.setWhiteDie(message.getWhiteDie());
			this.view.setOrangeDie(message.getOrangeDie());
		}
	}

	/**
	 * Message forwarded by the Model every round, the visitor has to
	 * call a method of the View for setting the Cards received on the proper Tower.
	 */
	@Override
	public void visit(CardsMessage message) {

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
					logger.debug("wrong message from GameLogic");
			}
			
		}
		
	}
	
	/**
	 * Message forwarded by the Model every time a call to the syncResources method in 
	 * player is made, the visitor has to call a method of the View that update the 
	 * resources of that player.
	 */
	@Override
	public void visit(ResourceUpdateMessage message) {

		if(message != null){
			
			this.view.setResources(message.getResources(), message.getPlayerID());
		}
			
		
	}
	
	/**
	 * Message forwarded by the Model every time a Familiar is moved by a
	 * player, the visitor has to call a method of the View that update the 
	 * familiar's position of that player.
	 */
	@Override
	public void visit(FamiliarUpdateMessage message) {

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
						logger.debug("[DEBUG] wrong message from GameLogic");
				}	
			}
			catch( ElementNotFoundException e){
				logger.error("Error in Viewvisitor FamiliarUpdateMessage");
				logger.info(e);
			}
			
		}
		
	}
	
	/**
	 * Message forwarded by the Model every time a Card is taken by a
	 * player, the visitor has to call a method of the View that update the 
	 * cards of that Player and the state of the Table.
	 */
	@Override
	public void visit(CardUpdateMessage message) {
		
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
					logger.debug("[DEBUG] wrong message from GameLogic");
				}	
			}
			catch(ElementNotFoundException e){
				logger.error("Error in Viewvisitor CardUpdateMessage");
				logger.info(e);
			}
		}
		
	}
	
	/**
	 * Message forwarded by the Model at the end of a Period if the player
	 * do not has the required faithPoint, the visitor has to call a method of the View that update the 
	 * bans of that player.
	 */
	@Override
	public void visit(BanUpdateMessage message) {

		if(message != null){
			try {
				this.view.setBanToPlayer( message.getPlayerID(), message.getBan());
			} catch (ElementNotFoundException e) {
				logger.error("Error in Viewvisitor BanUpdateMessage");
				logger.info(e);
			}
		}
		
	}
	
	/**
	 * Message forwarded by the Model every time a player has some Council Requests, 
	 * the visitor has to call a method of the View that ask to that player a choice.
	 */
	@Override
	public void visit(CouncilRequest message) {
			
		if( message != null)
				this.view.askCouncilRequest(message);
		
	}
	
	/**
	 * Message forwarded by the Model every time a player has to perform a move, 
	 * the visitor has to call a method of the View that ask a move to that player.
	 */	
	@Override
	public void visit(PlayerToken message) {
	
		if(message != null)
			this.view.askPlayerMove(message);
	}

	/**	
	 * Set the card to the enabledLeaderCard player's array
	 */
	@Override
	public void visit(LeaderCardUpdateMessage message) {

		if(message != null){
			try {
				this.view.setEnabledLeaderCard(message.getPlayerID(), message.getCard());
			} catch (ElementNotFoundException e) {
				logger.error("Error in Viewvisitor LeaderCardUpdateMessage");
				logger.info(e);
			}
		}
			
		
	}

	/**
	 * Ask to the player if he wants pay for the ban or not
	 */	
	@Override
	public void visit(BanRequest message) {
	
		if( message != null)
			this.view.askPayBan(message);
	}
	
	/**
	 * Ask to the Player to answer the Request		
	 */
	@Override
	public void visit(CardRequest message) {

		if( message != null){
			this.view.askCardRequest( message);
		}
	}

	/**
	 * Nothings to do since this Message is never received
	 */
	@Override
	public void visit(PlayerMove message) {
		
	}

	/**
	 * Ask to the player to choose 
	 */
	@Override
	public void visit(LeaderFamiliarRequest message) {

		if(message != null){
			this.view.handleLeaderFamiliarRequest(message);
		}
		
	}
	
	/**
	 * No things to do since this message is only sent to the Game Logic
	 */
	@Override
	public void visit(EmptyMove message) {

	}

	/**
	 * Show to the Player the final rank
	 */
	@Override
	public void visit(WinnerMessage message) {

		if(message != null){
			view.handleResult(message);
		}
		
	}

	/**
	 * No things to do since this message is only sent to the Game Logic
	 */
	@Override
	public void visit(CancelCardRequest message) {

	}

	/**
	 * Call a view method to discard a leader card
	 */
	@Override
	public void visit(DiscardLeaderCard message) {
		view.discardLeaderCard(message.getPlayerID(), message.getCard());
	}

	/**
	 * Reconnect the disconnected player and restore its previous state (cards, leader cards, bans..)
	 */
	@Override
	public void visit(ReconnectMessage message) {
		if(message != null)
			view.handleReconnect(message);
	}


}
