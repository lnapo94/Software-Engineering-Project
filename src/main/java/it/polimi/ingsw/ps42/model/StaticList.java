package it.polimi.ingsw.ps42.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class StaticList<E> implements Iterable<E>, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -409294164517194264L;

	//Data structure for List with a fixed max dimension
	
	private final int MAX_SIZE;
	
	private List<E> list;
	
	public StaticList(int MAX_SIZE) {
		//Construct the StaticList ArrayList
		
		list = new ArrayList<>();
		this.MAX_SIZE = MAX_SIZE;
	}
	
	public void add(E element) {
		//Add an element only if array isn't full
		
		if(list.size() < MAX_SIZE) {
			list.add(element);
		}
		else {
			throw new IndexOutOfBoundsException("Problem in StaticList.add: array is full");
		}
	}
	
	public E get(int index) {
		//Get the element at specificated index
		if(index < list.size())
			return list.get(index);
		return null;
	}
	
	public void remove(E element) {
		//Remove an element from the array
		if(list.isEmpty()) 
			throw new IndexOutOfBoundsException("StaticList.remove(element) error, empty array");
		list.remove(element);
	}
	
	public E remove(int index) {
		return list.remove(index);
	}
	
	public void removeAll() {
		//Remove all the element from the StaticList
		list = new ArrayList<>();
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
	public Iterator<E> iterator() {
		//This is necessary to implement the FOR EACH statement
		return new StaticListIterator();
	}
	
	//Private class for the FOR EACH statement
	
	private class StaticListIterator implements Iterator<E> {
		
		private int index = 0;

		@Override
		public boolean hasNext() {
			return index < size();
		}

		@Override
		public E next() {
			E temp = list.get(index);
			index++;
			return temp;
		}
		
	}
}
