package it.polimi.ingsw.ps42.parser;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class TimerBuilder extends Builder {

	public TimerBuilder(String fileName) throws IOException {
		super(fileName);
	}

	@Override
	protected void initGson() {
		GsonBuilder builder = new GsonBuilder().serializeNulls().setPrettyPrinting();
		Type type = new TypeToken<HashMap<Integer, Integer>>(){}.getType();
		this.gson = builder.registerTypeAdapter(type, new Serializer()).create();
	}
	
	public void createTimers() throws IOException {
		HashMap<String, Long> timers = new HashMap<>();
		String response;
		
		System.out.println("Inserisci valore timer per server [secondi]: ");
		response = scanner.nextLine();
		
		timers.put("ServerTimer", Long.parseLong(response));
		
		System.out.println("Inserisci valore timer per mossa giocatore [secondi]: ");
		response = scanner.nextLine();
		
		timers.put("PlayerMoveTimer", Long.parseLong(response));
		
		String parse = gson.toJson(timers);
		buffer.append(parse);
		buffer.close();
	}
	
	public static void main(String[] args) throws IOException {
		TimerBuilder builder = new TimerBuilder("Resource//Configuration//timers.json");
		builder.createTimers();
	}

}
