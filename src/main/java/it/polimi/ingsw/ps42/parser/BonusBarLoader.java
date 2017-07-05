package it.polimi.ingsw.ps42.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;

import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.player.BonusBar;

/**
 * Loader for the BonusBar Gson File
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class BonusBarLoader extends Loader {

	/**
	 * Constructor for the BonusBar Loader 
	 * @param fileName the file path to the Bonus Bar Gson File
	 * @throws IOException if a problem occurs in file opening
	 */
	public BonusBarLoader(String fileName) throws IOException {
		
		super(fileName);
	}
	
	/**
	 * Private method used to pesonalize the Gson Loader, uses an Adapter for the Effect class
	 */
	@Override
	protected void initGson() {
		
		GsonBuilder builder = new GsonBuilder().registerTypeAdapter(Effect.class, new Serializer());
		gson = builder.create();
		parser = new JsonStreamParser(buffer);
	}

	/**
	 * Getter for the list of BonusBar loaded from file
	 * @return the List of BonusBar in the File
	 */
	public List<BonusBar> getBonusBars(){
		
		ArrayList<BonusBar> bonusBarList = new ArrayList<>();
		
		while(parser.hasNext()){
			
			JsonElement element = parser.next();
			if(element.isJsonObject())	
				bonusBarList.add( gson.fromJson(element, BonusBar.class));
			
		}
		
		return bonusBarList;
	}
	

	
	
}
