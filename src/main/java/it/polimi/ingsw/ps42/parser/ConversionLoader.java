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
 * Loader for the Conversion of Victory Points
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class ConversionLoader extends Loader{
	
	//Conversion for the green and blue cards
	private HashMap<String, Integer> greenCardsConversion;
	private HashMap<String, Integer> blueCardsConversion;
	
	//Conversion for the other resources in player
	private int otherResources;

	/**
	 * Constructor for the Conversion Loader from a json File
	 * @param fileName the path to a Conversion File
	 * @throws IOException if any problem in file opening occurs
	 */
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
	
	/**
	 * Private method for Loading the HashMap from File
	 * @return the HashMap read from file
	 */
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

	/**
	 * Private method for gson loader personalization
	 */
	@Override
	protected void initGson() {
		gson = new Gson();
		parser = new JsonStreamParser(buffer);
	}
	
	/**
	 * Getter for GreenCard conversion in VictoryPoint
	 * @param conversionKey the key for the HashMap of greenCard possible Conversions
	 * @return the VictoryPoint Unit conversion
	 */
	public Unit getGreenConversion(Integer conversionKey) {
		Unit unit = new Unit(Resource.VICTORYPOINT, greenCardsConversion.get(conversionKey.toString()));
		return unit;
	}
	

	 /** Getter for BlueCard conversion in VictoryPoint
	 * @param conversionKey the key for the HashMap of blueCard possible Conversions
	 * @return the VictoryPoint Unit conversion
	 */
	public Unit getBlueConversion(Integer conversionKey) {
		Unit unit = new Unit(Resource.VICTORYPOINT, blueCardsConversion.get(conversionKey.toString()));
		return unit;
	}
	
	 /** Getter for other Resources conversion in VictoryPoint
		 * @param conversionKey the total amount of other Resources 
		 * @return the VictoryPoint Unit conversion
		 */
	public Unit getOtherResourcesConversion(int totalResources) {
		return new Unit(Resource.VICTORYPOINT, totalResources/this.otherResources);
	}

}
