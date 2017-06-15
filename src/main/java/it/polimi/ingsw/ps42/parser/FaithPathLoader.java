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


public class FaithPathLoader extends Loader {

	private HashMap<String, Integer> faithPathMap;

	public FaithPathLoader(String fileName) throws IOException{
	
		super(fileName);
		initGson();

		//Load the hashmap
		faithPathMap = loader();
		if(faithPathMap == null) {
			throw new IOException("Error in faithPathLoade, cannot open the file");
		}
	}

	@Override
	protected void initGson(){
			gson = new Gson();
			parser = new JsonStreamParser(buffer);
	}

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

	
	public Unit conversion(Integer faithPoint) {
		//This method must convert the given faith point in the correct victory point
		return new Unit(Resource.VICTORYPOINT, faithPathMap.get(faithPoint.toString()));
	}
}
