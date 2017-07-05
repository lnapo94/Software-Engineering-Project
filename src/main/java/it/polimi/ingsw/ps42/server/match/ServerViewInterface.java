package it.polimi.ingsw.ps42.server.match;

import java.rmi.Remote;
import java.rmi.RemoteException;

import it.polimi.ingsw.ps42.client.ClientInterface;
import it.polimi.ingsw.ps42.message.Message;

/**
 * Interface to manage correctly a RMI connection in a ServerView Class
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public interface ServerViewInterface extends Remote{
	
	/**
	 * Method used to add to a ServerView the specify Client
	 * 
	 * @param client			The ClientInterface to add to the ServerView
	 * @param playerID			The chosen player's ID
	 * @throws RemoteException	Thrown if there is a network error
	 */
	public void connectToServerView(ClientInterface client, String playerID) throws RemoteException;
	
	/**
	 * Method used to disconnect the specify ID from the ServerView
	 * @param playerID				The player's ID to disconnect from the ServerView
	 * @throws RemoteException		Thrown if there is a network error
	 */
	public void disconnectClient(String playerID) throws RemoteException;
	
	/**
	 * Method used to send a Message to the ServerView
	 * @param message				The message to send to the ServerView
	 * @throws RemoteException		Thrown if there is a network error
	 */
	public void sendMessage(Message message) throws RemoteException;

}
