package it.polimi.ingsw.ps42.server;

import java.util.TimerTask;

import org.apache.log4j.Logger;

public class ServerTimer extends TimerTask{
	
	//When timer goes on, then start the match
	
	private Server server;
	
	//Logger
	private transient Logger logger = Logger.getLogger(ServerTimer.class);
	
	public ServerTimer(Server server) {
		this.server = server;
	}
	
	@Override
	public void run() {
		logger.info("Timer expired, starting match");
		server.startMatch();
	}

}
