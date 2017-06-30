package it.polimi.ingsw.ps42.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

import it.polimi.ingsw.ps42.message.GenericMessage;

/**
 * Interface used to implement the RMI comunication between client and server
 * This is the client-side
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public interface ClientInterface extends Remote{

	/**
	 * Method used to notify the client that a new GenericMessage is arrived
	 * 
	 * @param message			The message for the client
	 * @throws RemoteException	Thrown if there is a network problem
	 */
	public void notify(GenericMessage message) throws RemoteException;
	
	/**
	 * Method used to notify the client that there is a ServerView object ready to
	 * accept the client connection
	 * 
	 * @param serverViewID		ID used to know the ServerView
	 * @throws RemoteException	Thrown if there is a network problem
	 */
	public void notifyServerView(String serverViewID) throws RemoteException;
}
