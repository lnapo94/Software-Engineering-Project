package it.polimi.ingsw.ps42.parser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;

public class CardBuilder {
	
	Gson gson;
	FileWriter writer;
	BufferedWriter buffer;
	
	public CardBuilder(String fileName) throws IOException {
		
		initGson(gson);
		writer=new FileWriter(fileName);
		buffer=new BufferedWriter(writer);
	
	}
	
	private void initGson(Gson gson){
		
		GsonBuilder builder=new GsonBuilder().setPrettyPrinting();
		builder.registerTypeAdapter(Effect.class, new Serializer());
		
		gson=builder.create();
	}
	
	public void addCard(){
		
		String name = askName();
		String description = askDescription();
		CardColor color = askColor();
		int period = askPeriod();
		int level = askLevel();
		List<Packet> costs = askCosts();
		List<Packet> requirements = askRequirements();
		List<Effect> immediateEffects = askImmediateEffect();
		List<Effect> permanentEffect = askPermanentEffect();
		List<Effect> finalEffect = askFinalEffect();
		
	}
	
	private String askName(){
		
		System.out.println("inserisci il nome della carta");
		
	}
	
	private String askDesccription(){
		
	}
	
	private CardColor askCardColor(){
		
	}
	
	private int askPeriod(){
		
	}
	
	private int askLevel(){
		
	}
	
	private List<Packet> askCosts(){
		
	}
	
	private List<Packet> askRequirements(){
		
	}
	
	private List<Effect> askImmediateEffect(){
		
	}
	
	private List<Effect> askPermanentEffect(){
		
	}
	
	private List<Effect> askFinalEffect(){
		
	}
	
	
	private void addCardToFile(Card card){
		
		gson.toJson(card, buffer);
	}
	
}
