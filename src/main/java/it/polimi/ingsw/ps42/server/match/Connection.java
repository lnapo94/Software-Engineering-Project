package it.polimi.ingsw.ps42.server.match;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.message.GenericMessage;
import it.polimi.ingsw.ps42.message.Message;

public class Connection extends Observable implements Runnable{

	private Socket socket;
	private ObjectInputStream reader;
	private ObjectOutputStream writer;
	private boolean active;
	private String playerID;
	
	//Logger
	private transient Logger logger = Logger.getLogger(Connection.class);
	
	public Connection(String playerID, Socket socket, ObjectInputStream reader, ObjectOutputStream writer) {
			this.socket = socket;
			this.reader = reader;
			this.writer = writer;
			this.active = true;
			this.playerID = playerID;
	}

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
	
	private boolean isActive(){
		if(socket.isInputShutdown() || socket.isOutputShutdown())
			active = false;
		return active;
	}
	
	private void close(){
		setChanged();
		notifyObservers(this.playerID);
		closeConnection();
		logger.info("Cancel the client");
	}
	
	private synchronized void closeConnection(){
		
		try {
			socket.close();
		} catch (IOException e) {
			logger.error("error in socket close");
			logger.info(e);
		}
		active=false;
		
	}
	
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
