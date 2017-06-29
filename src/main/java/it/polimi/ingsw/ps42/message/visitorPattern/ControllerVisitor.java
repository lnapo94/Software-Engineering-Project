
package it.polimi.ingsw.ps42.message.visitorPattern;

import org.apache.log4j.Logger;

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
import it.polimi.ingsw.ps42.message.EmptyMove;
import it.polimi.ingsw.ps42.message.FamiliarUpdateMessage;
import it.polimi.ingsw.ps42.message.LeaderCardMessage;
import it.polimi.ingsw.ps42.message.LeaderCardUpdateMessage;
import it.polimi.ingsw.ps42.message.PlayerMove;
import it.polimi.ingsw.ps42.message.PlayerToken;
import it.polimi.ingsw.ps42.message.ResourceUpdateMessage;
import it.polimi.ingsw.ps42.message.WinnerMessage;
import it.polimi.ingsw.ps42.message.leaderRequest.LeaderFamiliarRequest;
import it.polimi.ingsw.ps42.model.action.Action;
import it.polimi.ingsw.ps42.model.action.ActionCreator;
import it.polimi.ingsw.ps42.model.exception.ElementNotFoundException;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.player.Player;

/**
 * Class that implements all the visitor methods used in Controller (a.k.a. GameLogic)
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class ControllerVisitor implements Visitor {

	private GameLogic gameLogic;
	
	//Logger
	private transient Logger logger = Logger.getLogger(ControllerVisitor.class);
	
	/**
	 * Constructor of the visitor, it needs the GameLogic for some operations
	 * @param gameLogic
	 */
	public ControllerVisitor(GameLogic gameLogic){
		
		this.gameLogic = gameLogic;
	}
	
	/**
	 * Response message from the View about a BonusBar choice.
	 * Give to the Game Logic the BonusBar chosen and the related player
	 */
	@Override
	public void visit(BonusBarMessage message) {
		if(gameLogic.isInitGame())
			gameLogic.setBonusBar(message.getChoice(), message.getPlayerID());

	}

	/**
	 * Response Message from the View about the Leader Card choice.
	 * Give to the Game Logic the Leader Card chosen and the related player
	 */
	@Override
	public void visit(LeaderCardMessage message) {
		
		if(gameLogic.isInitGame())
			gameLogic.setLeaderCard(message.getChoice(), message.getPlayerID());

	}

	/** 
	 * Nothing to do (this message is only received by the View)
	 */
	@Override
	public void visit(BanMessage message) {
		
		
	}
	
	/** 
	 * Nothing to do (this message is only received by the View)
	 */
	@Override
	public void visit(DiceMessage message) {
		
	}

	/** 
	 * Nothing to do (this message is only received by the View)
	 */
	@Override
	public void visit(CardsMessage message) {
		
	}

	/** 
	 * Nothing to do (this message is only received by the View)
	 */
	@Override
	public void visit(ResourceUpdateMessage message) {

	}

	/** 
	 * Nothing to do (this message is only received by the View)
	 */
	@Override
	public void visit(FamiliarUpdateMessage message) {

	}

	/** 
	 * Nothing to do (this message is only received by the View)
	 */
	@Override
	public void visit(CardUpdateMessage message) {

	}

	/** 
	 * Nothing to do (this message is only received by the View)
	 */
	@Override
	public void visit(BanUpdateMessage message) {

	}
	
	/**
	 * Response Message from the View about the Player move.
	 * Build the correct Action and send it to the Game Logic with a method
	 */
	@Override
	public void visit(PlayerMove message) {

		try {
			Player player = gameLogic.searchPlayer(message.getPlayerID());
			if(gameLogic.isConnected(player)) {
				Action action = new ActionCreator(player, gameLogic.getTable(), message, gameLogic.getBonusActionValue()).getCreatedAction();
				gameLogic.handleAction(action, message.getPlayerID());
			}
		} catch (NotEnoughResourcesException e) {
			
			try {
				//Retrasmit the message because player has not enough resources
				PlayerToken newMessage = new PlayerToken(message.getPlayerID());
				newMessage.setRetrasmission();
				gameLogic.searchPlayer(message.getPlayerID()).retrasmitMessage(newMessage);				
				
			} catch (ElementNotFoundException e1) {
				logger.fatal("Player not found in gameLogic");
				logger.info(e1);
			}
			
		} catch (ElementNotFoundException e) {
			logger.fatal("Player not found in gameLogic");
			logger.info(e);
		}
	}

	/**
	 * Response Message by the View to a specific request. 
	 * Send with a method to the GameLogic
	 */
	@Override
	public void visit(CardRequest message) {

		try {
			if(gameLogic.isConnected(gameLogic.searchPlayer(message.getPlayerID())))
				gameLogic.handleRequest(message);
		} catch (ElementNotFoundException e) {
			logger.fatal("Player not found in gameLogic");
			logger.info(e);
		}
	}

	/**
	 * Response Message by the View to a Council request.
	 * Send to the Game Logic with a method
	 */
	@Override
	public void visit(CouncilRequest message) {

		try {
			if(gameLogic.isConnected(gameLogic.searchPlayer(message.getPlayerID())))
				gameLogic.handleCouncilRequest(message);
		} catch (ElementNotFoundException e) {
			logger.fatal("Player not found in gameLogic");
			logger.info(e);
		}
		
	}

	/** 
	 * Nothing to do (this message is only received by the View)
	 */
	@Override
	public void visit(PlayerToken message) {
		
	}

	/**
	 * Someone requires to enable a specific card
	 * Check if the card can be activated, if so move the card in the correct
	 * arraylist in player and create a message
	 */
	@Override
	public void visit(LeaderCardUpdateMessage message) {

		try {
			if(gameLogic.isConnected(gameLogic.searchPlayer(message.getPlayerID())))
				gameLogic.HandleLeaderUpdate(gameLogic.searchPlayer(message.getPlayerID()), message.getCard());
		} catch (ElementNotFoundException e) {
			logger.fatal("Unable to find the player. Method: leaderCardUpdateMessage");
			logger.info(e);
		}
	}

	/**
	 * Enable the chosen ban if the variable is set to false,
	 * else reduce the faith point and assign the victory point to player 
	 */
	@Override
	public void visit(BanRequest message) {

		try {
			if(gameLogic.isConnected(gameLogic.searchPlayer(message.getPlayerID())))
				gameLogic.handleBan(message.getPlayerID(), message.getBanNumber(), message.wantPayForBan());
		} catch (ElementNotFoundException e) {
			logger.fatal("Player not found in gameLogic");
			logger.info(e);
		}

	}

	/**
	 * Message received by the view, do the apply for the leader card
	 */
	@Override
	public void visit(LeaderFamiliarRequest message) {
		
		try {
			if(gameLogic.isConnected(gameLogic.searchPlayer(message.getPlayerID())))
				gameLogic.handleLeaderFamiliarRequest(message);
		} catch (ElementNotFoundException e) {
			logger.fatal("Player not found in gameLogic");
			logger.info(e);
		}

	}

	/**
	 * If the player doesn't want to play
	 */
	@Override
	public void visit(EmptyMove message) {

		try {
			if(gameLogic.isConnected(gameLogic.searchPlayer(message.getPlayerID())))
				gameLogic.initAction();
		} catch (ElementNotFoundException e) {
			logger.fatal("Player not found in gameLogic");
			logger.info(e);
		}
	}

	/** 
	 * Nothing to do (this message is only received by the View)
	 */
	@Override
	public void visit(WinnerMessage message) {
		
	}


}
