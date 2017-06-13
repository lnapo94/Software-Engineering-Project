package it.polimi.ingsw.ps42.parser;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class ConversionBuilder extends Builder{

	public ConversionBuilder(String fileName) throws IOException {
		super(fileName);
	}

	@Override
	protected void initGson() {
		GsonBuilder builder = new GsonBuilder().serializeNulls().setPrettyPrinting();
		Type type = new TypeToken<HashMap<Integer, Integer>>(){}.getType();
		this.gson = builder.registerTypeAdapter(type, new Serializer()).create();
	}

	public void addSomething() throws IOException {
		String response;
		String parse;
		HashMap<String, Integer> map = new HashMap<>();
		
		for(Integer i = 0; i < 6; i++) {
			System.out.println("Quanti punti vittoria per " + (i + 1) + " carta verde: ");
			response = scanner.nextLine();
			map.put(i.toString(), Integer.parseInt(response));
		}
		
		parse = gson.toJson(map);
		buffer.append(parse);
		
		map = new HashMap<>();
		
		for(Integer i = 0; i < 6; i++) {
			System.out.println("Quanti punti vittoria per " + (i + 1) + " carta blu: ");
			response = scanner.nextLine();
			map.put(i.toString(), Integer.parseInt(response));
		}
		
		parse = gson.toJson(map);
		buffer.append(parse);

		buffer.close();
	}
	
	public static void main(String[] args) throws IOException {
		ConversionBuilder builder = new ConversionBuilder("Resource\\Configuration\\finalResourceConfiguration.json");
		builder.addSomething();
	}

}
