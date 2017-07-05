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

/**
 * The Server of the game. It contains and handles all the matches, with 2 different connection
 * technologies: Remote Method Invocation (RMI) and Socket
 * @author Luca Napoletano, Claudio Montanari
 *
 */
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
	
	/**
	 * The constructor of the Server
	 * @throws IOException	Thrown if there is a network error, such as the chosen port is already used or
	 * 						the server is already exported in the Naming of RMI
	 */
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
	
	/**
	 * Method used to add a player to the waiting view. If the waiting view doesn't exist, the Server
	 * creates a new Match, represents by the ServerView class.
	 * If the player is trying to reconnect to his match, this method control his user name and add
	 * he to the correct ServerView
	 * 
	 * @param playerID						The user name to add to the view	
	 * @param socket						The socket with the client connection
	 * @param reader						The reader object to read from the socket
	 * @param writer						The writer object to write on the socket
	 * @throws ElementNotFoundException		Thrown if there is a problem with the current waiting view
	 */
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
	
	/**
	 * Method used to know if the chosen user name is already used
	 * @param playerID		The user name to check
	 * @return				True is the user name is already used, otherwise False
	 */
	public synchronized boolean existAnotherPlayer(String playerID) {
		return playerTable.containsKey(playerID);
	}
	
	/**
	 * Method used to know if the user name was connected to a ServerView yet
	 * @param playerID		The user name to check
	 * @return				True if the player used connected, otherwise False
	 */
	public synchronized boolean playerWasPlaying(String playerID) {
		return playerTable.get(playerID).wasConnected(playerID);
	}
	
	/**
	 * Method used to initialize and run the server
	 */
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
	
	/**
	 * Method used to start a match and cancel the corresponding timer
	 */
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

	/**
	 * Method used in a RMI Client to connect to the Server and send to it his user name. If
	 * the player was already connect, and this is a reconnection, the server add this client
	 * to the correct match
	 */
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
	
	/**
	 * Main method used to start the server
	 * @param args
	 * @throws IOException	Thrown if there is a network error with the server
	 */
	public static void main(String[] args) throws IOException {
		Server server = new Server();		
		server.run();
	}

	
}
