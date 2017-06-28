package it.polimi.ingsw.ps42.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import it.polimi.ingsw.ps42.client.ClientInterface;
import it.polimi.ingsw.ps42.message.LoginMessage;

public interface ServerInterface extends Remote{

	public void sendLoginMessage(ClientInterface client, LoginMessage loginMessage) throws RemoteException;
	
}
