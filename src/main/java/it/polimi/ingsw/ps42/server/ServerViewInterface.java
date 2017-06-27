package it.polimi.ingsw.ps42.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import it.polimi.ingsw.ps42.message.Message;

public interface ServerViewInterface extends Remote{
	
	public void sendMessage(Message message) throws RemoteException;
	public void disconnect(String playerID) throws RemoteException;
}
