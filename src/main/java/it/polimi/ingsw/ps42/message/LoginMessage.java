package it.polimi.ingsw.ps42.message;

import java.util.List;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.player.Player;

public class LoginMessage extends Message{

	private List<Player> allPlayers;
	
	public void setPlayerID(String ID) {
		this.playerID = ID;
	}
	
	public void setAllPlayers(List<Player> allPlayers) {
		this.allPlayers = allPlayers;
	}

	@Override
	public void accept(Visitor v) {
		// TODO Auto-generated method stub
		
	}

}
