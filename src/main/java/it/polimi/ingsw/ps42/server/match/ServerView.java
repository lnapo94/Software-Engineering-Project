package it.polimi.ingsw.ps42.server.match;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.message.GenericMessage;
import it.polimi.ingsw.ps42.message.Message;
import it.polimi.ingsw.ps42.message.PlayersListMessage;
import it.polimi.ingsw.ps42.model.exception.ElementNotFoundException;
import it.polimi.ingsw.ps42.model.exception.GameLogicError;
import it.polimi.ingsw.ps42.model.exception.NotEnoughPlayersException;
import it.polimi.ingsw.ps42.client.ClientInterface;
import it.polimi.ingsw.ps42.controller.GameLogic;

public class ServerView extends Observable implements Observer{

	private Map<String, Connection> connections;
	private List<String> disconnectedPlayers;
	
	//Logger
	private transient Logger logger = Logger.getLogger(ServerView.class);
	
	public ServerView() {
	
		connections = new HashMap<>();
		disconnectedPlayers = new ArrayList<>();
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
	
	public void addRMIClient(ClientInterface client){
		//TODO Add a RMI Client to the game
		
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
		
		return !connections.containsKey(playerID);
	}
	
	public int getNumberOfPlayers(){
		return connections.size();
	}
	
	public void run() throws NotEnoughPlayersException, GameLogicError, IOException{
		
		logger.info("ServerView is now running...");
		
		sendPlayersList();
		
		List<String> playerIDList = new ArrayList<>();
		connections.forEach((playerID, connection)->{
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

		for(String playerID : disconnectedPlayers) {
			connections.remove(playerID);
		}
	}
	
	public void deleteConnection(String playerID) {
		disconnectedPlayers.add(playerID);
		Connection connection = connections.remove(playerID);
		connection.deleteObserver(this);
	}
	


}