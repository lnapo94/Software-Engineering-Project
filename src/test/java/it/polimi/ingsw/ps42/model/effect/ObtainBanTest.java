package it.polimi.ingsw.ps42.model.effect;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

public class ObtainBanTest {
	
	private Player p1;
	private ObtainBan ban;
	private Packet packetForThePlayer;
	
	@Before
	public void setUp() throws Exception {
		//Create a player
		p1 = new Player("Player 1");
		
		//Create a ban
		Packet packet = new Packet();
		packet.addUnit(new Unit(Resource.MILITARYPOINT, 1));
		packet.addUnit(new Unit(Resource.FAITHPOINT, 2));
		ban = new ObtainBan(packet);
		
		//Add ban to a player
		p1.setBan(ban);
		
		//Create a military point packet to add to the player
		packetForThePlayer = new Packet();
		Unit unit = new Unit(Resource.MILITARYPOINT, 3);
		Unit unit2 = new Unit(Resource.FAITHPOINT, 2);
		packetForThePlayer.addUnit(unit);
		packetForThePlayer.addUnit(unit2);
	}

	@Test
	public void test() {
		//Now player has a ban, so each time he gains a military resource, he also loses 
		//one of that resource
		p1.increaseResource(packetForThePlayer);
		p1.synchResource();
		assertEquals(2, p1.getResource(Resource.MILITARYPOINT));
		assertEquals(0, p1.getResource(Resource.FAITHPOINT));
	}

}
