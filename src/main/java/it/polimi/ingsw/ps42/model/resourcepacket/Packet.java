package it.polimi.ingsw.ps42.model.resourcepacket;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import it.polimi.ingsw.ps42.model.Printable;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.exception.ElementNotFoundException;


/**
 * This class is a container of Units and is used as the basic Resource reference in all the Game
 * 
 * @author Luca Napoletano, Claudio Montanari
 */
public class Packet implements Iterable<Unit>, Printable, Cloneable, Serializable {
	
	private static final long serialVersionUID = 4208699031549844006L;
	private List<Unit> list;
	
	/**
	 * Default Constructor, builds an empty Packet
	 */
	public	Packet()
	{	//Builder of an empty packet
		list = new ArrayList<>() ;
	}
	
	/**
	 * Special Constructor, builds a Packet starting from a Map
	 * 
	 * @param resource is a Map where key is Resource type and value is Resource quantity
	 */
	public Packet(Map<Resource, Integer> resource)
	{	//Packet builder from a Map, used in BonusBar for e.g.
		this();
		resource.forEach((r, q) -> {
			
		    Unit unit = new Unit(r,q);
		    this.list.add(unit);
		});
	}
	
	/**
	 * Getter method used to return the list of unit that belongs to the Packet
	 * 
	 * @return The list of Unit that belongs to the Packet
	 */
	public List<Unit> getPacket() { 
		return this.list;
		
	}
	
	/**
	 * Method used to sum a Unit to the current Packet of Resources
	 * 
	 * @param unit the Unit to sum to the Packet
	 */
	public void addUnit(Unit unit) {
		int pos;
		try {
			
			pos = search(unit.getResource());
			Unit tempUnit=list.get(pos);
			tempUnit.setQuantity(tempUnit.getQuantity()+unit.getQuantity());
			
		} catch (ElementNotFoundException e) {
			
			list.add(new Unit(unit.getResource(),unit.getQuantity()));
		}
	}
	
	/**
	 * Method used to confront two Packets, return true if current Packet contains all the Resources of the passed Packet
	 * 
	 * @param packet the Packet that is used to make the confront
	 * @return true if the Packet contains at least all the Resources of the Packet passed
	 */
	public boolean isGreater(Packet packet){
		//Checks if the packet has almost all the resources of the packet passed
		if(packet!=null){
			int index;
			for (Unit unit : packet) {
				try{
					index=search(unit.getResource());
					Unit tempUnit=this.list.get(index);
					if(!tempUnit.isGreater(unit)) //This means the packet has less quantity of a single resource of the packet passed
						return false;
				}
				catch (ElementNotFoundException e) {		//This means the packet miss a Resource of the packet passed
					return false;
				}
				
			}
			
		}
		
		return true;		//this mean the packet passed is either empty or every units is less/equal than the units of the instance
		
	}
	
	/**
	 * Private method used to search a unit in a Packet, used as support to add and confront operations
	 * 
	 * @param resource the Resource unit to search in the Packet
	 * @return the index of the unit passed if that exist otherwise throws an Exception
	 * @throws ElementNotFoundException if the Resource passed is not contained in the Packet
	 */
	private int search(Resource resource) throws ElementNotFoundException{
		for (Unit unit : list) {
			if(unit.getResource()==resource)
				return list.indexOf(unit);
		}
		//Resource is not in the list
		throw new ElementNotFoundException("Method search in packet, no unit present in list");
	}
	
	/**
	 * Method used to remove a unit to the current Packet if is contained in it
	 * 
	 * @param unit the Unit to subtract
	 * @throws ElementNotFoundException if the Packet does not contains the Unit passed
	 */
	public void removeUnit(Unit unit) throws ElementNotFoundException {
		int index = search(unit.getResource());
		this.list.remove(index);
	}
	
	/**
	 * Method used to subtract a Unit to the Packet, if the quantity of that unit is not sufficient then the quantity is
	 * automatically set to zero
	 *  
	 * @param unitToSubtract the Unit that will be subtracted to the Packet
	 */
	public void subtractUnit(Unit unitToSubtract) {
		//Subtract a unit from the packet, if the result quantity goes under 0
		//the quantity is set to zero automatically
		try {
			int index = search(unitToSubtract.getResource());
			Unit unit = list.get(index);
			unit.setQuantity(unit.getQuantity() - unitToSubtract.getQuantity());
			
			if(unit.getQuantity() < 0) {
				unit.setQuantity(0);
			}
			
		} catch (ElementNotFoundException e) {
			System.out.println("Nothing to subtract");
		}
	}
	
	@Override
	public Iterator<Unit> iterator() {
		return new UnitIterator();
	}
	private class UnitIterator implements Iterator<Unit> {
		private int index=0;
		
		
		@Override
		public boolean hasNext() {
			return index < list.size() ;
		}

		@Override
		public Unit next() {
			Unit unitTemp = list.get(index);
			index++;
			return unitTemp;
		}
		
	}
	@Override
	public String print() {
		return this.list.toString();
	}
	
	@Override
	public Packet clone() {
		Packet packet = new Packet();
		
		for(Unit unit : list) {
			Unit temp = new Unit(unit.getResource(), unit.getQuantity());
			packet.addUnit(temp);			
		}
		
		return packet;
	}
}
