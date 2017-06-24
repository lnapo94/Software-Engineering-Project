package it.polimi.ingsw.ps42.view;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

import it.polimi.ingsw.ps42.message.LoginMessage;
import it.polimi.ingsw.ps42.message.Message;
import it.polimi.ingsw.ps42.message.PlayersListMessage;

public class ClientSocket extends Observable implements Observer{

	private Socket socket;
	private ObjectInputStream reader;
	private ObjectOutputStream writer;
	private final static int PORT = 5555;
	private boolean isConnected = false;
	
	public ClientSocket(String host) throws UnknownHostException, IOException {
		
		this.socket = new Socket(host, PORT);
		writer = new ObjectOutputStream(socket.getOutputStream());
		writer.flush();
		reader = new ObjectInputStream(socket.getInputStream());
		isConnected = true;
	}
	
	public void addView(View view){

		view.addObserver(this);
		this.addObserver(view);
	}
	
	public void send(Message message) throws IOException{
		
		try {
			writer.writeObject(message);
			writer.flush();
		} catch (IOException e) {
			System.out.println("Error in sending the new message to: "+message.getPlayerID() );
			isConnected = false;
			throw new IOException();
		}	
	}
	
	public void send(LoginMessage message) throws IOException{
		
		try {
			writer.writeObject(message);
			writer.flush();
		} catch (IOException e) {
			System.out.println("Error in sending the new message to: " );
			isConnected = false;
			throw new IOException();
		}	
	}
	
	public boolean isConnected(){
		if(socket.isClosed())
			isConnected=false;
		return isConnected;
	}
	
	public void readMessage() {
		
		if(isConnected()){
			try{
				Object msg = reader.readObject();
				if(msg instanceof Message) {
					setChanged();
					notifyObservers((Message)msg);
				}
				else if(msg instanceof LoginMessage){
					setChanged();
					notifyObservers((LoginMessage)msg);
				}
				else if(msg instanceof PlayersListMessage){
					setChanged();
					notifyObservers((PlayersListMessage)msg);
				}
			}
			catch(IOException | ClassNotFoundException e){
				System.out.println("errore nella lettura dei messaggi ricevuti");
				e.printStackTrace();
				isConnected=false;
			}
		}
	}
	
	public void close() throws IOException {
		
		reader.close();
		writer.close();
	}
	
	public void startReading(){
		new Thread( new Runnable(){
			
				public void run(){
					System.out.println("inizio thread dal socket");
					while(isConnected()){
						readMessage();
					}
					
				}
			
		}).start();
			
	}
	@Override
	public void update(Observable sender, Object message) {

		if(message instanceof Message){
			Message msg = (Message) message;
			try {
				this.send(msg);
			} catch (IOException e) {
				
			}
		}
		else if (message instanceof LoginMessage){
			try {
				this.send((LoginMessage)message);
			} catch (IOException e) {
				System.out.println("Unable to read the message");
			}
		}
	}
	
	
	public static void main(String[] args) {
		
		String host = "localhost"; 
		try {
			ClientSocket client = new ClientSocket(host);
			View view = new TerminalView();
			client.addView(view);
			view.askNewPlayerID();
			client.startReading();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
