package it.polimi.ingsw.ps42.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import it.polimi.ingsw.ps42.client.ClientInterface;
import it.polimi.ingsw.ps42.message.LoginMessage;

public class Server extends UnicastRemoteObject implements ServerInterface{

	
	
	protected Server() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 778583111570504358L;

	@Override
	public void addClient(ClientInterface client) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendLoginMessage(LoginMessage loginMessage) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addToGame() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	
}
