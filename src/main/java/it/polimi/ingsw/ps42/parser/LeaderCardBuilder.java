package it.polimi.ingsw.ps42.parser;

import java.io.IOException;

import javax.jws.soap.SOAPBinding.ParameterStyle;

import com.google.gson.GsonBuilder;

import it.polimi.ingsw.ps42.model.effect.CanPositioningEverywhereLeader;
import it.polimi.ingsw.ps42.model.effect.CouncilObtain;
import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.effect.NoMoneyMalusLeader;
import it.polimi.ingsw.ps42.model.effect.Obtain;
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
		this.gson = builder.registerTypeAdapter(Effect.class, new Serializer()).create();
		
	}
	
	public void addLeaderCard() throws IOException{
		
		String name = askName();
		String description = askDescription();
		LeaderRequirements requirements = askRequirements();
		Effect onceARoundEffect = askOnceARoundEffect();
		Effect permanentEffect = askPermanentEffect();
		Obtain obtain = askObtain();
		
		LeaderCard card = new LeaderCard(name, description, requirements, onceARoundEffect, permanentEffect, obtain);
		
		//Serialize the LeaderCard and write it down on the file
		String parse = gson.toJson(card);
		buffer.append(parse);
		
	}
	
	private LeaderRequirements askRequirements(){
		
		System.out.println("Aggiunta Requisiti attivazione della carta");
		System.out.println("Requisiti di risorse:");
		Packet resourceRequirements = askPacket();
		CardColor color = null;
		int cardRequirements = 0;
		System.out.println("aggiungere requisito di Carte?(si/no)");
		String response = scanner.nextLine();
		if(response.toUpperCase().equals("SI"));{
			
			System.out.println("Colore della carta?");
			System.out.println(CardColor.BLUE.toString()+" "+CardColor.GREEN.toString()+" "
					+CardColor.VIOLET.toString()+" "+CardColor.YELLOW.toString());
			color = CardColor.parseInput(scanner.nextLine());
			
			System.out.println("Quantità?");
			cardRequirements = Integer.parseInt(scanner.nextLine());
		}
		return new LeaderRequirements(resourceRequirements, color, cardRequirements);
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
		System.out.println(EffectType.OBTAIN.toString()+"\n "+EffectType.FOR_EACH_OBTAIN.toString()+"\n "
				+EffectType.INCREASE_ACTION.toString()+"\n "+EffectType.DO_ACTION+"\n "+ EffectType.COUNCIL_OBTAIN.toString()+
				"\n "+EffectType.INCREASE_FAMILIARS.toString()+"\n "+EffectType.INCREASE_SINGLE_FAMILIAR.toString()+"\n "+
				EffectType.NO_TOWER_BONUS.toString()+"\n "+EffectType.CARD_FOR_EACH_OBTAIN);
		String effectType=scanner.nextLine();
		switch (effectType.toUpperCase()){
		
		case "DO_ACTION":
			effect = askDoAction();
			break;
		case "CAN_POSITIONING_EVERYWHERE":
			effect = new CanPositioningEverywhereLeader();
			break();
		case "NO_MONEY_MALUS":
			effect = new NoMoneyMalusLeader();
			break;
		case "INCREASE_SINGLE_FAMILIAR":
			effect = askIncreaseSingleFamiliar();
		
		}
		
		return effect;
	}
	
	private Obtain askObtain(){
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
	
}
