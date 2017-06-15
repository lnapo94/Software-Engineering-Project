package it.polimi.ingsw.ps42.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import it.polimi.ingsw.ps42.controller.cardCreator.CardsCreator;
import it.polimi.ingsw.ps42.controller.cardCreator.CardsFirstPeriod;
import it.polimi.ingsw.ps42.message.BanUpdateMessage;
import it.polimi.ingsw.ps42.message.CardRequest;
import it.polimi.ingsw.ps42.message.CouncilRequest;
import it.polimi.ingsw.ps42.message.LeaderRequest;
import it.polimi.ingsw.ps42.message.Message;
import it.polimi.ingsw.ps42.message.PlayerToken;
import it.polimi.ingsw.ps42.message.visitorPattern.ControllerVisitor;
import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.StaticList;
import it.polimi.ingsw.ps42.model.Table;
import it.polimi.ingsw.ps42.model.action.Action;
import it.polimi.ingsw.ps42.model.action.ActionPrototype;
import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.enumeration.Response;
import it.polimi.ingsw.ps42.model.exception.ElementNotFoundException;
import it.polimi.ingsw.ps42.model.exception.FamiliarInWrongPosition;
import it.polimi.ingsw.ps42.model.exception.GameLogicError;
import it.polimi.ingsw.ps42.model.exception.NotEnoughPlayersException;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.leaderCard.LeaderCard;
import it.polimi.ingsw.ps42.model.player.BonusBar;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;
import it.polimi.ingsw.ps42.parser.BanLoader;
import it.polimi.ingsw.ps42.parser.BonusBarLoader;
import it.polimi.ingsw.ps42.parser.ConversionLoader;
import it.polimi.ingsw.ps42.parser.FaithPathLoader;
import it.polimi.ingsw.ps42.view.View;

public class GameLogic implements Observer{
	
	private static final int MAX_BANS_IN_FILE = 7;
	private static final int FAMILIARS_NUMBER = 4;
	
	private static final int FIRST_PERIOD = 0;
	private static final int SECOND_PERIOD = 1;
	private static final int THIRD_PERIOD = 2;

	private Action currentAction;
	private List<Player> players;
	
	//Variables used to know the correct round and action order
	private List<Player> roundOrder;
	private List<Player> actionOrder;
	
	private CardsCreator cardsCreator;
	private Table table;
	private Visitor messageVisitor;
	private int currentPeriod;
	private ActionPrototype bonusAction;
	private List<View> views;		//TODO
	
	
	//Variable used to check the bonusBar
	private List<BonusBar> bonusBarList;
	
	
	public void handleAction(Action action, String playerID) throws ElementNotFoundException{
		/*
		 * 1: Check if is a Bonus Action, if so check it (if something wrong, retransmit and end).
		 * 2: Check Action, if something goes wrong retransmit.
		 * 3: Check if player has requests, if so askRequest .
		 * 4: Stop the Timer and Check if player has council requests, if so resume Timer and askCouncilRequest [and stop (later restart from 5)]. 
		 * 5: Do the Action and delete it from gameLogic.
		 * 6: Stop the Timer and Check if player has requests, if so resume Timer and askRequest.
		 * 7: Stop the Timer and Check if player has council requests, if so resume Timer and askCouncilRequest [and stop (later restart from 8)].
		 * 8: Check player BonusAction, if so create a new Timer and send a playerToken with an action Prototype.
		 */
		
		//Find the player
		Player player = searchPlayer(playerID);
		
		//If the player action isn't good, retrasmit to the player another message 
		if(bonusAction != null && !bonusAction.checkAction(action)){
			
			PlayerToken message = new PlayerToken(player.getPlayerID(), bonusAction);
			player.setBonusAction(bonusAction);
			message.setRetrasmission();
			player.retrasmitMessage(message);
			
		} 
		else {
			//
			Response response = action.checkAction();
			
			if(response == Response.CANNOT_PLAY) {
				//If player can't play, end his action, and move he to the end of actionOrder array
				player.setCanPlay(true);
				actionOrder.remove(actionOrder.indexOf(player));
				actionOrder.add(player);
			}
			else if(response == Response.FAILURE || response == Response.LOW_LEVEL) {
				PlayerToken message = new PlayerToken(player.getPlayerID());
				message.setRetrasmission();
				player.retrasmitMessage(message);
			}
			else {
				
				this.currentAction = action;
				//Control player requests
				applyRequest(player);
				
				//Control player council requests
				applyCouncilRequest(player);
				
				//Apply the action
				try {
					action.doAction();
				} catch (FamiliarInWrongPosition e) {
					System.out.println("[DEBUG]: familiar in wrong position in gamelogic");
				}
				//Remove the action from the variable
				this.currentAction = null;
				
				//Re-check the player requests
				applyRequest(player);
				applyCouncilRequest(player);
				
				//SYNCH resources
				player.synchResource();
				
				bonusAction = player.getBonusAction();
				
				if(bonusAction != null)
					player.askMove();
			}
		}
	}
	
