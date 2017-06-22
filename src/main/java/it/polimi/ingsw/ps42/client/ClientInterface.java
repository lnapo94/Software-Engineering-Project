package it.polimi.ingsw.ps42.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

import it.polimi.ingsw.ps42.message.LoginMessage;
import it.polimi.ingsw.ps42.message.Message;

public interface ClientInterface extends Remote{

	public void notify(Message message) throws RemoteException;
	public void notify(LoginMessage loginMessage) throws RemoteException;
}
