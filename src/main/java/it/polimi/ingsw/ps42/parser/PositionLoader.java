package it.polimi.ingsw.ps42.parser;

import java.io.IOException;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;

import it.polimi.ingsw.ps42.model.position.CouncilPosition;
import it.polimi.ingsw.ps42.model.position.MarketPosition;
import it.polimi.ingsw.ps42.model.position.Position;
import it.polimi.ingsw.ps42.model.position.TowerPosition;
import it.polimi.ingsw.ps42.model.position.YieldAndProductPosition;

/**
 * Class for Loading Position of the Game from json File
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class PositionLoader extends Loader {
	
	/**
	 * Constructor for the loader from the path to the json File
	 * @param fileName the path to the json File of Positions
	 * @throws IOException if any problem in File opening occurs
	 */
	public PositionLoader( String fileName) throws IOException {
		
		super(fileName);
	}
	
	/**
	 * Method used to personalize the gson loader
	 */
	protected void initGson(){
		
		GsonBuilder builder = new GsonBuilder().registerTypeAdapter(Position.class, new Serializer());
		gson = builder.create();
		parser = new JsonStreamParser(buffer);
		
	}
	
	public TowerPosition getNextTowerPosition(){
		
		if( parser.hasNext()){
			
			JsonElement element = parser.next();
			if( element.isJsonObject())
				return gson.fromJson(element, TowerPosition.class);
		}
		
		return null;
	}
	
	/**
	 * Getter for the YieldAndProduct position loader from File
	 * @return the YieldAndProduct position read from File
	 */
	public YieldAndProductPosition getNextYieldAndProductPosition(){
		
		if( parser.hasNext()){
			
			JsonElement element = parser.next();
			if( element.isJsonObject())
				return gson.fromJson(element, YieldAndProductPosition.class);
		}
		
		return null;
	}
	

	/**
	 * Getter for the Market position loader from File
	 * @return the Market position read from File
	 */
	public MarketPosition getNextMarketPosition(){
		
		if( parser.hasNext()){
			
			JsonElement element = parser.next();
			if( element.isJsonObject())
				return gson.fromJson(element, MarketPosition.class);
		}		
		
		return null;
	}
	

	/**
	 * Getter for the council position loader from File
	 * @return the Council position read from File
	 */
	public CouncilPosition getNextCouncilPosition(){
		
		if( parser.hasNext()){
			
			JsonElement element = parser.next();
			if( element.isJsonObject())
				return gson.fromJson(element, CouncilPosition.class);
		}
		
		return null;
	}
}
