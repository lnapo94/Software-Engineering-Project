package it.polimi.ingsw.ps42.controller;

import java.util.TimerTask;

import it.polimi.ingsw.ps42.model.player.Player;

public class InitGameTimer extends TimerTask{
	
	private Player player;
	private GameLogic logic;
	
	public InitGameTimer(Player player, GameLogic logic) {
		this.player = player;
		this.logic = logic;
	}
	
	@Override
	public void run() {
		System.out.println("Timer for the initialization expired");
		if(logic.getCurrentPlayer() != null) {
			logic.setBonusBar(0, player.getPlayerID());
		}
		else {
			logic.setLeaderCard(0, player.getPlayerID());
		}
	}
}
