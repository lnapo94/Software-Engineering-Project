package it.polimi.ingsw.ps42.controller.cardCreator;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.StaticList;
import it.polimi.ingsw.ps42.parser.CardLoader;

public abstract class CardsCreator {
	//Max number of card in the StaticLists
	private final static int CARDS_NUMBER = 8;
	
	protected CardLoader loader;
	
	protected StaticList<Card> green;
	protected StaticList<Card> yellow;
	protected StaticList<Card> blue;
	protected StaticList<Card> violet;
	
	protected int lowIndex;
	protected int highIndex;
	
	protected String folderPath;
	
	public CardsCreator(String folderPath) throws IOException {
		this.folderPath = folderPath;
		
		//Initialize cards vectors
		green = loadCard("green.json");
		yellow = loadCard("yellow.json");
		blue = loadCard("blue.json");
		violet = loadCard("violet.json");
		
		//Initialize variables
		lowIndex = 0;
		highIndex = 4;
	}
	
	protected StaticList<Card> shuffle(StaticList<Card> cards) {
		//TODO testing
		//Shuffle the cards list
		StaticList<Card> temporary = new StaticList<>(CARDS_NUMBER);
		Random random = new Random();
		
		while(!cards.isEmpty()) {
			//Select a random card
			int randomNumber = random.nextInt(cards.size());
			Card temporaryCard = cards.get(random.nextInt(randomNumber));
			
			//Add the chosen card in the temporary arrayList and
			//remove it from the cards array
			temporary.add(temporaryCard);
			cards.remove(temporaryCard);
		}
		return temporary;
	}
	
	public StaticList<Card> getNextGreenCards() {
		return getNextCards(green, lowIndex, highIndex);
	}
	
	public StaticList<Card> getNextYellowCards() {
		return getNextCards(yellow, lowIndex, highIndex);
	}
	
	public StaticList<Card> getNextBlueCards() {
		return getNextCards(blue, lowIndex, highIndex);
	}
	
	public StaticList<Card> getNextVioletCards() {
		return getNextCards(violet, lowIndex, highIndex);
	}
	
	
	//Private method used to get the next (highIndex - lowIndex) cards to give it to the Table
	private StaticList<Card> getNextCards(StaticList<Card> cards, int lowIndex, int highIndex) {
		
		StaticList<Card> temporary = new StaticList<>(highIndex - lowIndex);
		
		for(int i = lowIndex; i < highIndex; i++)
			temporary.add(cards.get(i));
		
		return temporary;
	}
	
	//Private method used to load the card
	//Card file must be at most with 8 card
	private StaticList<Card> loadCard(String fileName) throws IOException {
		
		//Construct the correct path to file
		String temporaryPath = folderPath + fileName;
		
		loader = new CardLoader(temporaryPath);
		
		StaticList<Card> temporary = new StaticList<>(8);
		
		//Set the correct name to the loader
		List<Card> readCards = loader.getCards();
		
		if(readCards.size() < CARDS_NUMBER)
			throw new IOException("Cannot load card because in file there are less card than " + CARDS_NUMBER);
		
		for(int i = 0; i < CARDS_NUMBER; i++)
			temporary.add(readCards.get(i));
		
		return temporary;
	}
	
	public abstract CardsCreator nextState() throws IOException;
}
