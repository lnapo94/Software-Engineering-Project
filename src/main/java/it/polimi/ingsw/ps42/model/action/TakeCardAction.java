package it.polimi.ingsw.ps42.model.action;


import java.util.List;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.message.CardRequest;
import it.polimi.ingsw.ps42.message.CardUpdateMessage;
import it.polimi.ingsw.ps42.message.FamiliarUpdateMessage;
import it.polimi.ingsw.ps42.message.Message;
import it.polimi.ingsw.ps42.model.StaticList;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.enumeration.Response;
import it.polimi.ingsw.ps42.model.exception.FamiliarInWrongPosition;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.position.TowerPosition;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

/**
 * Class that represent the action to take a card from a tower, both for "normal" actions
 * and "bonus" actions
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class TakeCardAction extends Action{
	
	private StaticList<TowerPosition> tablePosition;
	private int positionInTableList;
	
	//Logger
	private transient Logger logger = Logger.getLogger(TakeCardAction.class);

	/**
	 * Constructor for a normal action
	 * @param type								The type of the action
	 * @param familiar							The familiar the player wants to move
	 * @param tablePosition						The StaticList of market position in table
	 * @param positionInTableList				The exact index of the chosen position
	 * @throws NotEnoughResourcesException		Thrown if the player hasn't enough resources
	 */
	public TakeCardAction(ActionType type, Familiar familiar, StaticList<TowerPosition> tablePosition, int positionInTableList) throws NotEnoughResourcesException{
		//Constructor for normal action
		super(type, familiar);
		this.tablePosition = tablePosition;
		this.positionInTableList = positionInTableList;
	}
	
	/**
	 * Constructor for bonus action 
	 * @param type								The type of the action
	 * @param player							The interested player
	 * @param tablePosition						The StaticList of market position in table
	 * @param positionInTableList				The exact index of the chosen position
	 * @param actionValue						The value of this bonus action
	 * @param actionIncrement					The increment of this bonus action
	 * @throws NotEnoughResourcesException		Thrown if the player hasn't enough resources
	 */
	public TakeCardAction(ActionType type, Player player, StaticList<TowerPosition> tablePosition, 
			int positionInTableList, int actionValue, int actionIncrement) throws NotEnoughResourcesException{
		//Constructor for bonus action
		super(type, player, actionValue, actionIncrement);
		this.tablePosition = tablePosition;
		this.positionInTableList = positionInTableList;
	}
	
	/**
	 * Method used to check if this action can be applied. If the card as more cost, this method
	 * create a PayRequest
	 */
	@Override
	public Response checkAction() {
		//Initial checks for the takeCard action, valid for both normal and bonus action
		
		//First: Check if the player can play
		if(!player.canPlay())
			return Response.CANNOT_PLAY;
		
		//Second: Active the IncreaseEffect in player, control the ban of the tower bonus position
		checkIncreaseEffect();
		
		//Third: Check if the position is free, if there aren't other familiar with
		//the same player, if there isn't the card, if the familiar can't stay in that position
		//and if the player has the requirements of the chosen card
		//Be careful to the neutral familiar
		
		//Take the chosen position
		TowerPosition position = tablePosition.get(positionInTableList);
		
		if(!position.isEmpty()) {
			player.restoreResource();
			return Response.FAILURE;
		}
		
		if(position.getFamiliar() != null && !checkMyFamiliar()) {
			player.restoreResource();
			return Response.FAILURE;
		}
		
		actionValue = actionValue - position.getMalus();
		
		if(position.getLevel() > actionValue) {
			player.restoreResource();
			return Response.FAILURE;
		}
		
		if(!position.hasCard()) {
			player.restoreResource();
			return Response.FAILURE;
		}
		
		//Control how much green cards the player has in his list
		if(this.getType() == ActionType.TAKE_GREEN && !player.hasNoMilitaryRequirements()) {
			int militaryPointQuantity = player.getResource(Resource.MILITARYPOINT);
			int greenCardsInPlayer = player.getCardList(CardColor.GREEN).size();
			
			//Control if player can have enough military point
			if(militaryPointQuantity < 3 && greenCardsInPlayer == 2)
				return Response.FAILURE;
			
			if(militaryPointQuantity < 7 && greenCardsInPlayer == 3)
				return Response.FAILURE;
			
			if(militaryPointQuantity < 12 && greenCardsInPlayer == 4)
				return Response.FAILURE;
			
			if(militaryPointQuantity < 18 && greenCardsInPlayer == 5)
				return Response.FAILURE;
		}
		
		//Fourth: if the position has a bonus, apply it to the player
		if(familiar != null) {
			
			if(familiar.isPositioned()) {
				//If familiar is positioned yet
				player.restoreResource();
				return Response.FAILURE;
			}
			
			try {
				position.setFamiliar(familiar);
				player.synchResource();
			} catch (FamiliarInWrongPosition e) {
				logger.debug("familiar can't be positioned here");
				logger.info(e);
				familiar.setPosition(null);
				player.restoreResource();
				return Response.FAILURE;
			}
		}
		
		//Create a moneyMalus packet
		Packet moneyMalus = new Packet();
		moneyMalus.addUnit(new Unit(Resource.MONEY, 3));
		
		//Fifth: verify if there aren't any other player in the tower, else
		//decrease money in player
		if(!player.hasNoMoneyBonus()) {
			if(isAnotherFamiliar()) {
				try {
					player.decreaseResource(moneyMalus);
					player.synchResource();
				} catch (NotEnoughResourcesException e) {
					logger.info("Player has not enough resources to pay the 3 money, stop.");
					logger.info(e);
					if(position.getBonus() != null)
						position.resetBonus(player);
					if(familiar != null)
						familiar.setPosition(null);
					player.synchResource();
					position.removeFamiliar();
					return Response.LOW_LEVEL;
				}
			}
		}
		
		
		try {
			position.getCard().payCard(player, discount);
		} catch (NotEnoughResourcesException e) {
			logger.info("Player has not enough resources to pay the card, stop.");
			logger.info(e);
			rollBackAction();
			return Response.LOW_LEVEL;
		}
		return Response.SUCCESS;
	}
	
	/**
	 * Method used to undo a TakeCardAction
	 */
	@Override
	public void rollBackAction() {
		super.rollBackAction();
		//Called when player doesn't answer to gameLogic
		TowerPosition position = tablePosition.get(positionInTableList);
		Packet moneyMalus = new Packet();
		moneyMalus.addUnit(new Unit(Resource.MONEY, 3));
		
		if(position.getBonus() != null)
			position.resetBonus(player);
		if(isAnotherFamiliar())
			player.increaseResource(moneyMalus);
		if(familiar != null)
			familiar.setPosition(null);
		player.synchResource();
		position.removeFamiliar();
	}

	/**
	 * Method used to apply the action. If there were some PayRequest, apply it
	 */
	@Override
	public void doAction() {
		/*	Method used to apply an Action
		 * 	In this case, control if there is a request in player and apply it
		 * 	Then do some operation for setting the card to the player 
		 * 	Finally, synch all the player resources to apply all the changes 
		 */
		
		List<CardRequest> requests = player.getRequests();
		if(requests != null && !requests.isEmpty()) {
			for(CardRequest request : requests)
				request.apply(player);
		}
		
		TowerPosition position = tablePosition.get(positionInTableList);
		position.getCard().setPlayer(player);
		player.addCard(position.getCard());
		player.synchResource();
		try {
			position.getCard().enableImmediateEffect();
			if(position.getCard().getColor() == CardColor.BLUE || position.getCard().getColor() == CardColor.VIOLET)
				position.getCard().enablePermanentEffect();
		} catch (NotEnoughResourcesException e) {
			logger.debug("Player can not pay for enable the immediate effect");
			logger.info(e);
		}
		position.removeCard();
		
		//Notify the view
		if(familiar != null) {
			Message familiarUpdate = new FamiliarUpdateMessage(player.getPlayerID(), familiar.getColor(), getType(), positionInTableList);
			setChanged();
			notifyObservers(familiarUpdate);
		}
		Message cardUpdate = new CardUpdateMessage(player.getPlayerID(), getType(), positionInTableList);
		setChanged();
		notifyObservers(cardUpdate);
	}

	/**
	 * Private method used to check the familiar of the player, because the player
	 * can position only one of his familiars in the tower, apart the neutral familiar
	 * @return	True if player can position his familiar in the specified position, otherwise False
	 */
	private boolean checkMyFamiliar() {
		for(TowerPosition tower : tablePosition) {
			if(tower.getFamiliar().getPlayer() == player) {
				if(familiar.getColor() != FamiliarColor.NEUTRAL)
					return false;
			}
		}
		return true;
	}
	
	/**
	 * Method used to verify if the tower is occupied yet
	 * @return True if there is at least another familiar in tower (Not of the current player), otherwise False
	 */
	private boolean isAnotherFamiliar() {
		for(TowerPosition tower : tablePosition) {
			if(!tower.isEmpty() && tower.getFamiliar().getPlayer() != player) {
				return true;
			}
		}
		return false;
	}
}
