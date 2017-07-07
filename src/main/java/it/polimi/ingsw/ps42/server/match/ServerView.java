package it.polimi.ingsw.ps42.server.match;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.client.ClientInterface;
import it.polimi.ingsw.ps42.controller.GameLogic;
import it.polimi.ingsw.ps42.message.GenericMessage;
import it.polimi.ingsw.ps42.message.Message;
import it.polimi.ingsw.ps42.message.PlayersListMessage;
import it.polimi.ingsw.ps42.model.exception.ElementNotFoundException;
import it.polimi.ingsw.ps42.model.exception.GameLogicError;
import it.polimi.ingsw.ps42.model.exception.NotEnoughPlayersException;

/**
 * Class that represents a single match. It is used to implements a Model-View-Controller
 * pattern in the server, even if there is a network between the real view and the server.
 * This class manage both Remote Method Invocation (RMI) and Socket connections, and it is used to
 * hide the complexity of the network management from the Controller and Model
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class ServerView extends Observable implements Observer, ServerViewInterface{
	
	private static int serverViewIndex = 0;
	
	private Map<String, Connection> connections;
	private List<String> disconnectedPlayers;
	
	//Using for RMI connections
	private Map<String, ClientInterface> RMIConnections;
	
	//Logger
	private transient Logger logger = Logger.getLogger(ServerView.class);
	
	/**
	 * Constructor of the ServerView. All ServerView uses a static index to know the correct
	 * ServerView to pass to RMI connections
	 */
	public ServerView() {
		serverViewIndex++;
		
		connections = new HashMap<>();
		disconnectedPlayers = new ArrayList<>();
		RMIConnections = new HashMap<>();
		
		//Exporting the RMI object
		try {
			Naming.rebind(getID(), (ServerViewInterface)UnicastRemoteObject.exportObject(this, 0));
		} catch (RemoteException | MalformedURLException e) {
			logger.fatal("Error in binding of ServerView");
			logger.info(e);
		}
	}
	
	/**
	 * Method used to get the ID of the current ServerView for RMI connections
	 * @return	The ID of the ServerView
	 */
	public String getID() {
		return "ServerView" + serverViewIndex;
	}
	
	/**
	 * Method used to add a player to this ServerView. This method also re-add a disconnected player
	 * if he is in this match
	 * 
	 * @param connection					The Connection class that represents a Socket connection
	 * @param playerID						The player's ID to connect to this ServerView
	 * @throws ElementNotFoundException		Thrown if the player isn't found in the current view. It is necessary to reconnect the correct player
	 */
	public void addConnection(Connection connection, String playerID) throws ElementNotFoundException{
		
		//Add a Socket Client to the game
		if(wasConnected(playerID)){
			//If was connected then delete the old connection and add the new one
			disconnectedPlayers.remove(search(playerID));
			connect(connection, playerID);
			sendPlayersList();
			setChanged();
			notifyObservers(playerID);
		}
		else if(nameNotUsed(playerID))
			//If is a new Player add him to the game
			connect(connection, playerID);
	}
	
	/**
	 * Method used to add a connection like observer of this ServerView and to registry the connection in the Map
	 * @param connection		The connection to add to this ServerView
	 * @param playerID			The ID used to know who is the correct player associated to the connection
	 */
	private void connect(Connection connection, String playerID){
		
		connection.addObserver(this);
		connections.put(playerID, connection);
	}
	
	/**
	 * Private method used to search a disconnected player in this ServerView
	 * 
	 * @param playerID						The player's ID to search
	 * @return								The index of the player if it is found
	 * @throws ElementNotFoundException		Thrown if the player isn't found
	 */
	private int search(String playerID) throws ElementNotFoundException{
		
		for (String player: disconnectedPlayers) {
			if(player.equals(playerID))
				return disconnectedPlayers.indexOf(player);
		}
		throw new ElementNotFoundException("Player not present");
	}
	
	/**
	 * Method used to know if a player was connected to this ServerView before
	 * 
	 * @param playerID		The player's ID to search
	 * @return				True if the disconnected players list contains the specify ID, False otherwise
	 */
	public boolean wasConnected(String playerID){
		//Return if the playerID passed is in use by an active Player
		for (String player: disconnectedPlayers) {
			if(player.equals(playerID))
				return true;
		}
		
		return false;
	}
	
	/**
	 * Method used to know if the ID isn't used yet
	 * @param playerID	The ID to check
	 * @return			True if the ID isn't used before, otherwise False 
	 */
	public boolean nameNotUsed(String playerID){
		
		return !connections.containsKey(playerID) && !RMIConnections.containsKey(playerID);
	}
	
	/**
	 * Method used to know how many players are in this ServerView
	 * @return		The current number of player in this ServerView
	 */
	public int getNumberOfPlayers(){
		return connections.size() + RMIConnections.size();
	}
	
	/**
	 * Method used to run the ServerView class and start a match
	 * 
	 * @throws NotEnoughPlayersException		Thrown if there isn't enough players in this ServerView (current players < 2)
	 * @throws GameLogicError					Thrown if there is a problem with the GameLogic
	 * @throws IOException						Thrown if there is a network error
	 */
	public void run() throws NotEnoughPlayersException, GameLogicError, IOException{
		
		logger.info("ServerView is now running...");
		
		sendPlayersList();
		
		List<String> playerIDList = new ArrayList<>();
		connections.forEach((playerID, connection)->{
			playerIDList.add(playerID);
		});
		
		RMIConnections.forEach((playerID, client)->{
			playerIDList.add(playerID);
		});
		
		GameLogic gameLogic = new GameLogic(playerIDList, this);
		gameLogic.loadGame();
	}
	
	/**
	 * Private method used to send to the client the list of players that are playing in this match
	 */
	private void sendPlayersList() {
		List<String> playerIDList = new ArrayList<>();
		connections.forEach((playerID, connection)->{
			playerIDList.add(playerID);
		});
		RMIConnections.forEach((playerID, client)->{
			playerIDList.add(playerID);
		});
		PlayersListMessage message = new PlayersListMessage(playerIDList);
		sendAll(message);
	}
	
	/**
	 * Method used when this ServerView received a message from the GameLogic to send to the Client and vice-versa
	 */
	@Override
	public void update(Observable sender, Object messageToSend) {
		
		if( messageToSend instanceof Message){
			Message message= (Message) messageToSend;
			if(sender instanceof Connection){
				//Send to the Game Logic
				logger.info("new msg for the game logic from:" +message.getPlayerID());
				setChanged();
				notifyObservers(message);
			}
			else{
				//Send to the Players
				sendAll(message);
			}
		}
		else if(messageToSend instanceof String) {
			deleteConnection((String) messageToSend);
			setChanged();
			notifyObservers(messageToSend);
		}
	}
	
	/**
	 * Method used to send a message to all the connected players
	 * @param message	The GenericMessage to send to the Client
	 */
	private void sendAll(GenericMessage message){
		
		//Send to Socket Client
		connections.forEach((playerID, connection)->{
			try {
				connection.send(message);
			} catch (IOException e) {
				//The player is disconnected so remove his connection
				logger.info("The player is disconnected so remove his connection");
				logger.info(e);
				disconnectedPlayers.add(playerID);
				connection.deleteObserver(this);
			}
			
		});
		
		RMIConnections.forEach((playerID, client) -> {
			try {
				client.notify(message);
			} catch (RemoteException e) {
				logger.info("Unable to notify the RMI client, disconnect it");
				logger.info(e);
				try {
					notifyObservers(playerID);
					disconnectClient(playerID);
				} catch (RemoteException e1) {
					logger.error("Unable to disconnect the client");
					logger.info(e1);
				}
			}
		});

		for(String playerID : disconnectedPlayers) {
			connections.remove(playerID);
			RMIConnections.remove(playerID);
		}
	}
	
	/**
	 * Method used to delete a connection and add this player ID to the disconnected players list
	 * @param playerID	The player's ID to disconnect from this ServerVIew
	 */
	public void deleteConnection(String playerID) {
		disconnectedPlayers.add(playerID);
		Connection connection = connections.remove(playerID);
		connection.deleteObserver(this);
	}

	/**
	 * Method used to connect a RMI Client to this ServerView
	 */
	@Override
	public void connectToServerView(ClientInterface client, String playerID) throws RemoteException {
		//Add a Socket Client to the game
		if(wasConnected(playerID)){
			//If was connected then delete the old connection and add the new one
			try {
				disconnectedPlayers.remove(search(playerID));
				
				RMIConnections.put(playerID, client);
				
				sendPlayersList();
				setChanged();
				notifyObservers(playerID);
				
			} catch (ElementNotFoundException e) {
				logger.fatal("Unable to find the player");
				logger.info(e);
			}
		}
		else if(nameNotUsed(playerID))
			//If is a new Player add him to the game
			RMIConnections.put(playerID, client);
	}

	/**
	 * Method used to disconnect a RMI Client from this ServerView
	 */
	@Override
	public void disconnectClient(String playerID) throws RemoteException {
		disconnectedPlayers.add(playerID);
	}

	/**
	 * Method used to send a message to this ServerView
	 */
	@Override
	public void sendMessage(Message message) throws RemoteException {
		logger.info("new msg for the game logic from:" +message.getPlayerID());
		setChanged();
		notifyObservers(message);
	}
	


}
