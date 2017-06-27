package it.polimi.ingsw.ps42.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

import it.polimi.ingsw.ps42.message.GenericMessage;

public interface ClientInterface extends Remote{

	public void notify(GenericMessage message) throws RemoteException;
	
	public void setNewServerInterface(Integer index) throws RemoteException;
}
