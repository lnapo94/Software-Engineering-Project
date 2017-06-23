package it.polimi.ingsw.ps42.message;

public class LoginMessage {
	//Message used to connect the client with the server
	
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
