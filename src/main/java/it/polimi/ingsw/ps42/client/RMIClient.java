package it.polimi.ingsw.ps42.client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import it.polimi.ingsw.ps42.message.GenericMessage;
import it.polimi.ingsw.ps42.message.LoginMessage;
import it.polimi.ingsw.ps42.message.Message;
import it.polimi.ingsw.ps42.server.ServerInterface;
import it.polimi.ingsw.ps42.server.match.ServerViewInterface;
import it.polimi.ingsw.ps42.view.TerminalView;
import it.polimi.ingsw.ps42.view.View;
import it.polimi.ingsw.ps42.view.GUI.GUIView;

/**
 * The client that connects to the server thanks to RMI
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class RMIClient extends Observable implements Observer, ClientInterface{
	
	private ServerInterface server;
	private ServerViewInterface serverView;
	
	private String playerID;
	
	
	private transient Logger logger = Logger.getLogger(RMIClient.class);
	
	/**
	 * Constructor of the RMI client object
	 * 
	 * @param hostname	The IP address of the RMI Server
	 */
	public RMIClient(String hostname) {
		PropertyConfigurator.configure("Logger//Properties//client_log.properties");
		String RMIhostname = "//" + hostname + "/Server";
		
		try {
			server = (ServerInterface) Naming.lookup(RMIhostname);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			logger.fatal("Error in connection with RMI server");
			logger.info(e);
		}
	}
	
	/**
	 * Method used to add a view like observer and to add to a view this
	 * object like observer
	 * 
	 * @param view		The interested view to connect
	 */
	public void addView(View view){
		view.addObserver(this);
		this.addObserver(view);
	}

	/**
	 * Method used to notify the client that arrived a new message
	 */
	@Override
	public void notify(GenericMessage message) throws RemoteException {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				setChanged();
				notifyObservers(message);
			}
		}).start();
	}

	/**
	 * Method used to notify to the Client that a ServerView is ready to accept it
	 */
	@Override
	public void notifyServerView(String serverViewID) throws RemoteException {
		try {
			this.serverView = (ServerViewInterface) Naming.lookup(serverViewID);
			this.server = null;
			serverView.connectToServerView(this, playerID);
		} catch (MalformedURLException | NotBoundException e) {
			logger.fatal("Unable to connect to specify ServerView");
			logger.info(e);
		}
	}

	/**
	 * Method used to notify the correct RMI Object (Server for the login messages, ServerView for the match messages)
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		if(arg1 instanceof Message){
			if(serverView != null) {
				try {
					serverView.sendMessage((Message) arg1);
				} catch (RemoteException e) {
					logger.info("Unable to send a message to the ServerView");
					logger.info(e);
					
					try {
						serverView.disconnectClient(playerID);
					} catch (RemoteException e1) {
						logger.fatal("Unable to disconnect from the RMI ServerView");
						logger.info(e1);
					}
				}
			}
		}
		if(arg1 instanceof LoginMessage) {
			if(server != null) {
				try {
					LoginMessage message = (LoginMessage) arg1;
					this.playerID = message.getUserName();
					server.sendLoginMessage((ClientInterface) UnicastRemoteObject.exportObject(this, 0), message);
				} catch (RemoteException e) {
					logger.info("Unable to send a message to the RMI Server");
					logger.info(e);
					
				}
			}
		}
	}
	
	/**
	 * Method used to start the RMI client and create the correct view
	 * 
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		String hostname = "localhost";
		RMIClient client = new RMIClient(hostname);
		
		Scanner scanner = new Scanner(System.in);
		String input;	
		
		View view = null;

		
		do {
			System.out.println("Which Client do you want? [G : GUI], [C : CLI]");
			
			input = scanner.nextLine();
			input = input.toUpperCase();
			
			if(input.equals("G"))
				view = new GUIView();
			
			if(input.equals("C"))
				view = new TerminalView();
			
		} while ((!input.equals("G") && !input.equals("C")) || view == null);
		
		scanner.close();
		
		client.addView(view);
	}
	
}
