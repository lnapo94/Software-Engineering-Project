package it.polimi.ingsw.ps42.message;

import java.io.Serializable;
import java.util.List;

/**
 * Message sent when a player is connecting to the ServerView to inform him of which players
 * are playing in the current match
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class PlayersListMessage implements GenericMessage, Serializable{


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
