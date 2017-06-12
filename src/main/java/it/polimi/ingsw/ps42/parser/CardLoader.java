package it.polimi.ingsw.ps42.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.effect.Effect;

public class CardLoader extends Loader {

	
	public CardLoader(String fileName) throws IOException {
		
		super(fileName);
	}
	
	protected void initGson(){
		
		GsonBuilder builder = new GsonBuilder().registerTypeAdapter(Effect.class, new Serializer());
		gson = builder.create();
		parser = new JsonStreamParser(buffer);
		
	}
	
	public List<Card> getCards(){
		
		ArrayList<Card> deck = new ArrayList<>();
		
		while(parser.hasNext()){
			
			JsonElement element = parser.next();
			if(element.isJsonObject())
				deck.add(gson.fromJson(element , Card.class));
		}
		
		return deck;
	}

	public static void main(String[] args) {
		
		CardLoader loader;
		
		try {
			loader = new CardLoader("greenCardsFirstPeriod.json");
			List<Card> deck = loader.getCards();
			System.out.println(deck.size());
			loader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
