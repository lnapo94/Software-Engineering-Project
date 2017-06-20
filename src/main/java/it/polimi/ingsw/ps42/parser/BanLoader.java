package it.polimi.ingsw.ps42.parser;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;

import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.exception.ElementNotFoundException;

public class BanLoader extends Loader{

	public BanLoader(String fileName) throws IOException {
		
		super(fileName);
		
	}
	
	@Override
	protected void initGson() {

		GsonBuilder builder = new GsonBuilder().registerTypeAdapter(Effect.class, new Serializer());
		gson = builder.create();
		parser = new JsonStreamParser(buffer);
		
	}

	public Effect getBan(int index) throws ElementNotFoundException{
		
		ArrayList<Effect> effects = new ArrayList<>();
		
		while(parser.hasNext()){
			
			JsonElement element = parser.next();
			if(element.isJsonObject()){
				BanSerializerSet ban = gson.fromJson(element , BanSerializerSet.class);
				effects.add(ban.getBan());
			
			}
		}
		if(effects.size() > index)
			return effects.get(index);
		else throw new ElementNotFoundException("The index passed is too big for the file dimension");
	}

}
