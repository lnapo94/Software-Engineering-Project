package it.polimi.ingsw.ps42.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import it.polimi.ingsw.ps42.client.ClientInterface;
import it.polimi.ingsw.ps42.message.LoginMessage;

/**
 * Interface for the Server to implement the RMI-oriented connection
 * @author luca
 *
 */
public interface ServerInterface extends Remote{

	/**
	 * Method used in the RMI Client to send a Login Message to start a communication
	 * and to add the specified client to a waiting match
	 * 
	 * @param client			The client who sends this message
	 * @param loginMessage		The LoginMessage that contains the chosen user name
	 * @throws RemoteException	Thrown if there is a Network Exception
	 */
	public void sendLoginMessage(ClientInterface client, LoginMessage loginMessage) throws RemoteException;
	
}
