package it.polimi.ingsw.ps42.parser;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;
import com.google.gson.reflect.TypeToken;

/**
 * Loader for the Game Timers such as move time or next game time 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class TimerLoader extends Loader{

	private HashMap<String, Long> timers;
	
	/**
	 * Constructor for the class starting from the path to the json file of Timer 
	 * @param fileName the path to the Timer File 
	 * @throws IOException if any problem in File opening occurs
	 */
	public TimerLoader(String fileName) throws IOException {
		super(fileName);
		timers = loader();
	}
	
	/**
	 * Method used to load the HashMap from the File
	 * @return the HashMap just read
	 */
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
	
	/**
	 * Getter for the Time to wait before the next Game starts
	 * @return the time to wait in the Server to start a new Game
	 */
	public long getServerTimer() {
		return timers.get("ServerTimer");
	}
	
	/**
	 * Getter for Time to wait before skipping the move of the current Player
	 * @return the Time to wait before skipping the move of the current Player
	 */
	public long getPlayerMoveTimer() {
		return timers.get("PlayerMoveTimer");
	}

	/**
	 * Private method used to personalize the gson loader
	 */
	@Override
	protected void initGson() {
		gson = new Gson();
		parser = new JsonStreamParser(buffer);
	}

}
