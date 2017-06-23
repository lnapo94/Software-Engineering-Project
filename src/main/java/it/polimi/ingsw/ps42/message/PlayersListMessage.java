package it.polimi.ingsw.ps42.message;

import java.io.Serializable;
import java.util.List;

public class PlayersListMessage implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 2440354722191946303L;
	private List<String> playerList;
	
	public PlayersListMessage(List<String> playerList) {
		this.playerList = playerList;
	}
	
	public List<String> getPlayerList() {
		return playerList;
	}
	
	
}
