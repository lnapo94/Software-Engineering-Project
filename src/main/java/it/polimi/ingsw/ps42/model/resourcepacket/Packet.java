package it.polimi.ingsw.ps42.model.resourcepacket;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import it.polimi.ingsw.ps42.model.enumeration.Resource;

public class Packet implements Iterable<Unit>   {
	
	private ArrayList<Unit> list;
	public	Packet()
	{
		list = new ArrayList<>() ;
	}
	
	public Packet(ArrayList<Unit> list) {
		
	}
	public Packet(Map<Resource, Integer> resource)
	{
		
		resource.forEach((r, q) -> {
			
		    Unit unit = null;
		    unit.setResource(r);
		    unit.setQuantity(q);
		    this.list.add(unit);
		});
	}
	
	public ArrayList<Unit> getPacket() { 
		return this.list;
		
	}
	
	public void setPacket(ArrayList<Unit> list) {
		this.list = list;
		
	}
	
	public void addUnit(Unit unit) {
		this.list.add(unit);
		
	}
	
	public void removeUnit(Unit unit) {
		
				 this.list.remove(unit);
			
		
		
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.list.toString();
	}
	
	public static void main(String[] args) {
		System.out.println("inizio: ");
		Unit un = new Unit();
		Resource res;
		res = Resource.MONEY;
		un.setQuantity(5);
		un.setResource(res);
		Packet packet = new Packet();
		packet.addUnit(un);
		
		Unit un2 = new Unit(Resource.WOOD,10);
		packet.addUnit(un2);
	
		System.out.println(packet.toString());
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
