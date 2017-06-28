package it.polimi.ingsw.ps42.server.match;

import java.rmi.Remote;
import java.rmi.RemoteException;

import it.polimi.ingsw.ps42.client.ClientInterface;
import it.polimi.ingsw.ps42.message.Message;

public interface ServerViewInterface extends Remote{
	
	public void connectToServerView(ClientInterface client, String playerID) throws RemoteException;
	public void disconnectClient(String playerID) throws RemoteException;
	public void sendMessage(Message message) throws RemoteException;

}
