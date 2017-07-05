package it.polimi.ingsw.ps42.parser;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;

import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.exception.ElementNotFoundException;

/**
 * Class used to load the Ban File
 * 
 * @author Luca Napoletano, Claudio Montanari
 */
public class BanLoader extends Loader{
	
	private int indexOfBan;

	/**
	 * The Basic Constructor, loads bans from the json file path passed
	 * @param fileName the path to the json ban file
	 * @throws IOException if error in file opening occurs
	 */
	public BanLoader(String fileName) throws IOException {
		
		super(fileName);
	}
	
	/**
	 * Private method to define the proper Gson loader, uses an Adapter for the Effect class
	 */
	@Override
	protected void initGson() {

		GsonBuilder builder = new GsonBuilder().registerTypeAdapter(Effect.class, new Serializer());
		gson = builder.create();
		parser = new JsonStreamParser(buffer);
		
	}

	/**
	 * Getter for a ban starting from an index
	 * @param index the index of the Ban in the Ban File
	 * @return the Effect of the Ban
	 * @throws ElementNotFoundException if the index is not valid or the Ban does not exist
	 */
	public Effect getBan(int index) throws ElementNotFoundException{
		
		ArrayList<Effect> effects = new ArrayList<>();
		
		while(parser.hasNext()){
			
			JsonElement element = parser.next();
			if(element.isJsonObject()){
				BanSerializerSet ban = gson.fromJson(element , BanSerializerSet.class);
				effects.add(ban.getBan());
			
			}
		}
		if(effects.size() > index) {
			this.indexOfBan = index;
			return effects.get(index);
		}
		else throw new ElementNotFoundException("The index passed is too big for the file dimension");
	}
	
	/**
	 * Getter for the Ban index, used to send the correct Ban Message to the View
	 * @return the index of the loaded Ban
	 */
	public int getBanIndex() {
		return this.indexOfBan;
	}

}
