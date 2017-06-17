package it.polimi.ingsw.ps42.parser;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.GsonBuilder;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.effect.CardForEachObtain;
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
import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.resourcepacket.*;

public class CardBuilder extends Builder {
	

	public CardBuilder(String fileName, String councilConversionFile) throws IOException {
		
		super(fileName, councilConversionFile);
	}
	
	public CardBuilder(String fileName) throws IOException {
		
		super(fileName);
	}
	
	protected void initGson(){
		
		GsonBuilder builder=new GsonBuilder().serializeNulls().setPrettyPrinting();
		this.gson = builder.registerTypeAdapter(Effect.class, new Serializer()).create();
		
	}
	
	public void addCard() throws IOException {
		System.out.println("INIZIO PROCEDURA CREAZIONE NUOVA CARTA");
		
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
		
		addCardToFile(card);
		
		System.out.println("FINE PROCEDURA CREAZIONE CARTA");
		
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
		System.out.println(CardColor.BLUE.toString()+" "+CardColor.GREEN.toString()+" "
			+CardColor.VIOLET.toString()+" "+CardColor.YELLOW.toString());
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
			System.out.println("Aggiungere un altro costo?(si/no)");
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
			System.out.println("Aggiungere un altro requisito?(si/no)");
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
			System.out.println("Aggiungere un altro effetto " +effectType+ "?(si/no)");
			response = scanner.nextLine();	
		}
		return effects;
	}
	
	private Effect askEffect(){
		
		Effect effect = null;
		System.out.println("Tipo dell'Effetto? ");
		System.out.println(EffectType.OBTAIN.toString()+"\n "+EffectType.FOR_EACH_OBTAIN.toString()+"\n "
				+EffectType.INCREASE_ACTION.toString()+"\n "+EffectType.DO_ACTION+"\n "+ EffectType.COUNCIL_OBTAIN.toString()+
				"\n "+EffectType.INCREASE_FAMILIARS.toString()+"\n "+EffectType.INCREASE_SINGLE_FAMILIAR.toString()+"\n "+
				EffectType.NO_TOWER_BONUS.toString()+"\n "+EffectType.CARD_FOR_EACH_OBTAIN);
		String effectType=scanner.nextLine();
		switch (effectType.toUpperCase()) {
		case "OBTAIN":
			effect = askObtain();
			break;
		case "FOR_EACH_OBTAIN":
			effect = askForEachObtain();
			break;
		case "CARD_FOR_EACH_OBTAIN":
			effect = askCardForEachObtain();
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
		System.out.println(FamiliarColor.ALL+"\n "+FamiliarColor.BLACK+"\n "+FamiliarColor.NEUTRAL+"\n "+FamiliarColor.ORANGE+
							"\n "+FamiliarColor.WHITE);
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
		System.out.println("Quantità di benefici del consiglio?");
		response = scanner.nextLine();
		quantity = Integer.parseInt(response);
		return new CouncilObtain(quantity, councilConversion);
	}


	private Effect askDoAction() {
		
		String response;
		ActionType type;
		int actionLevel;
		Packet discount = new Packet();
		System.out.println("Tipo azione bonus?");
		System.out.println(ActionType.COUNCIL+"\n "+ActionType.MARKET+"\n "+ActionType.PRODUCE+"\n "+ActionType.TAKE_ALL+"\n "
							+ActionType.TAKE_BLUE+"\n "+ActionType.TAKE_GREEN+"\n "+ActionType.TAKE_VIOLET+"\n "+ActionType.TAKE_YELLOW+
							"\n "+ActionType.YIELD);
		response = scanner.nextLine();
		type = ActionType.parseInput(response);
		System.out.println("Valore azione bonus?");
		response = scanner.nextLine();
		actionLevel = Integer.parseInt(response);
		System.out.println("Aggiungere sconto all'azione? (si/no)");
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
		System.out.println("Tipo azione da incrementare?");
		System.out.println(ActionType.COUNCIL+"\n "+ActionType.MARKET+"\n "+ActionType.PRODUCE+"\n "+ActionType.TAKE_ALL+"\n "
							+ActionType.TAKE_BLUE+"\n "+ActionType.TAKE_GREEN+"\n "+ActionType.TAKE_VIOLET+"\n "+ActionType.TAKE_YELLOW+
							"\n "+ActionType.YIELD);
		response = scanner.nextLine();
		type = ActionType.parseInput(response);
		System.out.println("Valore incremento dell'azione?");
		response = scanner.nextLine();
		value = Integer.parseInt(response);
		System.out.println("Aggiungere sconto all'azione incrementata? (si/no)");
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

	private Effect askCardForEachObtain(){
		
		CardColor color;
		Packet gains;
		System.out.println("Colore della carta?");
		System.out.println(CardColor.BLUE.toString()+"\n "+CardColor.GREEN.toString()+"\n "
			+CardColor.VIOLET.toString()+"\n "+CardColor.YELLOW.toString()+ "\n "+CardColor.ALL);
		color = CardColor.parseInput(scanner.nextLine());
		System.out.println("Guadagni per la carta selezionata:");
		gains = askPacket();
		
		return new CardForEachObtain(color, gains);
	}
	private Effect askObtain(){
		Packet costs = new Packet();
		Packet gains = new Packet();
		CouncilObtain councilObtain = null;
		String response;
		System.out.println("Aggiungere costi? (si/no)");
		response = scanner.nextLine();
		if(response.toUpperCase().equals("SI"))
			costs=askPacket();
		System.out.println("Aggiungere guadagni? (si/no)");
		response = scanner.nextLine();
		if(response.toUpperCase().equals("SI"))
			gains=askPacket();

		System.out.println("Aggiungere privilegio del consiglio?");
		response = scanner.nextLine();
		if(response.toUpperCase().equals("SI")){
			System.out.println("Quantità di Privilegi del consiglio?");
			int quantity = scanner.nextInt();
			councilObtain = new CouncilObtain(quantity, councilConversion);
		}
		
		return new Obtain(costs, gains, councilObtain);
	}
	
	private void addCardToFile(Card card) throws IOException{
		
		String parse = gson.toJson(card);
		buffer.append(parse);
	}

	public static void main(String[] args) {
		
		CardBuilder builder;
		Scanner scanner = new Scanner(System.in);
		try {
			builder = new CardBuilder("BlueCardsSecondPeriod.json", "Resource//Position//CouncilPosition//CouncilConvertion.json");
			String response;
			int i =0;
			do{
				builder.addCard();
				i++;
				System.out.println("Aggiungere un'altra carta?(si/no) ["+i+" carte aggiunte]");
				response = scanner.nextLine();
			}
			while(response.toUpperCase().equals("SI"));
			builder.close();
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}
}
