package it.polimi.ingsw.ps42.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import it.polimi.ingsw.ps42.message.GenericMessage;
import it.polimi.ingsw.ps42.view.TerminalView;
import it.polimi.ingsw.ps42.view.View;

public class ClientSocket extends Observable implements Observer{

	private Socket socket;
	private ObjectInputStream reader;
	private ObjectOutputStream writer;
	private final static int PORT = 5555;
	private boolean isConnected = false;
	
	//Logger
	private transient Logger logger = Logger.getLogger(ClientSocket.class);
	
	public ClientSocket(String host) throws UnknownHostException, IOException {
		
		this.socket = new Socket(host, PORT);
		writer = new ObjectOutputStream(socket.getOutputStream());
		writer.flush();
		reader = new ObjectInputStream(socket.getInputStream());
		isConnected = true;
		
		//Setting client logger property
		PropertyConfigurator.configure("Logger//Properties//client_log.properties");
	}
	
	public void addView(View view){

		view.addObserver(this);
		this.addObserver(view);
	}
	
	public void send(GenericMessage message) throws IOException{
		
		try {
			writer.writeObject(message);
			writer.flush();
		}catch (IOException e) {
			logger.error("Error in sending the new message ");
			logger.info(e);
			isConnected = false;
			throw new IOException();
		}	
	}
	
	public boolean isConnected(){
		if(socket.isInputShutdown() || socket.isOutputShutdown())
			isConnected=false;
		return isConnected;
	}
	
	public void readMessage() {
		
		if(isConnected()){
			try{
				GenericMessage msg = (GenericMessage) reader.readObject();
				setChanged();
				notifyObservers(msg);
			} catch(IOException | ClassNotFoundException e){
				logger.error("Error in reading the input messages");
				logger.info(e);
				isConnected=false;
			}
		}
	}
	
	public void close() throws IOException {
		logger.info("Closing the reader/writer for the socket");
		reader.close();
		writer.close();
	}
	
	public void startReading(){
		new Thread( new Runnable(){
			
				public void run(){
					logger.info("Starting Connection Thread in ClientSocket");
					while(isConnected()){
						readMessage();
					}
					
				}
			
		}).start();
			
	}
	@Override
	public void update(Observable sender, Object message) {

		if(message instanceof GenericMessage){
			GenericMessage msg = (GenericMessage) message;
			try {
				this.send(msg);
			} catch (IOException e) {
				logger.error("Unknown type of message");
				logger.info(e);
			}
		}
		
	}
	
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		
		String host = "localhost"; 

		ClientSocket client = new ClientSocket(host);
		View view = new TerminalView();
		client.addView(view);
		view.askNewPlayerID();
		client.startReading();
	}

}
