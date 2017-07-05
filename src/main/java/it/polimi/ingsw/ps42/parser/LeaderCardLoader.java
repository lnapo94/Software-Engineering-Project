package it.polimi.ingsw.ps42.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;

import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.leaderCard.LeaderCard;

/**
 * Loader for the Leader Cards from json file
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class LeaderCardLoader extends Loader{

	private List<LeaderCard> deck;
	
	/**
	 * Constructor for the LeaderCard loader given the path to the json file of cards
	 * @param fileName the path to the json file
	 * @throws IOException  if any problem in File opening occurs
	 */
	public LeaderCardLoader( String fileName) throws IOException{
	
		super(fileName);
		
		//Load all the cards from file
		deck = new ArrayList<>();
			
			while(parser.hasNext()){
				
				JsonElement element = parser.next();
				if(element.isJsonObject())
					deck.add(gson.fromJson(element , LeaderCard.class));
			}
	}
	
	/**
	 * Private method used to personalize Gson loader, Adapter for HashMap  and Effect class is used
	 */
	@Override
	protected void initGson() {
		
		GsonBuilder builder = new GsonBuilder().enableComplexMapKeySerialization().registerTypeAdapter(Effect.class, new Serializer());
		gson = builder.create();
		parser = new JsonStreamParser(buffer);
		
	}
	
	/**
	 * Getter for the List of Leader Cards loaded from file
	 * @return the List of LeaderCard loaded
	 */
	public List<LeaderCard> getLeaderCards(){
		
		int index;
		ArrayList<LeaderCard> result = new ArrayList<>();
		for(int i=0; i<4; i++){
			//Get a random index and add the relative card to the result Array
			index = new Random().nextInt(deck.size());
			result.add(deck.remove(index));
		
		}
		
		return result;
			
	}
	
}
