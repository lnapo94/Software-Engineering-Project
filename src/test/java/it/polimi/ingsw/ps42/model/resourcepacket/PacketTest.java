package it.polimi.ingsw.ps42.model.resourcepacket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import it.polimi.ingsw.ps42.model.enumeration.Resource;

public class PacketTest {

	@Test
	public void test() {
		//Testing the Packet and the Unit
		
		/*UNIT Test*/
		Unit unit = new Unit(Resource.MONEY, 13);
		assertEquals("Quantity in Unit must be 13", 13, unit.getQuantity());
		assertTrue("Resource in unit must be MONEY", unit.getResource() == Resource.MONEY);
		unit.setResource(Resource.FAITHPOINT);
		unit.setQuantity(11);
		assertEquals("Quantity in Unit must be 11", 11, unit.getQuantity());
		assertTrue("Resource in unit must be FAITH POINT", unit.getResource() == Resource.FAITHPOINT);
		/*END UNIT TEST*/
		
		/*PACKET TEST*/
		Packet packet = new Packet();
		packet.addUnit(unit);
		for (Unit u : packet) {
			assertEquals("Now only resource in packet is the faith point unit with 11", 11, u.getQuantity());
			assertTrue(unit.getResource() == Resource.FAITHPOINT);
		}
		//Adding a new Faith Point, i want only 1 unit in packet
		unit.setQuantity(19);
		packet.addUnit(unit);
		for (Unit u : packet) {
			assertEquals("Now only resource in packet is the faith point unit with 11+19", 11+19, u.getQuantity());
			assertTrue(unit.getResource() == Resource.FAITHPOINT);
		}
		
		//Create a Packet from an HashMap of resource
		
		Map<Resource, Integer> map = new HashMap<>();
		
		map.put(Resource.FAITHPOINT, 10);
		map.put(Resource.MONEY, 40);
		
		Packet packet2 = new Packet(map);
		
		for(Unit u : packet2) {
			if(u.getResource() == Resource.MONEY) {
				assertEquals("Created a Packet from a Map, Expected 30", 40, u.getQuantity());
				assertTrue("Expected Money", Resource.MONEY == u.getResource());
			}
			if(u.getResource() == Resource.FAITHPOINT) {
				assertEquals("Created a Packet from a Map, Expected 10", 10, u.getQuantity());
				assertTrue("Expected Faith Point", Resource.FAITHPOINT == u.getResource());
			}
		}
	}

}
