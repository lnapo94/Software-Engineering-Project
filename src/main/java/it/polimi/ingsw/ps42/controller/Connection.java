package it.polimi.ingsw.ps42.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;

import it.polimi.ingsw.ps42.message.GenericMessage;
import it.polimi.ingsw.ps42.message.Message;

public class Connection extends Observable implements Runnable{

	private Socket socket;
	private ObjectInputStream reader;
	private ObjectOutputStream writer;
	private boolean active;
	
	public Connection(Socket socket, ObjectInputStream reader, ObjectOutputStream writer) {
			this.socket = socket;
			this.reader = reader;
			this.writer = writer;
			this.active = true;
	}

	@Override
	public void run() {

		System.out.println("connection is tring to run");
		try{
			
			while(isActive()){
				Message message=(Message) reader.readObject();
				System.out.println("nuovo msg da inoltrare al controller da: "+ message.getPlayerID());
				setChanged();
				notifyObservers(message);
			}
		
		}
		catch (IOException | ClassNotFoundException e) {
			System.out.println("Error occurred in trasmission, closing Connection");
		}
		finally {
			close();
		}
	}
	
	private boolean isActive(){
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
			System.out.println("error in socket close");
		}
		active=false;
		
	}
	
	public void send(GenericMessage message) throws IOException{
		
		try {
			System.out.println("new msg to send");
			writer.writeObject(message);
			writer.flush();
		} catch (IOException e) {
			System.out.println("Error in sending the new message ");
			throw new IOException();
		}	
		finally {
			active = false;
		}
	}
	
}
