package it.polimi.ingsw.ps42.controller;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import it.polimi.ingsw.ps42.controller.cardCreator.CardsCreator;
import it.polimi.ingsw.ps42.message.CouncilRequest;
import it.polimi.ingsw.ps42.message.Message;
import it.polimi.ingsw.ps42.message.RequestInterface;
import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.Table;
import it.polimi.ingsw.ps42.model.action.Action;
import it.polimi.ingsw.ps42.model.player.BonusBar;
import it.polimi.ingsw.ps42.model.player.Player;

public class GameLogic implements Observer{

	private Action currentAction;
	private List<Player> players;
	private List<Player> roundOrder;
	private CardsCreator cardsCreator;
	private Table table;
	private Visitor messageVisitor;
	private Message currentMessage;
	private int currentPeriod;
	
	//TO-DO:private BanLoader banLoader;
	//TO-DO: Gestione e Caricamento dei Timer
	
	/* TO-DO: 
	 * 2: aggiungere in player un retransmitMessage(Message) 
	 * 3: 
	 */
	
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
	
	public GameLogic(List<String> players){
		/* 1: Build the players from the names passed.
		 * 2: Build the Table
		 * 3: Load the Bans and set them to the Table
		 * 4: Load the Timers
		 * 5: Build the Visitor
		 */
		
	}
	
	public void initGame(){
		/* Method called after the gameLogic is created that handles the entire game.
		 * 1: Load the BonusBar and ask to all the players to choose one. 
		 * 2: Load the LeaderCards and ask to all the players to choose one. 
		 * 3: Call the initRound method while currentPeriod < 6
		 * 4: Call the checkBan if period is even (!=0)
		 * 5: Reset the table (restart from 3)
		 * 6: Load the Resource Conversion File and count the points
		 * 7: Notify the results and finish the game.
		 */
		
	}
	
	public void setBonusBar(int index, String playerID){
		// Set the BonusBar to the player
		 
		
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
	
	private void checkBan(){
		/* Method called to check and assign the period ban to the players:
		 * 1: For each Player:
		 *  - if can not pay the ban set the ban to the player(call also the notifyBan method)
		 *  - otherwise ask if he want to pay
		 *  (If is the last round do not ask to the player but do).
		 */
		
	}
	
	public void handleBan( String playerID, int index, boolean payBan ){
		/* Method called by the Visitor to set a Ban to a Player:
		 * if choice = false set the ban to the player
		 * else resource update
		 */
		
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
		/* Cast the message received and visit the message.
		 */
		
	}
	
	

}
