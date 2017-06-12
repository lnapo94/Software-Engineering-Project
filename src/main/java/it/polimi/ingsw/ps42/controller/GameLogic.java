package it.polimi.ingsw.ps42.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import it.polimi.ingsw.ps42.controller.cardCreator.CardsCreator;
import it.polimi.ingsw.ps42.message.CouncilRequest;
import it.polimi.ingsw.ps42.message.Message;
import it.polimi.ingsw.ps42.message.RequestInterface;
import it.polimi.ingsw.ps42.message.visitorPattern.ControllerVisitor;
import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.Table;
import it.polimi.ingsw.ps42.model.action.Action;
import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.exception.ElementNotFoundException;
import it.polimi.ingsw.ps42.model.exception.GameLogicError;
import it.polimi.ingsw.ps42.model.exception.NotEnoughPlayersException;
import it.polimi.ingsw.ps42.model.player.BonusBar;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.parser.BanLoader;
import it.polimi.ingsw.ps42.parser.BonusBarLoader;
import it.polimi.ingsw.ps42.view.View;

public class GameLogic implements Observer{
	
	private static final int MAX_BANS_IN_FILE = 7;

	private Action currentAction;
	private List<Player> players;
	private List<Player> roundOrder;
	private CardsCreator cardsCreator;
	private Table table;
	private Visitor messageVisitor;
	private Message currentMessage;
	private int currentPeriod;
	private List<View> views;		//TODO
	
	//Variable used to check the bonusBar
	private List<BonusBar> bonusBarList;
	
	//TO-DO: Gestione e Caricamento dei Timer
	
	
	public void handleAction(Action action){
		/* 0: Stop the player timer.
		 * 1: Check if is a Bonus Action, if so check it (if something wrong, retransmit and end).
		 * 2: Check Action, if something goes wrong retransmit.
		 * 3: Check if player has requests, if so resume Timer and askRequest .
		 * 4: Stop the Timer and Check if player has council requests, if so resume Timer and askCouncilRequest [and stop (later restart from 5)]. 
		 * 5: Do the Action and delete it from gameLogic.
		 * 6: Stop the Timer and Check if player has requests, if so resume Timer and askRequest.
		 * 7: Stop the Timer and Check if player has council requests, if so resume Timer and askCouncilRequest [and stop (later restart from 8)].
		 * 8: Check player BonusAction, if so create a new Timer and send a playerToken with an action Prototype.
		 */
		
		
		
		
	}
	
	public GameLogic(List<String> players) throws NotEnoughPlayersException, GameLogicError{
		/* 1: Build the players from the names passed.
		 * 2: Build the Table
		 * 3: Load the Bans and set them to the Table
		 * 4: Load the Timers
		 * 5: Build the Visitor
		 */
		//Build the players
		this.players = new ArrayList<>();
		
		for(String playerID : players) {
			Player temporary = new Player(playerID);
			this.players.add(temporary);
		}
		
		//Build the table
		this.table = constructTable(this.players);
		if(table == null)
			throw new NotEnoughPlayersException("GameLogic has not enought player to construct the table");
		
		//Load the three bans from a file
		try {
			BanLoader loader = new BanLoader("src/bans/firstBans");
			table.addFirstBan(loader.getBan(new Random().nextInt(MAX_BANS_IN_FILE)));
			
			loader.setFileName("src/bans/secondBans");
			table.addSecondBan(loader.getBan(new Random().nextInt(MAX_BANS_IN_FILE)));
			
			loader.setFileName("src/bans/thirdBans");
			table.addThirdBan(loader.getBan(new Random().nextInt(MAX_BANS_IN_FILE)));
		} catch (IOException e) {
			
			System.out.println("Unable to open the file in GameLogic");
			throw new GameLogicError("File not found");
			
		} catch (ElementNotFoundException e) {
			
			System.out.println("Unable to find the correct ban");
			throw new GameLogicError("Ban not found");
			
		}
		
		//Construct the visitor to parse the message
		messageVisitor = new ControllerVisitor(this);
		
		//Initialize the view array
		views = new ArrayList<>();
		
		
		//TODO INITIALIZE THE TIMERS
		
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
			BonusBarLoader loader = new BonusBarLoader("src/bonusBarConfig");
			bonusBarList = loader.getBonusBars();
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
				checkBan(table.getFirstBan(), 3);
			}
			
			if(currentPeriod == 4) {
				checkBan(table.getSecondBan(), 4);
			}
			
			if(currentPeriod == 6) {
				checkBan(table.getThirdBan(), 5);
			}
		}
		
		//At the end of the match
		
				
	}
	
	//Private Method to control the ban for all the players
	private void checkBan(Effect ban, int faithPointToHave) {
		
		for(Player player : this.players) {
			if(player.getResource(Resource.FAITHPOINT) < faithPointToHave) {
				if(ban.getTypeOfEffect() == EffectType.OBTAIN_BAN || ban.getTypeOfEffect() == EffectType.INCREASE_FAMILIARS) {
					player.setBan(ban);
				}
				else
					ban.enableEffect(player);
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
		 *  4: For each player in roundOrder start a Timer and ask a Move 
		 */
	}
	
	public void handleBan( String playerID, int index, boolean wantToPayBan ) throws ElementNotFoundException{
		/* Method called by the Visitor to set a Ban to a Player:
		 * if choice = false set the ban to the player
		 * else resource update
		 */
		
		Player player = searchPlayer(playerID);
		
		if(wantToPayBan) {
			//Player wants to pay faith point to haven't the ban
			player.setToZero(Resource.FAITHPOINT);
			//TODO Increase player faith point with the correct value
		}
		else {
			//Player wants the ban
			
			if(index == 1) {
				//For the first period ban we have two kind of ban
				if(table.getFirstBan().getTypeOfEffect() == EffectType.OBTAIN_BAN || table.getFirstBan().getTypeOfEffect() == EffectType.INCREASE_FAMILIARS)
					player.setBan(table.getFirstBan());
				else
					table.getFirstBan().enableEffect(player);
			}
			if(index == 2) {
				table.getSecondBan().enableEffect(player);
			}
			if(index == 3) {
				table.getThirdBan().enableEffect(player);
			}
			
		}
	}
	
	public void handleRequest( RequestInterface request){
		/* Method called by the Visitor to set a request response to a Player:
		 * if something wrong retransmit the message, else add the request to the player request
		 */
		
	}
	
	public void handleCouncilRequest( CouncilRequest councilRequest){
		/* Method called by the Visitor to set a council request response to a Player:
		 * if something wrong retransmit the message, else add the request to the player council request
		 */
		
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
	private Player searchPlayer(String playerID) throws ElementNotFoundException {
		Player temporaryPlayer = null;
		
		for(Player player : players) {
			if(player.getPlayerID() == playerID)
				temporaryPlayer = player;
		}
		
		if(temporaryPlayer == null)
			throw new ElementNotFoundException("Player: " + playerID + "not found in GameLogic, STOP");
		return temporaryPlayer;
	}

}
