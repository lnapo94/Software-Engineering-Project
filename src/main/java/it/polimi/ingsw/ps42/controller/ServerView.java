package it.polimi.ingsw.ps42.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.polimi.ingsw.ps42.message.Message;
import it.polimi.ingsw.ps42.model.exception.GameLogicError;
import it.polimi.ingsw.ps42.model.exception.NotEnoughPlayersException;
import it.polimi.ingsw.ps42.client.ClientInterface;
import it.polimi.ingsw.ps42.controller.GameLogic;

public class ServerView extends Observable implements Observer{
	
	//TODO Gestire i timer
	private Map<String, Connection> connections;
	
	public ServerView() {
	
		connections = new HashMap<>();
	}
	
	public void addConnection(Connection connection, String playerID){
		
		//Add a Socket Client to the game
		connection.addObserver(this);
		connections.put(playerID, connection);
		
	}
	
	public void addRMIClient(ClientInterface client){
		//TODO Add a RMI Client to the game
		
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
				System.out.println("new msg for the game logic from:" +message.getPlayerID());
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
		
		//Send to Socket Client
		connections.forEach((playerID, connection)->{
			connection.send(message);
		});
		
		//TODO: Send to RMI Client
		
	}
	
	public static void main(String[] args) {
		
		ServerSocket serverSocket;
		try {
			ExecutorService executor= Executors.newFixedThreadPool(128);
			serverSocket = new ServerSocket(12345);
			int i=1;
			ServerView gameConnection = new ServerView() ;
			while(true){
				System.out.println("aspetto nuova connessione..");
				Socket newSocket=serverSocket.accept();
				System.out.println("creo nuova connessione..");
				Connection connection= new Connection(newSocket);
				System.out.println("aggiungo la nuova connessione..");
				gameConnection.addConnection(connection, ("primoGiocatore"+i));
				i++;
				executor.submit(connection);
				if( i == 3){
					
					System.out.println("Game Starts");
					gameConnection.run();
				}
			}
		}
		catch (IOException | NotEnoughPlayersException | GameLogicError e) {
						// TODO: handle exception
			System.out.println("errore nella connessione");
			e.printStackTrace();
		}
						
	}

}
