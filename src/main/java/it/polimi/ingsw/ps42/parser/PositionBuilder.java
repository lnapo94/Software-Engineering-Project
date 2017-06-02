package it.polimi.ingsw.ps42.parser;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.position.Position;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;

public class PositionBuilder extends Builder {

	
	public PositionBuilder( String fileName, String councilConversionFile) throws IOException {
		
		super(fileName, councilConversionFile);
	}
	
	protected void initGson(){
		
		GsonBuilder builder=new GsonBuilder().serializeNulls().setPrettyPrinting();
		
		this.gson = builder.registerTypeAdapter(Position.class, new Serializer()).create();
		
	}
	
	public void close() throws IOException{
		
		buffer.close();
		writer.close();
		scanner.close();
	}
	
	public void addPosition() throws IOException {
		
		ActionType type = askActionType();
		int level = askLevel();
		Obtain bonus = askBonus();
		int malus = askMalus();
		
		
	}
	
	private ActionType askActionType(){
		
		return null;
	}
	
	private int askLevel(){
		System.out.println("inserisci il livello della posizione");
		return Integer.parseInt(scanner.nextLine());
	}
		
	private int askMalus(){
		System.out.println("inserisci il malus della posizione");
		return Integer.parseInt(scanner.nextLine());
	}	
	
	private Obtain askBonus(){
		
		Packet costs = new Packet();
		Packet gains = new Packet();
		String response;

		System.out.println("Aggiungere guadagni? (si/no)");
		response = scanner.nextLine();
		if(response.toUpperCase().equals("SI"))
			gains=askPacket();
		return new Obtain(costs, gains);
	}
}
