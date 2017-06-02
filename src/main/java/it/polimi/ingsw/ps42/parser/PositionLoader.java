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

public class PositionLoader extends Loader {
	
	public PositionLoader( String fileName) throws IOException {
		
		super(fileName);
	}
	
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
	
	public YieldAndProductPosition getNextYieldAndProductPosition(){
		
		if( parser.hasNext()){
			
			JsonElement element = parser.next();
			if( element.isJsonObject())
				return gson.fromJson(element, YieldAndProductPosition.class);
		}
		
		return null;
	}
	
	public MarketPosition getNextMarketPosition(){
		
		if( parser.hasNext()){
			
			JsonElement element = parser.next();
			if( element.isJsonObject())
				return gson.fromJson(element, MarketPosition.class);
		}		
		
		return null;
	}
	
	public CouncilPosition getNextCouncilPosition(){
		
		if( parser.hasNext()){
			
			JsonElement element = parser.next();
			if( element.isJsonObject())
				return gson.fromJson(element, CouncilPosition.class);
		}
		
		return null;
	}
}
