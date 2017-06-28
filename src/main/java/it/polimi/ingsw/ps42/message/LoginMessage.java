package it.polimi.ingsw.ps42.message;

import java.io.Serializable;

/**
 * Class to know which user name the player wants. Used only with the Server
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class LoginMessage implements GenericMessage, Serializable{
	//Message used to connect the client with the server
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4050092214596749927L;
	private String username;
	private boolean yetUsed;
	
	public LoginMessage(String username) {
		this.username = username;
		yetUsed = false;
	}
	
	public String getUserName() {
		return this.username;
	}
	
	public void playerIdYetUsed() {
		this.yetUsed = true;
	}
	
	public boolean existAnotherPlayer() {
		return this.yetUsed;
	}
}
