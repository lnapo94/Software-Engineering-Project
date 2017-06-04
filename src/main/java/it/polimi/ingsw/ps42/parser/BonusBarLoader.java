package it.polimi.ingsw.ps42.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;

import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.player.BonusBar;

public class BonusBarLoader extends Loader {

	
	public BonusBarLoader(String fileName) throws IOException {
		
		super(fileName);
		
	}
	
	@Override
	protected void initGson() {
		
		GsonBuilder builder = new GsonBuilder().registerTypeAdapter(Effect.class, new Serializer());
		gson = builder.create();
		parser = new JsonStreamParser(buffer);
	}

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
