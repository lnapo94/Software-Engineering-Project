package it.polimi.ingsw.ps42.parser;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.resourcepacket.*;

public class CardBuilder {
	
	Gson gson;
	FileWriter writer;
	BufferedWriter buffer;
	Scanner scanner;
	
	public CardBuilder(String fileName) throws IOException {
		
		initGson(gson);
		writer=new FileWriter(fileName);
		buffer=new BufferedWriter(writer);
		scanner=new Scanner(System.in);
	}
	
	private void initGson(Gson gson){
		
		GsonBuilder builder=new GsonBuilder().setPrettyPrinting();
		builder.registerTypeAdapter(Effect.class, new Serializer());
		
		gson=builder.create();
	}
	
	public void addCard(){
		
		String name = askName();
		String description = askDescription();
		CardColor color = askCardColor();
		int period = askPeriod();
		int level = askLevel();
		List<Packet> costs = askCosts();
		List<Packet> requirements = askRequirements();
		List<Effect> immediateEffects = askEffects("immediato");
		List<Effect> permanentEffect = askEffects("permanente");
		List<Effect> finalEffect = askEffects("finale");
		
		Card card = new Card(name, description, color, period, level, costs, 
				immediateEffects, requirements, permanentEffect, finalEffect );
		addCardToFile(card);
	}
	
	private String askName(){
		
		System.out.println("inserisci il nome della carta");
		return scanner.nextLine();
	}
	
	private String askDescription(){

		System.out.println("inserisci descrizione della carta");
		return scanner.nextLine();
	}
	
	private CardColor askCardColor(){
		
		System.out.println("inserisci colore della carta");
		return CardColor.parseInput(scanner.nextLine());
	}
	
	private int askPeriod(){

		System.out.println("inserisci il periodo della carta");
		return Integer.parseInt(scanner.nextLine());
		
	}
	
	private int askLevel(){

		System.out.println("inserisci il livello della carta");
		return Integer.parseInt(scanner.nextLine());
	}
	
	private List<Packet> askCosts(){
		String response;
		ArrayList<Packet> costs=new ArrayList<>();
		System.out.println("Creare nuovo costo?(si/no)");
		response = scanner.nextLine();
		while(response.toUpperCase()!="NO"){
			
			costs.add(askPacket());
			System.out.println("Creare nuovo costo?(si/no)");
			response = scanner.nextLine();	
		}
		return costs;
	}
	
	private List<Packet> askRequirements(){
		String response;
		ArrayList<Packet> requirements = new ArrayList<>();
		System.out.println("Creare nuovo requisito?(si/no)");
		response = scanner.nextLine();
		while(response.toUpperCase()!="NO"){
			
			requirements.add(askPacket());
			System.out.println("Creare nuovo requisito?(si/no)");
			response = scanner.nextLine();	
		}
		return requirements;
	}
	
	private List<Effect> askEffects(String effectType){
		
		ArrayList<Effect> effects = new ArrayList<>();
		String response;
		System.out.println("Creare nuovo effetto " +effectType+ "? (si/no)");
		response = scanner.nextLine();
		while(response.toUpperCase()!="NO"){
			
			effects.add(askEffect());
			System.out.println("Creare nuovo effetto " +effectType+ "?(si/no)");
			response = scanner.nextLine();	
		}
		return effects;
	}
	
	private Packet askPacket(){
		String response;
		Packet packet=new Packet();
		do{
			System.out.println("Tipo Risorsa? ");
			String resource=scanner.nextLine();
			System.out.println("Quantità?" );
			int quantity = Integer.parseInt(scanner.nextLine());
			packet.addUnit(new Unit(Resource.parseInput(resource), quantity));
			System.out.println("Vuoi aggiungere altro?(si/no)");
			response=scanner.nextLine();
			System.out.println("stato attuale: "+packet);
		}
		while(response.toUpperCase()!="NO");
		return packet;
	}
	
	private Effect askEffect(){
		
		Effect effect = null;
		System.out.println("Tipo ? ");
		String effectType=scanner.nextLine();
		switch (effectType.toUpperCase()) {
		case "OBTAIN":
			effect = askObtain();
			break;

		default:
			System.out.println("tipo non valido");
			break;
		}
		return effect;
		
	}
	
	private Obtain askObtain(){
		
		return null;
	}
	private void addCardToFile(Card card){
		
		gson.toJson(card, buffer);
	}
	
}
