package it.polimi.ingsw.ps42.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.message.GenericMessage;
import it.polimi.ingsw.ps42.message.Message;
import it.polimi.ingsw.ps42.message.visitorPattern.ControllerVisitor;

public class Connection extends Observable implements Runnable{

	private Socket socket;
	private ObjectInputStream reader;
	private ObjectOutputStream writer;
	private boolean active;
	
	//Logger
	private transient Logger logger = Logger.getLogger(ControllerVisitor.class);
	
	public Connection(Socket socket, ObjectInputStream reader, ObjectOutputStream writer) {
			this.socket = socket;
			this.reader = reader;
			this.writer = writer;
			this.active = true;
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
		}
		finally {
			close();
		}
	}
	
	private boolean isActive(){
		if(socket.isInputShutdown() || socket.isOutputShutdown())
			active = false;
		return active;
	}
	
	private void close(){
		
		closeConnection();
		System.out.println("deregistro il client ");
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
			System.out.println("new msg to send");
			writer.writeObject(message);
			writer.flush();
		} catch (IOException e) {
			logger.error("Error in sending the new message ");
			logger.info("Stop the connection, active = false");
			logger.info(e);
			active = false;
			throw new IOException();
		}
	}
	
}