	private void applyRequest(Player player) {
		List<CardRequest> requests = player.getRequests();
		if(requests != null && !requests.isEmpty())
			player.askRequest(requests);
	}
	
	private void applyCouncilRequest(Player player) {
		List<CouncilRequest> councilRequests = player.getCouncilRequests();
		if(councilRequests != null && !councilRequests.isEmpty())
			player.askCouncilRequest(councilRequests);
	}
	
	public GameLogic(List<String> players) throws NotEnoughPlayersException, GameLogicError, IOException, ElementNotFoundException{
		/* 1: Build the players from the names passed.
		 * 2: Build the Table
		 * 3: Load the Bans and set them to the Table
		 * 4: Load the Timers
		 * 5: Build the Visitor
		 */
		//Build the players
		this.players = new ArrayList<>();
		for(String playerID : players)
			this.players.add(new Player(playerID));
		
		
		//Initialize the round array for the first round
		roundOrder = new ArrayList<>();
		
		for(Player player : this.players) {
			roundOrder.add(player);
		}
		
		//Initialize the action array for the first round
		actionOrder = new ArrayList<>();
		
		for(int i = 0; i < FAMILIARS_NUMBER; i++) {
			for(Player player : roundOrder)
				actionOrder.add(player);
		}
		
		//Build the table
		this.table = constructTable(this.players);
		if(table == null)
			throw new NotEnoughPlayersException("GameLogic has not enought player to construct the table");
		
		//Load the three bans from a file
		try {
			BanLoader loader = new BanLoader("Resource//BansFile//firstPeriodBans.json");
			table.addFirstBan(loader.getBan(new Random().nextInt(MAX_BANS_IN_FILE)));
			
			loader.setFileName("Resource//BansFile//secondPeriodBans.json");
			table.addSecondBan(loader.getBan(new Random().nextInt(MAX_BANS_IN_FILE)));
			
			loader.setFileName("Resource//BansFile//thirdPeriodBans.json");
			table.addThirdBan(loader.getBan(new Random().nextInt(MAX_BANS_IN_FILE)));
			loader.close();
		} catch (IOException e) {
			
			System.out.println("Unable to open the ban file in GameLogic");
			throw new GameLogicError("File not found");
			
		} catch (ElementNotFoundException e) {
			
			System.out.println("Unable to find the correct ban");
			throw new GameLogicError("Ban not found");
			
		}
		
		//Construct the visitor to parse the message
		messageVisitor = new ControllerVisitor(this);
		
		//Initialize the view array
		views = new ArrayList<>();
		
		//Initialize the card creator
		cardsCreator = new CardsFirstPeriod();
		
	}
	
	public void addView(View view) {
		views.add(view);
		
		//Adding the controller as an observer of the view
		view.addObserver(this);
		
		//Adding the view as an observer of the model
		table.addObserver(view);
		for(Player player : players)
			player.addObserver(view);
	}
	
