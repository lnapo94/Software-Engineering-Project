package it.polimi.ingsw.ps42.model.resourcepacket;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import it.polimi.ingsw.ps42.model.enumeration.Resource;

public class Packet implements Iterable<Unit>   {
	
	private ArrayList<Unit> list;
	public	Packet()
	{	//Builder of an empty packet
		list = new ArrayList<>() ;
	}
	
	public Packet(ArrayList<Unit> list) {
		this.list=list;
		
	}
	public Packet(Map<Resource, Integer> resource)
	{	//Packet builder from a Map, used in BonusBar for e.g.
		this();
		resource.forEach((r, q) -> {
			
		    Unit unit = new Unit(r,q);
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
	
	/*public static void main(String[] args) {
		System.out.println("inizio: ");
		HashMap<Resource, Integer> res=new HashMap<>();
		res.put(Resource.FAITHPOINT, 5);
		res.put(Resource.WOOD, 3);
		Packet p=new Packet(res);	
		System.out.println(p.toString());
	}
	*/
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
