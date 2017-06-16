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

public class LeaderCardLoader extends Loader{

	private List<LeaderCard> deck;
	
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
	
	@Override
	protected void initGson() {
		
		GsonBuilder builder = new GsonBuilder().registerTypeAdapter(Effect.class, new Serializer());
		gson = builder.create();
		parser = new JsonStreamParser(buffer);
		
	}
	
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
	
	public static void main(String[] args) {
		
		LeaderCardLoader loader;
		
		try {
			loader = new LeaderCardLoader("LeaderCards.json");
			List<LeaderCard> deck = loader.getLeaderCards();
			System.out.println(deck.size());
			loader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
