package it.polimi.ingsw.ps42.server;

import java.util.TimerTask;

public class ServerTimer extends TimerTask{
	
	//When timer goes on, then start the match
	
	private Server server;
	
	public ServerTimer(Server server) {
		this.server = server;
	}
	
	@Override
	public void run() {
		server.startMatch();
	}

}
