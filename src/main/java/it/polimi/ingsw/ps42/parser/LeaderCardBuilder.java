package it.polimi.ingsw.ps42.parser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import com.google.gson.GsonBuilder;

import it.polimi.ingsw.ps42.model.effect.CanPositioningEverywhereLeader;
import it.polimi.ingsw.ps42.model.effect.CouncilObtain;
import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.effect.FiveMoreVictoryPointLeader;
import it.polimi.ingsw.ps42.model.effect.NoMilitaryRequirementsLeader;
import it.polimi.ingsw.ps42.model.effect.NoMoneyMalusLeader;
import it.polimi.ingsw.ps42.model.effect.SetAllFamiliarsLeader;
import it.polimi.ingsw.ps42.model.effect.SetSingleFamiliarLeader;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.leaderCard.LeaderCard;
import it.polimi.ingsw.ps42.model.leaderCard.LeaderRequirements;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;

public class LeaderCardBuilder extends CardBuilder{

	
	public LeaderCardBuilder(String fileName, String councilConversionFile) throws IOException {
		
		super(fileName, councilConversionFile);
	}

	@Override
	protected void initGson() {

		GsonBuilder builder=new GsonBuilder().serializeNulls().setPrettyPrinting();
		this.gson = builder.enableComplexMapKeySerialization().registerTypeAdapter(Effect.class, new Serializer()).create();
		
	}
	
	@Override
	public void addCard() throws IOException{
		
		String name = askName();
		String description = askDescription();
		LeaderRequirements requirements = askRequirements();
		Effect onceARoundEffect = askOnceARoundEffect();
		Effect permanentEffect = askPermanentEffect();
		CouncilObtain obtain = null;
		System.out.println("Aggiungere privilegi del consiglio?(si/no)");
		String response = scanner.nextLine();
		if(response.toUpperCase().equals("SI"))
			obtain = askCouncilObtain();
		LeaderCard card = new LeaderCard(name, description, requirements, onceARoundEffect, permanentEffect, obtain);
		
		//Serialize the LeaderCard and write it down on the file
		String parse = gson.toJson(card);
		buffer.append(parse);
		
	}
	
	private LeaderRequirements askRequirements(){
		
		System.out.println("Aggiunta Requisiti attivazione della carta");
		System.out.println("Requisiti di risorse?(si/no)");
		String response = scanner.nextLine();
		Packet resourceRequirements= null;
		
		if(response.toUpperCase().equals("SI"))
			resourceRequirements = askPacket();
		
		CardColor color;
		int cardRequirement;
		
		HashMap<CardColor, Integer> cardRequirements = new HashMap<>();
		
		System.out.println("aggiungere requisito di Carte?(si/no)");
		response = scanner.nextLine();
		while(!response.toUpperCase().equals("NO")){
			
			System.out.println("Colore della carta?");
			System.out.println(CardColor.BLUE.toString()+" "+CardColor.GREEN.toString()+" "
					+CardColor.VIOLET.toString()+" "+CardColor.YELLOW.toString());
			color = CardColor.parseInput(scanner.nextLine());
			
			System.out.println("Quantità?");
			cardRequirement = Integer.parseInt(scanner.nextLine());
			
			cardRequirements.put(color, cardRequirement);
			
			System.out.println("aggiungere un altro requisito di Carte?(si/no)");
			response = scanner.nextLine();
		}
		return new LeaderRequirements(resourceRequirements, cardRequirements);
	}
	
	private Effect askOnceARoundEffect(){
		System.out.println("Aggiungere un effetto una volta per turno?(si/no)");
		String response = scanner.nextLine();
		Effect effect = null;
		if(response.toUpperCase().equals("SI")){
			effect = askEffect();
		}
		return effect;
	}
	
	private Effect askPermanentEffect(){
		
		System.out.println("Aggiungere un effetto permanente?(si/no)");
		String response = scanner.nextLine();
		Effect effect = null;
		if(response.toUpperCase().equals("SI")){
			effect = askEffect();
		}
		return effect;
	}
	
	@Override
	protected Effect askEffect() {
	
		Effect effect = null;
		System.out.println("Tipo dell'Effetto? ");
		System.out.println(EffectType.OBTAIN.toString()+"\n "+EffectType.CAN_POSITIONING_EVERYWHERE.toString()+"\n "
				+EffectType.INCREASE_ACTION.toString()+"\n "+EffectType.DO_ACTION+"\n "+ EffectType.NO_MONEY_MALUS.toString()+
				"\n "+EffectType.SET_SINGLE_FAMILIAR_LEADER.toString()+"\n "+EffectType.INCREASE_SINGLE_FAMILIAR.toString()+"\n "+
				EffectType.FIVE_MORE_VICTORY_POINT.toString()+"\n "+EffectType.NO_MILITARY_REQUIREMENTS+"\n"+EffectType.SET_ALL_FAMILIARS_LEADER);
		String effectType=scanner.nextLine();
		switch (effectType.toUpperCase()){
		
		case "DO_ACTION":
			effect = askDoAction();
			break;
		case "CAN_POSITIONING_EVERYWHERE":
			effect = new CanPositioningEverywhereLeader();
			break;
		case "NO_MONEY_MALUS":
			effect = new NoMoneyMalusLeader();
			break;
		case "INCREASE_SINGLE_FAMILIAR":
			effect = askIncreaseSingleFamiliar();
			break;
		case "OBTAIN":
			effect = askObtain();
			break;
		case "SET_FAMILIAR":
			effect = askSetFamiliar();
			break;
		case "NO_MILITARY_REQUIREMENTS":
			effect = new NoMilitaryRequirementsLeader();
			break;
		case "FIVE_MORE_VICTORY_POINT":
			effect = new FiveMoreVictoryPointLeader();
			break;
		case "COPY_EFFECT":
			effect = null;
			break;
		case "FOR_EACH":
			effect = null;
			break;
		case "SET_ALL_FAMILIARS_LEADER":
			effect = askSetAllFamiliarsLeader();
			break;
		}
		
		return effect;
	}
	
	private Effect askSetFamiliar(){
		
		System.out.println("Incremento del familiare");
		int value = Integer.parseInt(scanner.nextLine());
		return new SetSingleFamiliarLeader(value);
	}
	
	
	private Effect askSetAllFamiliarsLeader() {
		int value;
		System.out.println("Valore?");
		value = Integer.parseInt(scanner.nextLine());
		return new SetAllFamiliarsLeader(value);
	}
	
	public static void main(String[] args) {
		
		LeaderCardBuilder builder;
		Scanner scanner = new Scanner(System.in);
		try {
			builder = new LeaderCardBuilder("Resource//LeaderCards//leaderCards.json", "Resource//Position//CouncilPosition//CouncilConvertion.json");
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
		}
		
		
		
	}
}
