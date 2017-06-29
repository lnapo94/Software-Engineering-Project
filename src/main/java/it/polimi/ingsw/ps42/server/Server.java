package it.polimi.ingsw.ps42.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import it.polimi.ingsw.ps42.client.ClientInterface;
import it.polimi.ingsw.ps42.message.LoginMessage;
import it.polimi.ingsw.ps42.model.exception.ElementNotFoundException;
import it.polimi.ingsw.ps42.model.exception.GameLogicError;
import it.polimi.ingsw.ps42.model.exception.NotEnoughPlayersException;
import it.polimi.ingsw.ps42.parser.TimerLoader;
import it.polimi.ingsw.ps42.server.match.Connection;
import it.polimi.ingsw.ps42.server.match.ServerView;

public class Server extends UnicastRemoteObject implements ServerInterface{

	/**
	 * 
	 */
	private static final long serialVersionUID = 778583111570504358L;
	private static final int SERVER_PORT = 5555;
	private static final int MATCH_NUMBER = 128;
	
	private static long TIMER_SECONDS;
	
	private boolean isActive = true;
	
	//Logger
	private transient Logger logger = Logger.getLogger(Server.class);
	
	//Socket for the server
	private ServerSocket serverSocket;
	
	//Executor to run more matches at the same time
	private ExecutorService executor;
	
	//Table to know where the player is playing
	private HashMap<String, ServerView> playerTable;
	
	//View is waiting the start
	private ServerView waitingView;
	
	private Timer timer;
	
	public Server() throws IOException {
		super();
		
		//Configure logger
		PropertyConfigurator.configure("Logger//Properties//server_log.properties");
		
		//Load timer
		TimerLoader loader = new TimerLoader("Resource//Configuration//timers.json");
		TIMER_SECONDS = loader.getServerTimer();
		
		logger.info("Timer for the match is setted to: " + TIMER_SECONDS);
		
		logger.info("Starting RMI procedure");
		logger.info("Create registry...");
		LocateRegistry.createRegistry(1099);
		logger.info("Export the server");
		Naming.rebind("Server", this);
		logger.info("RMI procedure done!");
		
		
		try {
			serverSocket = new ServerSocket(SERVER_PORT);
			executor = Executors.newFixedThreadPool(MATCH_NUMBER);
			playerTable = new HashMap<>();
		} catch (IOException e) {
			logger.error("Error in Server Creation");
			logger.info(e);
		}
	}
	
	public synchronized void addPlayer(String playerID, Socket socket, ObjectInputStream reader, ObjectOutputStream writer) throws ElementNotFoundException {
		//Add a player to a match
		try {
			writer.flush();
		} catch (IOException e) {
			logger.fatal("Network Error");
			logger.info(e);
		}
		
		logger.info("Adding a new player...");
		
		Connection connection = new Connection(playerID, socket, reader, writer);
		
		//If the player yet exists, add it to the correct view
		if(existAnotherPlayer(playerID)) {
			executor.submit(connection);
			playerTable.get(playerID).addConnection(connection, playerID);
		}
		else {
			//If there isn't a waiting match, create it
			if(waitingView == null) 
				waitingView = new ServerView();
			
			//Add a connection to the waiting view
			waitingView.addConnection(connection, playerID);
			executor.submit(connection);
			
			if(waitingView.getNumberOfPlayers() == 2) {
				timer = new Timer();
				timer.schedule(new ServerTimer(this), TIMER_SECONDS * 1000);
			}
			
			playerTable.put(playerID, waitingView);
			
			//If the waitingView is full, start the match
			if(waitingView.getNumberOfPlayers() == 4) {
				startMatch();
			}
		}
	}
	
	public synchronized boolean existAnotherPlayer(String playerID) {
		return playerTable.containsKey(playerID);
	}
	
	public synchronized boolean playerWasPlaying(String playerID) {
		return playerTable.get(playerID).wasConnected(playerID);
	}
	
	public void run() {
		try {
			logger.info("Server is now running");
			while(isActive) {
				Socket socket = serverSocket.accept();
				System.out.println("Connection accept, create thread");
				LoginThread thread = new LoginThread(this, socket);
				executor.submit(thread);
			}
		} 
		catch(IOException e) {
			logger.error("Error while server was running...");
			logger.info(e);
			isActive = false;
		}
	}
	
	//Method used in timer
	public synchronized void startMatch() {
		try {
			if(waitingView != null) {
				timer.cancel();
				waitingView.run();
				waitingView = null;
			}
		} catch (NotEnoughPlayersException | GameLogicError | IOException e) {
			logger.error("Start the match error");
			logger.info(e);
		}

	}

	@Override
	public void sendLoginMessage(ClientInterface client, LoginMessage loginMessage) throws RemoteException {
		
		logger.info("Adding a new player...");
		
		String playerID = loginMessage.getUserName();
		
		//If the player yet exists, add it to the correct view
		if(existAnotherPlayer(playerID)) {
			if(playerWasPlaying(playerID)) {
				client.notifyServerView(playerTable.get(playerID).getID());
			}
			else {
				loginMessage.playerIdYetUsed();
				client.notify(loginMessage);
			}
		}
		else {
			//If there isn't a waiting match, create it
			if(waitingView == null) 
				waitingView = new ServerView();
			
			//Add a connection to the waiting view
			client.notifyServerView(waitingView.getID());
			
			if(waitingView.getNumberOfPlayers() == 2) {
				timer = new Timer();
				timer.schedule(new ServerTimer(this), TIMER_SECONDS * 1000);
			}
			
			playerTable.put(playerID, waitingView);
			
			//If the waitingView is full, start the match
			if(waitingView.getNumberOfPlayers() == 4) {
				startMatch();
			}
		}
		logger.info("RMI Player added");
	}
	
	
	public static void main(String[] args) throws IOException {
		Server server = new Server();		
		server.run();
	}

	
}