	//Private method used to construct the correct table
	private Table constructTable(List<Player> players) {
		if(players.size() == 2)
			return new Table(players.get(0), players.get(1));
		if(players.size() == 3)
			return new Table(players.get(0), players.get(1), players.get(2));
		if(players.size() == 4)
			return new Table(players.get(0), players.get(1), players.get(2), players.get(3));
		return null;
	}
	
	public void initGame() throws GameLogicError{
		/* Method called after the gameLogic is created that handles the entire game.
		 * 1: Load the BonusBar and ask to all the players to choose one. 
		 * 2: Load the LeaderCards and ask to all the players to choose one. 
		 * 3: Call the initRound method while currentPeriod < 6
		 * 4: Call the checkBan if period is even (!=0)
		 * 5: Reset the table (restart from 3)
		 * 6: Load the Resource Conversion File and count the points
		 * 7: Notify the results and finish the game.
		 */
		
		//Load the bonus bar from a file
		try {
			BonusBarLoader loader = new BonusBarLoader("Resource//BonusBars//bonusBars.json");
			bonusBarList = loader.getBonusBars();
			loader.close();
		} catch (IOException e) {
			System.out.println("Unable to open the bonus bar file in gameLogic");
			throw new GameLogicError("File open error");
		}
		
		for(Player player : players) {
			player.askChooseBonusBar(bonusBarList);
		}
		
		//TODO Load the LeaderCards
		
		//Start the match
		currentPeriod = 1;
		while(currentPeriod <= 6) {
			initRound();
			
			//Check the first ban
			if(currentPeriod == 2) {
				checkBan(table.getFirstBan(), 3, FIRST_PERIOD);
			}
			
			if(currentPeriod == 4) {
				checkBan(table.getSecondBan(), 4, SECOND_PERIOD);
			}
			currentPeriod++;
		}
		
		//At the end of the match
		for(Player player : this.players) {
			//Enable the third ban
			if(player.getResource(Resource.FAITHPOINT) < 5) {
				table.getThirdBan().enableEffect(player);
				BanUpdateMessage message = new BanUpdateMessage(player.getPlayerID(), THIRD_PERIOD);
				
				player.notifyNewBan(message);
			}
			
			//Control the request for final effects
			List<CardRequest> requests = player.getRequests();
			if(!requests.isEmpty()) 
				player.askRequest(requests);
			
			List<CouncilRequest> councilRequests = player.getCouncilRequests();
			if(!councilRequests.isEmpty()) 
				player.askCouncilRequest(councilRequests);
			
			//Apply the victory point for the resources
			Packet victoryPoint;
			try {
				ConversionLoader loader = new ConversionLoader("Resource//Configuration//finalResourceConfiguration.json");
				victoryPoint = new Packet();
				victoryPoint.addUnit(loader.getGreenConversion(player.getCardList(CardColor.GREEN).size()));
				victoryPoint.addUnit(loader.getBlueConversion(player.getCardList(CardColor.BLUE).size()));
				
				//Counting the other resources
				int totalPlayerResources = player.getResource(Resource.WOOD) + player.getResource(Resource.MONEY) +
											player.getResource(Resource.STONE) + player.getResource(Resource.SLAVE);
				victoryPoint.addUnit(loader.getOtherResourcesConversion(totalPlayerResources));
				
				player.increaseResource(victoryPoint);
				player.synchResource();
				
				loader.close();
			} catch (IOException e) {
				System.out.println("Unable to open the conversion file");
			}
			
			//Enable the final effect of the cards
			enableFinalEffect(player.getCardList(CardColor.GREEN));
			enableFinalEffect(player.getCardList(CardColor.YELLOW));
			enableFinalEffect(player.getCardList(CardColor.BLUE));
			enableFinalEffect(player.getCardList(CardColor.VIOLET));
			
		}
		
		//Notify the winner
		Player winner = players.get(0);
		for(Player player : players) {
			if(player.getResource(Resource.VICTORYPOINT) > winner.getResource(Resource.VICTORYPOINT))
				winner = player;
		}
				
	}
	
	//Private method to enable the final effect of the card
	private void enableFinalEffect(StaticList<Card> cards) {
		for(Card card : cards) {
			try {
				card.enableFinalEffect();
			} catch (NotEnoughResourcesException e) {
				System.out.println("Player cannot enable the final effect of the card "
						+ "because he hasn't enough resources");
			}
		}
	}
	
