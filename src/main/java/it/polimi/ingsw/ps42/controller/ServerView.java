package it.polimi.ingsw.ps42.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import it.polimi.ingsw.ps42.message.Message;
import it.polimi.ingsw.ps42.model.exception.GameLogicError;
import it.polimi.ingsw.ps42.model.exception.NotEnoughPlayersException;
import it.polimi.ingsw.ps42.controller.GameLogic;

public class ServerView extends Observable implements Observer{
	
	//TODO Gestire i timer
	private Map<String, Connection> connections;
	
	public ServerView() {
	
		connections = new HashMap<>();
	}
	
	public void addConnection(Connection connection, String playerID){
		
		connection.addObserver(this);
		connections.put(playerID, connection);
		
	}
	
	public void run() throws NotEnoughPlayersException, GameLogicError, IOException{
		
		List<String> playerIDList = new ArrayList<>();
		connections.forEach((playerID, connection)->{
			playerIDList.add(playerID);
		});
		GameLogic gameLogic = new GameLogic(playerIDList, this);
		gameLogic.loadGame();
	}
	
	@Override
	public void update(Observable sender, Object messageToSend) {
		
		if( messageToSend instanceof Message){
			Message message= (Message) messageToSend;
			if(sender instanceof Connection){
				//Send to the Game Logic
				setChanged();
				notifyObservers(message);
			}
			else{
				//Send to the Players
				sendAll(message);
			}
		}
	}
	
	private void sendAll(Message message){
		connections.forEach((playerID, connection)->{
			connection.send(message);
		});
	}

}
