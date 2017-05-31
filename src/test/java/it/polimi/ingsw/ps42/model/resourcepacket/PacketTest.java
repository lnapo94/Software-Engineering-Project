package it.polimi.ingsw.ps42.model.resourcepacket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.exception.ElementNotFoundException;

public class PacketTest {


	@Test
	public void test() throws ElementNotFoundException {
		//Testing the Packet and the Unit
		
		/*UNIT Test*/
		Unit unit = new Unit(Resource.MONEY, 13);
		assertEquals("Quantity in Unit must be 13", 13, unit.getQuantity());
		assertTrue("Resource in unit must be MONEY", unit.getResource() == Resource.MONEY);
		unit.setResource(Resource.FAITHPOINT);
		unit.setQuantity(11);
		assertEquals("Quantity in Unit must be 11", 11, unit.getQuantity());
		assertTrue("Resource in unit must be FAITH POINT", unit.getResource() == Resource.FAITHPOINT);
		// increase quantity of FAITHPOINT
		unit.setQuantity(unit.getQuantity()+3);
		assertEquals("Quantity in Unit must be 11", 14, unit.getQuantity());
		/*END UNIT TEST*/
		
		/*PACKET TEST*/
		Packet packet = new Packet();
		packet.addUnit(unit);
		for (Unit u : packet) {
			assertEquals("Now only resource in packet is the faith point unit with 14", 14, u.getQuantity());
			assertTrue(unit.getResource() == Resource.FAITHPOINT);
		}
		//Adding a new Faith Point, i want only 1 unit in packet
		unit.setQuantity(19);
		packet.addUnit(unit);
		for (Unit u : packet) {
			assertEquals("Now only resource in packet is the faith point unit with 14+19", 14+19, u.getQuantity());
			assertTrue(unit.getResource() == Resource.FAITHPOINT);
		}
		
		//add unit that dosn't exist *2
		Unit unitSlave = new Unit(Resource.SLAVE, 10);
		packet.addUnit(unitSlave);
		packet.addUnit(unitSlave);
		for (Unit u : packet) {
			if(u.getResource() == Resource.SLAVE){
			assertEquals("Now only resource in packet is the faith point unit with 10*2", 20, u.getQuantity());
			assertTrue(unitSlave.getResource() == Resource.SLAVE);
			}
		}
		//Create a Packet from an HashMap of resource
		
		Map<Resource, Integer> map = new HashMap<>();
		
		map.put(Resource.FAITHPOINT, 10);
		map.put(Resource.MONEY, 40);
		map.put(Resource.SLAVE, 10);
		
		Packet packet2 = new Packet(map);
		//add unit resource slave to the packet
		packet2.addUnit(unitSlave);
		for(Unit u : packet2) {
			if(u.getResource() == Resource.MONEY) {
				assertEquals("Created a Packet from a Map, Expected 30", 40, u.getQuantity());
				assertTrue("Expected Money", Resource.MONEY == u.getResource());
			}
			if(u.getResource() == Resource.FAITHPOINT) {
				assertEquals("Created a Packet from a Map, Expected 10", 10, u.getQuantity());
				assertTrue("Expected Faith Point", Resource.FAITHPOINT == u.getResource());
			}
			if(u.getResource() == Resource.SLAVE) {
				assertEquals("Created a Packet from a Map, Expected 20", 20, u.getQuantity());
				assertTrue("Expected SLAVE", Resource.SLAVE == u.getResource());
				unitSlave.setQuantity(10);
				assertTrue("Expected Faith Point", true == u.isGreater(unitSlave));

			}
		}
		// Check  isGreater & removeunit
		Packet packet3 = new Packet(map);
		packet2 = new Packet(map);
		packet2.addUnit(unitSlave);
		
		System.out.println( "unitaSlave: " + unitSlave.toString());
		packet2.removeUnit(unitSlave);
		
		System.out.println( "packet3: " + packet3.toString());
		System.out.println( "packet2: " + packet2.toString());
		
		// Check  isGreater p3 = p3
		assertTrue("Expected ", true == packet3.isGreater(packet3));
		System.out.println("control if packe3 is grater than packet 2 ");
		System.out.println( "packet3: " + packet3.toString());
		System.out.println( "packet2: " + packet2.toString());
		
		packet2.addUnit(unitSlave);
		// Check  isGreater p2 > p3
		assertTrue("Expected ", true == packet2.isGreater(packet3));
		System.out.println("control if packe3 is grater than packet 2 ");
		System.out.println( "packet3: " + packet3.toString());
		System.out.println( "packet2: " + packet2.toString());
		// Check  isGreater p3 > null
		assertTrue("Expected ", true == packet2.isGreater(null));
		System.out.println("control if packe3 is grater than packet 2 ");
		System.out.println( "packet3: " + packet3.toString());
		System.out.println( "packet2: " + packet2.toString());
		
		
	}

}
