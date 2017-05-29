package it.polimi.ingsw.ps42.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.effect.Effect;

public class CardLoader {

	private Gson gson;
	private FileReader reader;
	private BufferedReader buffer;
	private JsonStreamParser parser;
	
	public CardLoader(String fileName) throws IOException {
		
		reader = new FileReader(fileName);
		buffer = new BufferedReader(reader);
		initGson();
	}
	
	private void initGson(){
		
		GsonBuilder builder = new GsonBuilder().registerTypeAdapter(Effect.class, new Serializer());
		gson = builder.create();
		parser = new JsonStreamParser(buffer);
		
	}
	
	public void setFileName(String fileName) throws IOException{
		
		reader = new FileReader(fileName);
		buffer = new BufferedReader(reader);
		initGson();
		
	}
	
	public void close() throws IOException {
		
		buffer.close();
		reader.close();
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
	
}
