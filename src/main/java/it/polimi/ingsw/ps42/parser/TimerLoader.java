package it.polimi.ingsw.ps42.parser;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;
import com.google.gson.reflect.TypeToken;

public class TimerLoader extends Loader{

	private HashMap<String, Long> timers;
	
	
	
	public TimerLoader(String fileName) throws IOException {
		super(fileName);
		timers = loader();
	}
	
	private HashMap<String, Long> loader() {
		if(parser.hasNext()) {
			JsonElement element = parser.next();
			
			if(element.isJsonObject()) {
				Type type = new TypeToken<HashMap<String, Long>>(){}.getType();
				return gson.fromJson(element, type);
			}
		}
		return null;
	}
	
	public long getServerTimer() {
		return timers.get("ServerTimer");
	}
	
	public long getPlayerMoveTimer() {
		return timers.get("PlayerMoveTimer");
	}

	@Override
	protected void initGson() {
		gson = new Gson();
		parser = new JsonStreamParser(buffer);
	}

}
