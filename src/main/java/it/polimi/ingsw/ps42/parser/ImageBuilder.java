package it.polimi.ingsw.ps42.parser;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import it.polimi.ingsw.ps42.controller.cardCreator.CardsCreator;
import it.polimi.ingsw.ps42.controller.cardCreator.CardsFirstPeriod;
import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.StaticList;

public class ImageBuilder extends Builder{
	
	private HashMap<String, String> map;

	public ImageBuilder(String fileName) throws IOException {
		super(fileName);
		map = new HashMap<>();
	}

	@Override
	protected void initGson() {
		GsonBuilder builder=new GsonBuilder().serializeNulls().setPrettyPrinting();
		Type type = new TypeToken<HashMap<String, String>>(){}.getType();
		this.gson = builder.enableComplexMapKeySerialization().registerTypeAdapter(type, new Serializer()).create();
		
	}
	
	public void addToMap(String name, String path) {
		map.put(name, path);
	}
	
	public int mapSize() {
		return map.size();
	}
	
	public void close() throws IOException {
		String parse = gson.toJson(map);
		buffer.append(parse);
		buffer.close();
	}
	
	
	public static void main(String[] args) throws IOException {
		ImageBuilder builder = new ImageBuilder("Resource//Configuration//imagePaths.json");
		
		StaticList<Card> deck;
		
		CardsCreator creator = new CardsFirstPeriod();
		
		deck = creator.getNextGreenCards();
		
		for(int i = 0; i < deck.size(); i++) {
			Card card = deck.get(i);
			builder.addToMap(card.getName(), "Resource//Images//Cards//FirstPeriod//Green//" + (i+1) + ".png");
		}
		System.out.println(builder.mapSize());
		deck = creator.getNextYellowCards();
		
		for(int i = 0; i < deck.size(); i++) {
			Card card = deck.get(i);
			builder.addToMap(card.getName(), "Resource//Images//Cards//FirstPeriod//Yellow//" + (i+1) + ".png");
		}
		System.out.println(builder.mapSize());
		deck = creator.getNextBlueCards();
		
		for(int i = 0; i < deck.size(); i++) {
			Card card = deck.get(i);
			builder.addToMap(card.getName(), "Resource//Images//Cards//FirstPeriod//Blue//" + (i+1) + ".png");
		}
		System.out.println(builder.mapSize());
		deck = creator.getNextVioletCards();
		
		for(int i = 0; i < deck.size(); i++) {
			Card card = deck.get(i);
			builder.addToMap(card.getName(), "Resource//Images//Cards//FirstPeriod//Violet//" + (i+1) + ".png");
		}
		System.out.println(builder.mapSize());
		
		
		
		
		
		creator = creator.nextState();
		
		deck = creator.getNextGreenCards();
		
		for(int i = 0; i < deck.size(); i++) {
			Card card = deck.get(i);
			builder.addToMap(card.getName(), "Resource//Images//Cards//FirstPeriod//Green//" + (i+5) + ".png");
		}
		
		System.out.println(builder.mapSize());
		
		deck = creator.getNextYellowCards();
		
		for(int i = 0; i < deck.size(); i++) {
			Card card = deck.get(i);
			builder.addToMap(card.getName(), "Resource//Images//Cards//FirstPeriod//Yellow//" + (i+5) + ".png");
		}
		
		System.out.println(builder.mapSize());
		
		deck = creator.getNextBlueCards();
		
		for(int i = 0; i < deck.size(); i++) {
			Card card = deck.get(i);
			builder.addToMap(card.getName(), "Resource//Images//Cards//FirstPeriod//Blue//" + (i+5) + ".png");
		}
		
		System.out.println(builder.mapSize());
		
		deck = creator.getNextVioletCards();
		
		for(int i = 0; i < deck.size(); i++) {
			Card card = deck.get(i);
			builder.addToMap(card.getName(), "Resource//Images//Cards//FirstPeriod//Violet//" + (i+5) + ".png");
		}
		
		System.out.println(builder.mapSize());
		
		//ENDS FIRST PERIOD
		creator = creator.nextState();
		
		deck = creator.getNextGreenCards();
		
		for(int i = 0; i < deck.size(); i++) {
			Card card = deck.get(i);
			builder.addToMap(card.getName(), "Resource//Images//Cards//SecondPeriod//Green//" + (i+1) + ".png");
		}
		System.out.println(builder.mapSize());
		deck = creator.getNextYellowCards();
		
		for(int i = 0; i < deck.size(); i++) {
			Card card = deck.get(i);
			builder.addToMap(card.getName(), "Resource//Images//Cards//SecondPeriod//Yellow//" + (i+1) + ".png");
		}
		System.out.println(builder.mapSize());
		deck = creator.getNextBlueCards();
		
		for(int i = 0; i < deck.size(); i++) {
			Card card = deck.get(i);
			builder.addToMap(card.getName(), "Resource//Images//Cards//SecondPeriod//Blue//" + (i+1) + ".png");
		}
		System.out.println(builder.mapSize());
		deck = creator.getNextVioletCards();
		
		for(int i = 0; i < deck.size(); i++) {
			Card card = deck.get(i);
			builder.addToMap(card.getName(), "Resource//Images//Cards//SecondPeriod//Violet//" + (i+1) + ".png");
		}
		System.out.println(builder.mapSize());
		
		
		
		
		
		creator = creator.nextState();
		
		deck = creator.getNextGreenCards();
		
		for(int i = 0; i < deck.size(); i++) {
			Card card = deck.get(i);
			builder.addToMap(card.getName(), "Resource//Images//Cards//SecondPeriod//Green//" + (i+5) + ".png");
		}
		
		System.out.println(builder.mapSize());
		
		deck = creator.getNextYellowCards();
		
		for(int i = 0; i < deck.size(); i++) {
			Card card = deck.get(i);
			builder.addToMap(card.getName(), "Resource//Images//Cards//SecondPeriod//Yellow//" + (i+5) + ".png");
		}
		
		System.out.println(builder.mapSize());
		
		deck = creator.getNextBlueCards();
		
		for(int i = 0; i < deck.size(); i++) {
			Card card = deck.get(i);
			builder.addToMap(card.getName(), "Resource//Images//Cards//SecondPeriod//Blue//" + (i+5) + ".png");
		}
		
		System.out.println(builder.mapSize());
		
		deck = creator.getNextVioletCards();
		
		for(int i = 0; i < deck.size(); i++) {
			Card card = deck.get(i);
			builder.addToMap(card.getName(), "Resource//Images//Cards//SecondPeriod//Violet//" + (i+5) + ".png");
		}
		
		System.out.println(builder.mapSize());
		
		//ENDS SECOND PERIOD
		
		creator = creator.nextState();
		
		deck = creator.getNextGreenCards();
		
		for(int i = 0; i < deck.size(); i++) {
			Card card = deck.get(i);
			builder.addToMap(card.getName(), "Resource//Images//Cards//ThirdPeriod//Green//" + (i+1) + ".png");
		}
		System.out.println(builder.mapSize());
		deck = creator.getNextYellowCards();
		
		for(int i = 0; i < deck.size(); i++) {
			Card card = deck.get(i);
			builder.addToMap(card.getName(), "Resource//Images//Cards//ThirdPeriod//Yellow//" + (i+1) + ".png");
		}
		System.out.println(builder.mapSize());
		deck = creator.getNextBlueCards();
		
		for(int i = 0; i < deck.size(); i++) {
			Card card = deck.get(i);
			builder.addToMap(card.getName(), "Resource//Images//Cards//ThirdPeriod//Blue//" + (i+1) + ".png");
		}
		System.out.println(builder.mapSize());
		deck = creator.getNextVioletCards();
		
		for(int i = 0; i < deck.size(); i++) {
			Card card = deck.get(i);
			builder.addToMap(card.getName(), "Resource//Images//Cards//ThirdPeriod//Violet//" + (i+1) + ".png");
		}
		System.out.println(builder.mapSize());
		
		
		
		
		
		creator = creator.nextState();
		
		deck = creator.getNextGreenCards();
		
		for(int i = 0; i < deck.size(); i++) {
			Card card = deck.get(i);
			builder.addToMap(card.getName(), "Resource//Images//Cards//ThirdPeriod//Green//" + (i+5) + ".png");
		}
		
		System.out.println(builder.mapSize());
		
		deck = creator.getNextYellowCards();
		
		for(int i = 0; i < deck.size(); i++) {
			Card card = deck.get(i);
			builder.addToMap(card.getName(), "Resource//Images//Cards//ThirdPeriod//Yellow//" + (i+5) + ".png");
		}
		
		System.out.println(builder.mapSize());
		
		deck = creator.getNextBlueCards();
		
		for(int i = 0; i < deck.size(); i++) {
			Card card = deck.get(i);
			builder.addToMap(card.getName(), "Resource//Images//Cards//ThirdPeriod//Blue//" + (i+5) + ".png");
		}
		
		System.out.println(builder.mapSize());
		
		deck = creator.getNextVioletCards();
		
		for(int i = 0; i < deck.size(); i++) {
			Card card = deck.get(i);
			builder.addToMap(card.getName(), "Resource//Images//Cards//ThirdPeriod//Violet//" + (i+5) + ".png");
		}
		
		System.out.println(builder.mapSize());
		
		builder.close();
	}
}
