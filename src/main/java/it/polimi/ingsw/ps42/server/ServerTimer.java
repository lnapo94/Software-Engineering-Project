package it.polimi.ingsw.ps42.server;

import java.util.TimerTask;

import org.apache.log4j.Logger;

/**
 * Class that represents the match timer. When a match reaches the minimum players value (2 players), this
 * timer starts. When it expired, the waiting match starts without waiting the maximum number of
 * players (4 players)
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class ServerTimer extends TimerTask{
	
	//When timer goes on, then start the match
	
	private Server server;
	
	//Logger
	private transient Logger logger = Logger.getLogger(ServerTimer.class);
	
	/**
	 * The constructor of this TimerTask. It needs a reference to the Server to start the
	 * waiting match
	 * 
	 * @param server		A reference to the Server
	 */
	public ServerTimer(Server server) {
		this.server = server;
	}
	
	/**
	 * Method used when the timer will expire. It is used to start the waiting match
	 */
	@Override
	public void run() {
		logger.info("Timer expired, starting match");
		server.startMatch();
	}

}
