package it.polimi.ingsw.ps42.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import it.polimi.ingsw.ps42.message.GenericMessage;
import it.polimi.ingsw.ps42.message.LoginMessage;
import it.polimi.ingsw.ps42.message.Message;
import it.polimi.ingsw.ps42.server.ServerInterface;
import it.polimi.ingsw.ps42.server.ServerViewInterface;
import it.polimi.ingsw.ps42.view.TerminalView;
import it.polimi.ingsw.ps42.view.View;

public class RMIClient extends Observable implements ClientInterface, Observer{
	
	private transient Logger logger = Logger.getLogger(RMIClient.class);
	
	private ServerViewInterface serverViewInterface;
	private ServerInterface serverInterface;
	
	private ClientInterface remoteRef;
	
	public RMIClient() {
		PropertyConfigurator.configure("Logger//Properties//client_log.properties");
		try {
			serverInterface = (ServerInterface) Naming.lookup("//127.0.0.1/Server");
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			logger.fatal("Error in RMI client creation caused by a network error");
			logger.info(e);
		}
	}
	
	public void run() {		
		try {
			ClientInterface remoteRef = (ClientInterface) UnicastRemoteObject.exportObject(this, 0);
			this.remoteRef = remoteRef;
		} catch (RemoteException e) {
			logger.fatal("Error in RMI client exportation caused by a network error");
			logger.info(e);
		}
	}
	
	public void addView(View view){

		view.addObserver(this);
		this.addObserver(view);
	}

	@Override
	public void notify(GenericMessage message) throws RemoteException {
		setChanged();
		notifyObservers(message);
	}

	@Override
	public void setNewServerInterface(Integer index) throws RemoteException {
		this.serverInterface = null;
		try {
			this.serverViewInterface = (ServerViewInterface) Naming.lookup("//localhost/ServerView" + index);
		} catch (MalformedURLException | NotBoundException e) {
			logger.fatal("Error in connection with the server view");
			logger.info(e);
		}
	}

	@Override
	public void update(Observable o, Object message) {

		if(message instanceof Message){
			Message msg = (Message) message;
			if(serverViewInterface != null) {
				try {
					serverViewInterface.sendMessage(msg);
				} catch (RemoteException e) {
					logger.error("Network error in RMI Client");
					logger.info(e);
				}
			}
		}
		
		if(message instanceof LoginMessage){
			LoginMessage msg = (LoginMessage) message;
			if(serverInterface != null) {
				try {
					serverInterface.sendLoginMessage(remoteRef, msg);
				} catch (RemoteException e) {
					logger.error("Error connecting to the server");
					logger.info(e);
				}
			}
		}
		
	}
	
	public static void main(String[] args) {
		RMIClient client = new RMIClient();
		client.run();
		View view = new TerminalView();
		client.addView(view);
		view.askNewPlayerID();
	}

}
