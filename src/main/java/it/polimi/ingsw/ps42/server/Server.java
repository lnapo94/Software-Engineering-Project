package it.polimi.ingsw.ps42.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import it.polimi.ingsw.ps42.client.ClientInterface;
import it.polimi.ingsw.ps42.message.LoginMessage;

public class Server extends UnicastRemoteObject implements ServerInterface{

	/**
	 * 
	 */
	private static final long serialVersionUID = 778583111570504358L;
	private static final int SERVER_PORT = 5555;
	
	private ServerSocket serverSocket;
	
	
	protected Server() throws RemoteException {
		super();
		try {
			serverSocket = new ServerSocket(SERVER_PORT);
			
		} catch (IOException e) {
			System.out.println("Error in creation of serverSocket");
		}
	}

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