	//Private Method to control the ban for all the players
	private void checkBan(Effect ban, int faithPointToHave, int period) {
		
		for(Player player : this.players) {
			if(player.getResource(Resource.FAITHPOINT) < faithPointToHave) {
				//Create the message
				BanUpdateMessage message = new BanUpdateMessage(player.getPlayerID(), period);
				if(ban.getTypeOfEffect() == EffectType.OBTAIN_BAN || ban.getTypeOfEffect() == EffectType.INCREASE_FAMILIARS) {
					player.setBan(ban);
				}
				else {
					ban.enableEffect(player);
				}
				//Send the message
				player.notifyNewBan(message);
				
			}
			else {
				player.askIfPayTheBan(currentPeriod);
			}
		}
	}
	
	public void setBonusBar(int index, String playerID) throws ElementNotFoundException{
		// Set the BonusBar to the player
		Player player = searchPlayer(playerID);
		player.setBonusBar(bonusBarList.remove(index));
	}
	
	public void setLeaderCard(){
		// Set the Leader Card to the player
		
	}
	
	private void initRound(){
		/*	Method called every time a new Round starts:
		 *  1: Load the proper period cards and set them on the Table, CardsCreator goes to the next state
		 *  2: Throw the Dice 
		 *  3: Check active Leader Cards effects and eventually enable them 
		 *  4: For each player in roundOrder ask a Move 
		 */
		
		//Load the card on the table
		table.placeGreenTower(cardsCreator.getNextGreenCards());
		table.placeYellowTower(cardsCreator.getNextYellowCards());
		table.placeBlueTower(cardsCreator.getNextBlueCards());
		table.placeVioletTower(cardsCreator.getNextVioletCards());
		
		//Go to the next state
		try {
			cardsCreator = cardsCreator.nextState();
		} catch (IOException e) {
			System.out.println("Unable to open the cards file");
		}
		
		table.throwDice(new Random());
		
		//TODO Leader Card da fare...
		
		//Start the round
		while(!actionOrder.isEmpty()) {
			Player player = actionOrder.get(0);
			//Leader Card
			for(LeaderCard card : player.getActivatedLeaderCard()) {
				card.enableOnceARoundEffect();
			}
			
			//Ask for leader card request, such as increase a single familiar
			List<LeaderRequest> leaderRequests = player.getLeaderRequests();
			
			if(!leaderRequests.isEmpty())
				player.askLeaderRequest(leaderRequests);
			
			player.askMove();
			actionOrder.remove(actionOrder.indexOf(player));
		}
		//End of the round
		
		//Control the new order and reset the table
		List<Player> newOrder = table.resetTable();
		if(!newOrder.isEmpty()) {
			//Remove the newOrder player from the previous order
			for(Player player : newOrder) {
				roundOrder.remove(roundOrder.indexOf(player));
			}
			
			List<Player> temporary = new ArrayList<>();
			temporary.addAll(newOrder);
			temporary.addAll(roundOrder);
			roundOrder = temporary;
		}
		
		//Set the action array for the next turn
		
		for(int i = 0; i < FAMILIARS_NUMBER; i++) {
			for(Player player : roundOrder)
				actionOrder.add(player);
		}
	}
	
