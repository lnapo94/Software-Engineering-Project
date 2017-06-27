package it.polimi.ingsw.ps42.controller;

import java.util.TimerTask;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.message.visitorPattern.ControllerVisitor;
import it.polimi.ingsw.ps42.model.player.Player;

/**
 * TimerTask used to stop a player when he takes to much time to answer during the match
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class PlayerMoveTimer extends TimerTask{
	
	private GameLogic logic;
	private final boolean isBanRequest;
	private Player player;
	
	//Logger
	private transient Logger logger = Logger.getLogger(ControllerVisitor.class);
	
	/**
	 * Contruct the player move task
	 * 
	 * @param player			The player whose this timer refers
	 * @param logic				A reference to the GameLogic
	 * @param isBanRequest		True if the timer refers to a ban request, false for the others requests
	 */
	public PlayerMoveTimer(Player player, GameLogic logic, boolean isBanRequest) {
		this.logic = logic;
		this.isBanRequest = isBanRequest;
		this.player = player;
	}
	
	/**
	 * Another construct, it works exactly like the other but it set the isBanRequest variable to false
	 * 
	 * @param player	The player whose this timer refers
	 * @param logic		A reference to the GameLogic
	 */
	public PlayerMoveTimer(Player player, GameLogic logic) {
		this(player, logic, false);
	}
	
	/**
	 * Method called to run the timer when it is expired
	 */
	@Override
	public void run() {
		logger.info("Timer for the player move expired");
		//Take the current player
		player.removeAllRequests();
		player.removeAllCouncilRequests();
		player.removeAllLeaderRequests();
		
		//Remove pending requests
		logic.removePlayerFromPendingRequest(player);
		
		//If there is an action in gameLogic, it must be a TakeCard action,
		//so cancel it
		if(logic.isThereAnAction())
			logic.rollBackTakeCardAction();
		
		logic.initAction();
		
		if(isBanRequest) {
			//Conversion for the correct period
			int index = (logic.getCurrentRound() / 2) - 1;
			
			//By default, enable the ban
			logic.handleBan(player.getPlayerID(), index, false);
		}
	}

}
