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

public class ServerView extends Observable implements Observer, ServerViewInterface{
	
	private static int serverViewIndex = 0;
	
	private Map<String, Connection> connections;
	private List<String> disconnectedPlayers;
	
	//Using for RMI connections
	private Map<String, ClientInterface> RMIConnections;
	
	//Logger
	private transient Logger logger = Logger.getLogger(ServerView.class);
	
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
	
	public String getID() {
		return "ServerView" + serverViewIndex;
	}
	
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
	
	private void connect(Connection connection, String playerID){
		
		connection.addObserver(this);
		connections.put(playerID, connection);
	}
	
	private int search(String playerID) throws ElementNotFoundException{
		
		for (String player: disconnectedPlayers) {
			if(player.equals(playerID))
				return disconnectedPlayers.indexOf(player);
		}
		throw new ElementNotFoundException("Player not present");
	}
	
	public boolean wasConnected(String playerID){
		//Return if the playerID passed is in use by an active Player
		for (String player: disconnectedPlayers) {
			if(player.equals(playerID))
				return true;
		}
		
		return false;
	}
	
	public boolean nameNotUsed(String playerID){
		
		return !connections.containsKey(playerID) && !RMIConnections.containsKey(playerID);
	}
	
	public int getNumberOfPlayers(){
		return connections.size() + RMIConnections.size();
	}
	
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
	
	public void deleteConnection(String playerID) {
		disconnectedPlayers.add(playerID);
		Connection connection = connections.remove(playerID);
		connection.deleteObserver(this);
	}

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

	@Override
	public void disconnectClient(String playerID) throws RemoteException {
		disconnectedPlayers.add(playerID);
	}

	@Override
	public void sendMessage(Message message) throws RemoteException {
		logger.info("new msg for the game logic from:" +message.getPlayerID());
		setChanged();
		notifyObservers(message);
	}
	


}
