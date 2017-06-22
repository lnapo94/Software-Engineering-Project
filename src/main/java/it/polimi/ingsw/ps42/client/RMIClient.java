package it.polimi.ingsw.ps42.client;

import java.rmi.RemoteException;

import it.polimi.ingsw.ps42.message.LoginMessage;
import it.polimi.ingsw.ps42.message.Message;

public class RMIClient implements ClientInterface{

	@Override
	public void notify(Message message) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notify(LoginMessage loginMessage) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

}
