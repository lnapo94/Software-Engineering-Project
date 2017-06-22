package it.polimi.ingsw.ps42.model.resourcepacket;

import java.io.Serializable;

import it.polimi.ingsw.ps42.model.enumeration.Resource;

public class Unit implements Serializable {
	//This is the unit of resource for the game
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8206718312351766487L;
	private Resource resource;
	private int quantity;
	
	public Unit(Resource resource, int quantity) {
		this.resource = resource;
		this.quantity = quantity;
	}
	
	public Resource getResource() {
		return this.resource;
		
	}
	
	public int getQuantity() {
		return this.quantity;

	}
	
	public void setResource(Resource resource) {
		this.resource = resource;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
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
