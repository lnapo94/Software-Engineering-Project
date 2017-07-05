package it.polimi.ingsw.ps42.server.match;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.message.GenericMessage;
import it.polimi.ingsw.ps42.message.Message;

/**
 * Class used to manage a connection with a socket client. This class implements Runnable
 * because each connection requires a thread to be managed well
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class Connection extends Observable implements Runnable{

	private Socket socket;
	private ObjectInputStream reader;
	private ObjectOutputStream writer;
	private boolean active;
	private String playerID;
	
	//Logger
	private transient Logger logger = Logger.getLogger(Connection.class);
	
	/**
	 * Constructor for the Connection thread
	 * 
	 * @param playerID		The ID chosen by the player
	 * @param socket		The socket of the connection between Server and Client
	 * @param reader		The reader object to read from the socket
	 * @param writer		The writer object to write on the socket
	 */
	public Connection(String playerID, Socket socket, ObjectInputStream reader, ObjectOutputStream writer) {
			this.socket = socket;
			this.reader = reader;
			this.writer = writer;
			this.active = true;
			this.playerID = playerID;
	}

	/**
	 * Method used to run the connection 
	 */
	@Override
	public void run() {

		logger.info("connection is trying to run");
		try{
			
			while(isActive()){
				Message message=(Message) reader.readObject();
				System.out.println("nuovo msg da inoltrare al controller da: "+ message.getPlayerID());
				setChanged();
				notifyObservers(message);
			}
		
		}
		catch (IOException | ClassNotFoundException e) {
			logger.error("Error occurred in trasmission, closing Connection");
			logger.info(e);
			close();
		}
		
	}
	
	/**
	 * Method used to control if the connection is still active
	 * @return	True if the connection is active, otherwise False
	 */
	private boolean isActive(){
		if(socket.isInputShutdown() || socket.isOutputShutdown())
			active = false;
		return active;
	}
	
	/**
	 * Method used to close the connection
	 */
	private void close(){
		setChanged();
		notifyObservers(this.playerID);
		closeConnection();
		logger.info("Cancel the client");
	}
	
	/**
	 * Synchronized method used to close the socket
	 */
	private synchronized void closeConnection(){
		
		try {
			socket.close();
		} catch (IOException e) {
			logger.error("error in socket close");
			logger.info(e);
		}
		active=false;
		
	}
	
	/**
	 * Method used to send a GenericMessage from the Server to the Client
	 * @param message			The GenericMessage to send to the client
	 * @throws IOException		Thrown if there is a network error
	 */
	public void send(GenericMessage message) throws IOException{
		
		try {
			logger.info("new msg to send");
			writer.writeObject(message);
			writer.flush();
		} catch (IOException e) {
			close();
			logger.error("Error in sending the new message ");
			logger.info("Stop the connection, active = false");
			logger.info(e);
			active = false;
			throw new IOException();
		}
	}
	
}
