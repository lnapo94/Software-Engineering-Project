package it.polimi.ingsw.ps42.view;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

import it.polimi.ingsw.ps42.message.GenericMessage;

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
	
	public void send(GenericMessage message) throws IOException{
		
		try {
			writer.writeObject(message);
			writer.flush();
		} catch(EOFException e) {
			System.out.println("EOF reached");
		}
		catch (IOException e) {
			System.out.println("Error in sending the new message ");
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
				setChanged();
				notifyObservers(msg);
			} catch(EOFException e) {
				System.out.println("EOF reached");
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

		if(message instanceof GenericMessage){
			GenericMessage msg = (GenericMessage) message;
			try {
				this.send(msg);
			} catch (IOException e) {
				System.out.println("Unknown type of message");
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
