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
import it.polimi.ingsw.ps42.model.effect.CouncilObtain;
import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.effect.ForEachObtain;
import it.polimi.ingsw.ps42.model.effect.IncreaseAction;
import it.polimi.ingsw.ps42.model.effect.IncreaseFamiliarsPoint;
import it.polimi.ingsw.ps42.model.effect.IncreaseSingleFamiliar;
import it.polimi.ingsw.ps42.model.effect.NoBonusInTower;
import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.resourcepacket.*;

public class CardBuilder {
	
	private Gson gson;
	private FileWriter writer;
	private BufferedWriter buffer;
	private Scanner scanner;
	private List<Obtain> councilConversion;
	
	public CardBuilder(String fileName) throws IOException {
		
		initGson(gson);
		writer=new FileWriter(fileName);
		buffer=new BufferedWriter(writer);
		scanner=new Scanner(System.in);
		
		councilConversion = askCouncilConversion();
	}
	
	private void initGson(Gson gson){
		
		GsonBuilder builder=new GsonBuilder().setPrettyPrinting();
		builder.registerTypeAdapter(Effect.class, new Serializer());
		
		gson=builder.create();
		
	}
	
	public void addCard() throws IOException {
		
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
		
		card.setPlayer(null);
		
		Card card2 = new Card("prova", "descrizione", CardColor.GREEN, 2, 2, null, null, null, null, null);
		card2.setPlayer(null);
		
		addCardToFile(card2);
	}
	
	public void close() throws IOException{
		
		buffer.close();
		scanner.close();
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
		while(response.toUpperCase().equals("SI")){
			
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
		while(response.toUpperCase().equals("SI")){
			
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
		while(response.toUpperCase().equals("SI")){
			
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
		while(response.toUpperCase().equals("SI"));
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
		case "FOR_EACH_OBTAIN":
			effect = askForEachObtain();
			break;
		case "INCREASE_ACTION":
			effect = askIncreaseAction();
			break;
		case "DO_ACTION":
			effect = askDoAction();
			break;	
		case "COUNCIL_OBTAIN":
			effect = askCouncilObtain();
			break;
		case "INCREASE_FAMILIARS":
			effect = askIncreaseFamiliars();
			break;
		case "INCREASE_SINGLE_FAMILIAR":
			effect = askIncreaseSingleFamiliar();
			break;
		case "NO_TOWER_BONUS":
			effect = new NoBonusInTower();
			break;
		default:
			System.out.println("tipo non valido");
			break;
		}
		return effect;
		
	}
	
	private Effect askIncreaseSingleFamiliar() {
		String response;
		int value;
		FamiliarColor color;
		System.out.println("Incremento familiare?");
		response = scanner.nextLine();
		value = Integer.parseInt(response);
		System.out.println("Colore del familiare?");
		response = scanner.nextLine();
		color = FamiliarColor.parseInput(response);
		
		return new IncreaseSingleFamiliar(value, color);
	}

	private Effect askIncreaseFamiliars() {

		String response;
		int value;
		System.out.println("Incremento familiare?");
		response = scanner.nextLine();
		value = Integer.parseInt(response);
		
		return new IncreaseFamiliarsPoint(value);
	}

	private Effect askCouncilObtain() {
		String response;
		int quantity;
		System.out.println("Quantità?");
		response = scanner.nextLine();
		quantity = Integer.parseInt(response);
		return new CouncilObtain(quantity, councilConversion);
	}

	private List<Obtain> askCouncilConversion(){
		
		System.out.println("--CREAZIONE CONVERSIONI PRIVILEGI DEL CONSIGLIO--");
		List<Obtain> conversion = new ArrayList<>();
		String response;
		do{
			Packet gain = askPacket();
			conversion.add(new Obtain( null, gain));
			System.out.println("Aggiungere altra conversione?");
			response = scanner.nextLine();
		}
		while(response.toUpperCase().equals("SI"));
		System.out.println("--FINE CREAZIONE LISTA CONVERSIONI PER PRIVILEGI DEL CONSIGLIO--");
		return conversion;
	}
	private Effect askDoAction() {
		
		String response;
		ActionType type;
		int actionLevel;
		Packet discount = new Packet();
		System.out.println("Tipo azione?");
		response = scanner.nextLine();
		type = ActionType.parseInput(response);
		System.out.println("Valore azione?");
		response = scanner.nextLine();
		actionLevel = Integer.parseInt(response);
		System.out.println("Aggiungere sconto? (si/no)");
		response = scanner.nextLine();
		if(response.toUpperCase().equals("SI") )
			discount = askPacket();
		return new IncreaseAction(type, actionLevel, discount);
	}

	private Effect askIncreaseAction() {
		
		String response;
		ActionType type;
		int value;
		Packet discount = new Packet();
		System.out.println("Tipo azione?");
		response = scanner.nextLine();
		type = ActionType.parseInput(response);
		System.out.println("Valore incremento?");
		response = scanner.nextLine();
		value = Integer.parseInt(response);
		System.out.println("Aggiungere sconto? (si/no)");
		response = scanner.nextLine();
		if(response.toUpperCase().equals("SI"))
			discount = askPacket();
		return new IncreaseAction(type, value, discount);
	}

	private Effect askForEachObtain() {
		
		Packet requirements = new Packet();
		Packet gains = new Packet();
		String response;
		System.out.println("Aggiungere requisiti? (si/no)");
		response = scanner.nextLine();
		if(response.toUpperCase().equals("SI"))
			requirements=askPacket();
		System.out.println("Aggiungere guadagni? (si/no)");
		response = scanner.nextLine();
		if(response.toUpperCase().equals("SI"))
			gains=askPacket();
		return new ForEachObtain(requirements, gains);
	}

	private Effect askObtain(){
		Packet costs = new Packet();
		Packet gains = new Packet();
		String response;
		System.out.println("Aggiungere costi? (si/no)");
		response = scanner.nextLine();
		if(response.toUpperCase().equals("SI"))
			costs=askPacket();
		System.out.println("Aggiungere guadagni? (si/no)");
		response = scanner.nextLine();
		if(response.toUpperCase().equals("SI"))
			gains=askPacket();
		return new Obtain(costs, gains);
	}
	private void addCardToFile(Card card) throws IOException{
		
		String parse = gson.toJson(card);
		System.out.println(parse);
		writer.append(parse);
	}
	

	
}
