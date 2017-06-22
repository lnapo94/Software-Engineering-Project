package it.polimi.ingsw.ps42.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import it.polimi.ingsw.ps42.client.ClientInterface;
import it.polimi.ingsw.ps42.message.LoginMessage;

public interface ServerInterface extends Remote{

	public void addClient(ClientInterface client) throws RemoteException;
	
	public void sendLoginMessage(LoginMessage loginMessage) throws RemoteException;
	public void addToGame() throws RemoteException;
	
}
