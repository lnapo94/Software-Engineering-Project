package it.polimi.ingsw.ps42.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.effect.Effect;

/**
 * Loader for the Card File
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class CardLoader extends Loader {

	
	/**
	 * Constructor for the Card Loader, requires a file name to a json file of Card
	 * @param fileName the path to the json file of Card
	 * @throws IOException if problems in file opening occurs
	 */
	public CardLoader(String fileName) throws IOException {
		
		super(fileName);
	}
	
	/**
	 * Private method for Gson loader personalization, use an Adapter for Effect class
	 */
	protected void initGson(){
		
		GsonBuilder builder = new GsonBuilder().registerTypeAdapter(Effect.class, new Serializer());
		gson = builder.create();
		parser = new JsonStreamParser(buffer);
		
	}
	
	/**
	 * Getter for the List of Card read from json File
	 * @return the List of Card from the File
	 */
	public List<Card> getCards(){
		
		ArrayList<Card> deck = new ArrayList<>();
		
		while(parser.hasNext()){
			
			JsonElement element = parser.next();
			if(element.isJsonObject())
				deck.add(gson.fromJson(element , Card.class));
		}
		
		return deck;
	}

}
