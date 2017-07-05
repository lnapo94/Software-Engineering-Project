package it.polimi.ingsw.ps42.parser;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

/**
 * Loader for FaithPoint Path conversion in VictoryPoints
 * 
 * @author Luca Napoletano, Claudio Monatanari
 *
 */
public class FaithPathLoader extends Loader {

	private HashMap<String, Integer> faithPathMap;

	/**
	 * Constructor for a FaithPoint loader starting from a json File
	 * @param fileName the path to the gson File
	 * @throws IOException if any problem in File opening occurs
	 */
	public FaithPathLoader(String fileName) throws IOException{
	
		super(fileName);
		initGson();

		//Load the hashmap
		faithPathMap = loader();
		if(faithPathMap == null) {
			throw new IOException("Error in faithPathLoade, cannot open the file");
		}
	}

	/**
	 * Private method used to personalize the Gson loader
	 */
	@Override
	protected void initGson(){
			gson = new Gson();
			parser = new JsonStreamParser(buffer);
	}

	/**
	 * Private method used to get the HashMap of conversion from the File
	 * @return the HashMap of conversion
	 */
	private HashMap<String, Integer> loader() {
		//Private method used to load the hashmap from a Json file
		if(parser.hasNext()) {
			JsonElement element = parser.next();

			if(element.isJsonObject()) {
				Type type = new TypeToken< HashMap<String, Integer> >(){}.getType();
				return gson.fromJson(element, type);
			}
		}
		return null;
	}

	/**
	 * Getter for the FaithPoint conversion given an Integer 
	 * @param faithPoint the amount of FaithPoint to convert
	 * @return the VictoryPint equivalent 
	 */
	public Unit conversion(Integer faithPoint) {
		//This method must convert the given faith point in the correct victory point
		return new Unit(Resource.VICTORYPOINT, faithPathMap.get(faithPoint.toString()));
	}
}
