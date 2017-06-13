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

public class ConversionLoader extends Loader{
	
	//Conversion for the green and blue cards
	private HashMap<String, Integer> greenCardsConversion;
	private HashMap<String, Integer> blueCardsConversion;
	
	//Conversione for the other resources in player
	private int otherResources;

	public ConversionLoader(String fileName) throws IOException {
		super(fileName);
		
		//Load the conversion file for the card
		greenCardsConversion = loader();
		blueCardsConversion = loader();
		
		if(greenCardsConversion == null || blueCardsConversion == null) {
			throw new IOException();
		}
		
		//Load the conversion value for the other resources
		otherResources = loader().get("Resources");
	}
	
	private HashMap<String, Integer> loader() {
		if(parser.hasNext()) {
			JsonElement element = parser.next();
			
			if(element.isJsonObject()) {
				Type type = new TypeToken<HashMap<String, Integer>>(){}.getType();
				return gson.fromJson(element, type);
			}
		}
		return null;
	}

	@Override
	protected void initGson() {
		gson = new Gson();
		parser = new JsonStreamParser(buffer);
	}
	
	public Unit getGreenConversion(Integer conversionKey) {
		Unit unit = new Unit(Resource.VICTORYPOINT, greenCardsConversion.get(conversionKey.toString()));
		return unit;
	}
	
	public Unit getBlueConversion(int conversionKey) {
		Unit unit = new Unit(Resource.VICTORYPOINT, blueCardsConversion.get(conversionKey));
		return unit;
	}
	
	public Unit getOtherResourcesConversion(int totalResources) {
		return new Unit(Resource.VICTORYPOINT, totalResources/this.otherResources);
	}
	
	
	public static void main(String[] args) throws IOException {
		ConversionLoader loader = new ConversionLoader("Resource\\Configuration\\finalResourceConfiguration.json");
		System.out.println(loader.getOtherResourcesConversion(30));
	}
}
