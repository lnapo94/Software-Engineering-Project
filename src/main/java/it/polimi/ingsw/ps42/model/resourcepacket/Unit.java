package it.polimi.ingsw.ps42.model.resourcepacket;

import java.io.Serializable;

import it.polimi.ingsw.ps42.model.enumeration.Resource;

public class Unit implements Serializable {
	//This is the unit of resource for the game
	
	/**
	 * Class that represents the single Resource Unit of the Game, it has a Resource type and a quantity 
	 * 
	 * @author Luca Napoletano, Claudio Montanari
	 */
	private static final long serialVersionUID = 8206718312351766487L;
	private Resource resource;
	private int quantity;
	
	/**
	 * Constructor of the Unit, requires the Resource type and the relative quantity
	 * 
	 * @param resource the type of Resource for the Unit
	 * @param quantity the amount of that Resource
	 */
	public Unit(Resource resource, int quantity) {
		this.resource = resource;
		this.quantity = quantity;
	}
	
	/**
	 * Getter for the Resource type of the Unit
	 * 
	 * @return the Resource type of the Unit
	 */
	public Resource getResource() {
		return this.resource;
		
	}
	
	/**
	 * Getter for the quantity of the Unit
	 * 
	 * @return the qunatity of the current Unit
	 */
	public int getQuantity() {
		return this.quantity;

	}
	
	/**
	 * Setter for the Resource type of the Unit
	 * 
	 * @param resource, the new Resource type of the Unit
	 */
	public void setResource(Resource resource) {
		this.resource = resource;
	}
	
	/**
	 * Setter for the quantity of the Unit
	 * 
	 * @param quantity, the new quantity for that Unit
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	/**
	 * Method used to confront two Unit  of the same Resource type
	 * 
	 * @param unit, the Unit to confront to the current
	 * 
	 * @return true if the current Unit is of the same type of the Unit passed and its quantity is greater or equal then the Unit passed
	 */
	public boolean isGreater(Unit unit){
		if(unit.getResource()==resource){
			return this.quantity>=unit.getQuantity();
		}
		else return false;
	}
	
	@Override
	public String toString() {
		return this.resource + ":" + this.quantity;
	}
}
