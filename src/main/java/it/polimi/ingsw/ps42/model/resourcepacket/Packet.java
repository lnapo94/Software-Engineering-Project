package it.polimi.ingsw.ps42.model.resourcepacket;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.exception.ElementNotFoundException;

public class Packet implements Iterable<Unit>   {
	
	private List<Unit> list;
	
	public	Packet()
	{	//Builder of an empty packet
		list = new ArrayList<>() ;
	}
	
	public Packet(Map<Resource, Integer> resource)
	{	//Packet builder from a Map, used in BonusBar for e.g.
		this();
		resource.forEach((r, q) -> {
			
		    Unit unit = new Unit(r,q);
		    this.list.add(unit);
		});
	}
	
	public List<Unit> getPacket() { 
		return this.list;
		
	}
	
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
	
	public boolean isGreater(Packet packet){
		
		
		
	}
	private int search(Resource resource) throws ElementNotFoundException{
		for (Unit unit : list) {
			if(unit.getResource()==resource)
				return list.indexOf(unit);
		}
		//Resource is not in the list
		throw new ElementNotFoundException("Method search in packet, no unit present in list");
	}
	
	public void removeUnit(Unit unit) {
		
				 this.list.remove(unit);	
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.list.toString();
	}
	
	@Override
	public Iterator<Unit> iterator() {
		// TODO Auto-generated method stub
		return new UnitIterator();
	}
	private class UnitIterator implements Iterator<Unit> {
		private int index=0;
		
		
		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			return index < list.size() ;
		}

		@Override
		public Unit next() {
			// TODO Auto-generated method stub
			Unit unitTemp = list.get(index);
			index++;
			return unitTemp;
		}
		
	}
	
	
}
