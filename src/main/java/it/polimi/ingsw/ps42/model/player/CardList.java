package it.polimi.ingsw.ps42.model.player;

import java.util.ArrayList;
import java.util.Iterator;

import it.polimi.ingsw.ps42.model.Card;

public class CardList implements Iterable<Card>{
	//Data structure for the cards
	//Max size of the list is 6 (default value)
	
	private static final int MAX_SIZE = 6;
	
	private ArrayList<Card> list;
	
	public CardList() {
		//Construct the Card ArrayList
		list = new ArrayList<>();
	}
	
	public void add(Card card) {
		//Add a card only if array isn't full
		
		if(list.size() < MAX_SIZE) {
			list.add(card);
		}
		else {
			throw new IndexOutOfBoundsException("Problem in CardList.add: array is full");
		}
	}
	
	public Card get(int index) {
		//Get the card at specificated index
		return list.get(index);
	}
	
	public void remove(Card card) {
		//Remove a card from the array
		if(list.isEmpty()) 
			throw new IndexOutOfBoundsException("CardList.remove(Card) error, empty array");
		list.remove(card);
	}
	
	public void removeAll() {
		//Remove all the cards
		for (Card c : list) {
			remove(c);
		}
	}
	
	public int size() {
		return list.size();
	}
	
	public boolean isEmpty() {
		return list.isEmpty();
	}
	
	public boolean isFull() {
		//Return true if the array is full
		return list.size() == MAX_SIZE;
	}

	@Override
	public Iterator<Card> iterator() {
		//This is necessary to implement the FOR EACH statement
		return new CardListIterator();
	}
	
	//Private class for the FOR EACH statement
	
	private class CardListIterator implements Iterator<Card> {
		
		private int index = 0;

		@Override
		public boolean hasNext() {
			return index < MAX_SIZE;
		}

		@Override
		public Card next() {
			Card temp = list.get(index);
			index++;
			return temp;
		}
		
	}
}
