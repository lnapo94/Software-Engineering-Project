package it.polimi.ingsw.ps42.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Service Class used to represent fixed-dimension array, such as the towers in table
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 * @param <E>	The type of the object inside the StaticList
 */
public class StaticList<E> implements Iterable<E>, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -409294164517194264L;

	//Data structure for List with a fixed max dimension
	
	private final int MAX_SIZE;
	
	private List<E> list;
	
	/**
	 * The constructor
	 * 
	 * @param MAX_SIZE	The number of how many elements can be stored in the StaticList object
	 */
	public StaticList(int MAX_SIZE) {
		//Construct the StaticList ArrayList
		
		list = new ArrayList<>();
		this.MAX_SIZE = MAX_SIZE;
	}
	
	/**
	 * Add an element to the StaticList, only if the StaticList isn't full
	 * 
	 * @param element		The element to add
	 */
	public void add(E element) {
		//Add an element only if array isn't full
		
		if(list.size() < MAX_SIZE) {
			list.add(element);
		}
		else {
			throw new IndexOutOfBoundsException("Problem in StaticList.add: array is full");
		}
	}
	
	/**
	 * Take an element from StaticList
	 * 
	 * @param index		The index of the element in the StaticList
	 * @return			The element at the specified index
	 */
	public E get(int index) {
		//Get the element at specified index
		if(index < list.size())
			return list.get(index);
		return null;
	}
	
	/**
	 * Remove an element from the StaticList
	 * 
	 * @param element	The element to remove
	 */
	public void remove(E element) {
		//Remove an element from the array
		if(list.isEmpty()) 
			throw new IndexOutOfBoundsException("StaticList.remove(element) error, empty array");
		list.remove(element);
	}
	
	/**
	 * Remove an element and return it
	 * 
	 * @param index		The position in the StaticList where is the element to remove
	 * @return			The removed element
	 */
	public E remove(int index) {
		return list.remove(index);
	}
	
	/**
	 * Remove all the element from the StaticList
	 */
	public void removeAll() {
		//Remove all the element from the StaticList
		list = new ArrayList<>();
	}
	
	/**
	 * Return the size of the StaticList
	 * 
	 * @return		How many elements are in the StaticList
	 */
	public int size() {
		return list.size();
	}
	
	/**
	 * Control if the StaticList is empty
	 * 
	 * @return		True if there isn't any element in the StaticList, otherwise False
	 */
	public boolean isEmpty() {
		return list.isEmpty();
	}
	
	/**
	 * Control if the StaticList is full
	 * 
	 * @return		True if the StaticList is full (size == MAX_ELEMENTS), otherwise False 
	 */
	public boolean isFull() {
		//Return true if the array is full
		return list.size() == MAX_SIZE;
	}
	
	public int getMaxSize() {
		return this.MAX_SIZE;
	}

	/**
	 * The iterator of the StaticList, necessary to implement a For-Each Statement
	 */
	@Override
	public Iterator<E> iterator() {
		//This is necessary to implement the FOR EACH statement
		return new StaticListIterator();
	}
	
	//Private class for the FOR EACH statement
	
	/**
	 * Private class that represents the iterator
	 * @author Luca Napoletano, Claudio Montanari
	 *
	 */
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
