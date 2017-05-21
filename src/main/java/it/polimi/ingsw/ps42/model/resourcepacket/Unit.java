package it.polimi.ingsw.ps42.model.resourcepacket;

import it.polimi.ingsw.ps42.model.enumeration.Resource;

public class Unit {
	//This is the unit of resource for the game
	
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
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.resource + ":" + this.quantity;
	}
}
