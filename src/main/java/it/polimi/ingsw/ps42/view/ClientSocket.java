package it.polimi.ingsw.ps42.view;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import it.polimi.ingsw.ps42.message.Message;

public class ClientSocket extends Observable implements Observer{

	private Socket socket;
	private ObjectInputStream reader;
	private ObjectOutputStream writer;
	private final static int PORT = 12345;
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
	
	public void send(Message message){
		
		try {
			writer.writeObject(message);
			writer.flush();
		} catch (IOException e) {
			System.out.println("Error in sending the new message to: "+message.getPlayerID() );
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
				Message msg = (Message)reader.readObject();
				setChanged();
				notifyObservers(msg);
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
			this.send(msg);
		}
		else{
			System.out.println("Wrong message type");
		}
	}
	
	
	public static void main(String[] args) {
		
		String host = "localhost"; 
		try {
			ClientSocket client = new ClientSocket(host);
			View view = new TerminalView();
			view.addPlayer("primoGiocatore2");
			client.addView(view);
			List<String> playersID = new ArrayList<String>();
			playersID.add("primoGiocatore1");
			playersID.add("primoGiocatore2");
			view.createTable(playersID);
			client.startReading();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