	public void handleBan( String playerID, int index, boolean wantToPayBan ) throws ElementNotFoundException, GameLogicError{
		/* Method called by the Visitor to set a Ban to a Player:
		 * if choice = false set the ban to the player
		 * else resource update
		 */
		
		Player player = searchPlayer(playerID);
		
		if(player != this.actionOrder.get(0))
			throw new GameLogicError("HandleBan error, player can't play");
		
		if(wantToPayBan) {
			//Player wants to pay faith point to haven't the ban
			
			//Adding victory point to player
			try {
				FaithPathLoader loader = new FaithPathLoader("Resource//Configuration//faithPointPathConfiguration.json");
				Packet victoryPoint = new Packet();
				victoryPoint.addUnit(loader.conversion(player.getResource(Resource.FAITHPOINT)));
				
				//If player has the leader card
				if(player.hasMoreVictoryPoint())
					victoryPoint.addUnit(new Unit(Resource.VICTORYPOINT, 5));
				
				player.setToZero(Resource.FAITHPOINT);
				player.increaseResource(victoryPoint);
				player.synchResource();
				
				loader.close();
			} catch (IOException e) {
				System.out.println("Unable to open the faithPath conversion file");
			}
			
		}
		else {
			//Player wants the ban
			BanUpdateMessage message;
			if(index == FIRST_PERIOD) {
				//For the first period ban we have two kind of ban
				if(table.getFirstBan().getTypeOfEffect() == EffectType.OBTAIN_BAN || table.getFirstBan().getTypeOfEffect() == EffectType.INCREASE_FAMILIARS)
					player.setBan(table.getFirstBan());
				else
					table.getFirstBan().enableEffect(player);
				
				message = new BanUpdateMessage(player.getPlayerID(), FIRST_PERIOD);
				player.notifyNewBan(message);
			}
			if(index == SECOND_PERIOD) {
				table.getSecondBan().enableEffect(player);
				message = new BanUpdateMessage(player.getPlayerID(), SECOND_PERIOD);
				player.notifyNewBan(message);
			}
			if(index == THIRD_PERIOD) {
				table.getThirdBan().enableEffect(player);
				message = new BanUpdateMessage(player.getPlayerID(), THIRD_PERIOD);
				player.notifyNewBan(message);
			}
			
		}
	}
	
	public void handleRequest(CardRequest request) throws ElementNotFoundException, GameLogicError{
		/* Method called by the Visitor to set a request response to a Player:
		 * if something wrong retransmit the message, else add the request to the player request
		 */
		
		Player player = searchPlayer(request.getPlayerID());

		if(player != this.actionOrder.get(0))
			throw new GameLogicError("Error in handleRequest, player can't play");
		
		if(request.getChoice() < request.showChoice().size()) {
			//If there is an action, add the request to the player
			//else apply directly the request
			if(this.currentAction != null) {
				player.addRequest(request);
			}
			else {
				request.apply();
				player.synchResource();
			}
		}
		else {
			//In case the player hasn't insert the correct information
			request.setRetrasmission();
			player.retrasmitMessage(request);
		}
	}
	
	public void handleCouncilRequest(CouncilRequest councilRequest) throws ElementNotFoundException, GameLogicError{
		/* Method called by the Visitor to set a council request response to a Player:
		 * if something wrong retransmit the message, else add the request to the player council request
		 */
		
		Player player = searchPlayer(councilRequest.getPlayerID());
		
		if(player != this.actionOrder.get(0))
			throw new GameLogicError("Error in handleCouncilRequest, player can't play");
		
		councilRequest.apply(player);
		player.synchResource();
	}	
	
	@Override
	public void update(Observable arg0, Object arg1) {
		Message message;
		if(arg1 instanceof Message) {
			message = (Message) arg1;
			message.accept(messageVisitor);
		}
	}
	
	//Private method used to search the correct player from the playerID string
	public Player searchPlayer(String playerID) throws ElementNotFoundException {
		Player temporaryPlayer = null;
		
		for(Player player : players) {
			if(player.getPlayerID() == playerID)
				temporaryPlayer = player;
		}
		
		if(temporaryPlayer == null)
			throw new ElementNotFoundException("Player: " + playerID + "not found in GameLogic, STOP");
		return temporaryPlayer;
	}
	
	public Table getTable() {
		//Required in action creator
		return this.table;
	}
	
	public int getBonusActionValue() {
		//If there is a bonus action, take the correct value of the action
		if(bonusAction == null)
			return 0;
		return bonusAction.getLevel();
	}
	
	public void HandleLeaderUpdate(Player player, LeaderCard card) {
		player.enableLeaderCard(card);
	}
	
	public void setLeaderCard(int chosenCard, String player) {
		
	}

}
