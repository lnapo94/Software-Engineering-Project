package it.polimi.ingsw.ps42.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.polimi.ingsw.ps42.client.ClientInterface;
import it.polimi.ingsw.ps42.controller.Connection;
import it.polimi.ingsw.ps42.controller.ServerView;
import it.polimi.ingsw.ps42.message.LoginMessage;
import it.polimi.ingsw.ps42.model.exception.ElementNotFoundException;
import it.polimi.ingsw.ps42.model.exception.GameLogicError;
import it.polimi.ingsw.ps42.model.exception.NotEnoughPlayersException;

public class Server extends UnicastRemoteObject implements ServerInterface{

	/**
	 * 
	 */
	private static final long serialVersionUID = 778583111570504358L;
	private static final int SERVER_PORT = 5555;
	private static final int MATCH_NUMBER = 128;
	
	private static final long TIMER_SECONDS = 2;
	
	private boolean isActive = true;
	
	//Socket for the server
	private ServerSocket serverSocket;
	
	//Executor to run more matches at the same time
	private ExecutorService executor;
	
	//Table to know where the player is playing
	private HashMap<String, ServerView> playerTable;
	
	//View is waiting the start
	private ServerView waitingView;
	
	private Timer timer;
	
	public Server() throws RemoteException {
		super();
		try {
			serverSocket = new ServerSocket(SERVER_PORT);
			executor = Executors.newFixedThreadPool(MATCH_NUMBER);
			playerTable = new HashMap<>();
		} catch (IOException e) {
			System.out.println("Error in creation of serverSocket");
		}
	}
	
	public synchronized void addPlayer(String playerID, Socket socket, ObjectInputStream reader, ObjectOutputStream writer) throws ElementNotFoundException {
		//Add a player to a match
		try {
			writer.flush();
		} catch (IOException e) {
			System.out.println("Network error");
		}
		Connection connection = new Connection(socket, reader, writer);
		
		//If the player yet exists, add it to the correct view
		if(existAnotherPlayer(playerID)) {
			playerTable.get(playerID).addConnection(connection, playerID);
			executor.submit(connection);
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
			System.out.println("Server is now running");
			while(isActive) {
				Socket socket = serverSocket.accept();
				System.out.println("Connection accept, create thread");
				LoginThread thread = new LoginThread(this, socket);
				executor.submit(thread);
			}
		} 
		catch(IOException e) {
			System.out.println("Error while server was running...");
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
			System.out.println("Unable to start the match");
		}

	}

	@Override
	public void addClient(ClientInterface client) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendLoginMessage(LoginMessage loginMessage) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addToGame() throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) throws RemoteException {
		Server server = new Server();
		server.run();
	}

	
}
