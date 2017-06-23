package it.polimi.ingsw.ps42.controller;

import java.util.TimerTask;

import it.polimi.ingsw.ps42.model.player.Player;

public class PlayerMoveTimer extends TimerTask{
	
	private GameLogic logic;
	private final boolean isBanRequest;
	
	public PlayerMoveTimer(GameLogic logic, boolean isBanRequest) {
		this.logic = logic;
		this.isBanRequest = isBanRequest;
	}
	
	public PlayerMoveTimer(GameLogic logic) {
		this(logic, false);
	}
	
	@Override
	public void run() {
		//Take the current player
		Player player = logic.getCurrentPlayer();
		player.removeAllRequests();
		player.removeAllCouncilRequests();
		player.removeAllLeaderRequests();
		
		//Remove pending requests
		logic.removePlayerFromPendingRequest(player);
		
		//If there is an action in gameLogic, it must be a TakeCard action,
		//so cancel it
		if(logic.isThereAnAction())
			logic.rollBackTakeCardAction();
		
		if(isBanRequest) {
			//Conversion for the correct period
			int index = (logic.getCurrentRound() / 2) - 1;
			
			//By default, enable the ban
			logic.handleBan(player.getPlayerID(), index, false);
		}
	}

}
