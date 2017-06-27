package it.polimi.ingsw.ps42.controller;

import java.util.TimerTask;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.model.player.Player;

/**
 * TimerTask used to stop a player when he takes to much time to answer during the init phase
 * (Sending bonus bars and leader cards)
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class InitGameTimer extends TimerTask{
	
	private Player player;
	private GameLogic logic;
	
	private transient Logger logger = Logger.getLogger(InitGameTimer.class);
	
	/**
	 * Construct the timer
	 * 
	 * @param player	The player whose the timer refers
	 * @param logic		A reference to the correct GameLogic
	 */
	public InitGameTimer(Player player, GameLogic logic) {
		this.player = player;
		this.logic = logic;
	}
	
	/**
	 * Method called to run the timer when it is expired
	 */
	@Override
	public void run() {
		logger.info("Timer for the initialization expired");
		if(logic.getCurrentPlayer() != null) {
			logic.setBonusBar(0, player.getPlayerID());
		}
		else {
			logic.setLeaderCard(0, player.getPlayerID());
		}
	}
}
