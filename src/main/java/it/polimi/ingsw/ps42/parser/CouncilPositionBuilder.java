package it.polimi.ingsw.ps42.parser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

public class CouncilPositionBuilder {


	private Gson gson;
	private FileWriter writer;
	private BufferedWriter buffer;
	private Scanner scanner;
	
	
	public CouncilPositionBuilder( String fileName) throws IOException{
	
		initGson();
		writer = new FileWriter(fileName, true);
		buffer = new BufferedWriter(writer);
		scanner = new Scanner(System.in);	
	
	}
	
	private void initGson(){
		
		GsonBuilder builder=new GsonBuilder().serializeNulls().setPrettyPrinting();		
		this.gson = builder.create();
		
	}
	
	public void close() throws IOException{
		
		buffer.close();
		writer.close();
		scanner.close();
	}
	
	public void addConversion() throws IOException {
		
		Obtain conversion = askConversion();
		String parse = gson.toJson(conversion);
		buffer.append(parse);
	}
	
	private Obtain askConversion(){
		
		Packet costs = new Packet();
		Packet gains = new Packet();
		
		System.out.println("Aggiunta nuova conversione in corso...");
		gains=askPacket();
		return new Obtain(costs, gains);
	}
	
	private Packet askPacket(){
		String response;
		Packet packet=new Packet();
		do{
			System.out.println("Tipo Risorsa? ");
			System.out.println(Resource.FAITHPOINT.toString()+" "+Resource.MILITARYPOINT.toString()+" "
					+Resource.MONEY.toString()+" "+Resource.SLAVE.toString()+" "+Resource.STONE.toString()+" "
					+Resource.VICTORYPOINT.toString()+" "+Resource.WOOD.toString());
			String resource=scanner.nextLine();
			System.out.println("Quantità?" );
			int quantity = Integer.parseInt(scanner.nextLine());
			packet.addUnit(new Unit(Resource.parseInput(resource), quantity));
			System.out.println("Vuoi aggiungere altro?(si/no)");
			response=scanner.nextLine();
			System.out.println("stato attuale: "+packet);
		}
		while(response.toUpperCase().equals("SI"));
		return packet;
	}
	

}
