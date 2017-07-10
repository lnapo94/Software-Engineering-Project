package it.polimi.ingsw.ps42.model.effect;

import static org.junit.Assert.assertEquals;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

/**
 * This class is used to test the ObtainBan effect. This particular effect is used
 * to decrease the player's resource every time he gains something. For example:
 * Every time the player increase his resources with money, the player gains one less money
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class ObtainBanTest {
	
	private Player p1;
	private ObtainBan ban;
	private Packet packetForThePlayer;
	
	@BeforeClass
	public static void classSetUp() {
		PropertyConfigurator.configure("Logger//Properties//test_log.properties");
	}
	
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

	/**
	 * This test simply apply the effect and verify if it works. In fact, the player has an
	 * Obtain Ban for the military point, and every time he gains military point, he loses one
	 * military point
	 */
	@Test
	public void test() {
		p1.increaseResource(packetForThePlayer);
		p1.synchResource();
		assertEquals(2, p1.getResource(Resource.MILITARYPOINT));
		assertEquals(0, p1.getResource(Resource.FAITHPOINT));
	}

}
