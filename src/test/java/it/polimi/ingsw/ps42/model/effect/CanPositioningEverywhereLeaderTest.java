package it.polimi.ingsw.ps42.model.effect;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polimi.ingsw.ps42.model.player.Player;

/**
 * This class tests the CanPositioningEveryWhere effect (Leader Cards), so it
 * enables the Effect on a player and then checks if the player can position its familiar every where 
 *    
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class CanPositioningEverywhereLeaderTest {

	private Player player;
	private Effect effectToEnable;
	
	@BeforeClass
	public static void setUpClass() {
		PropertyConfigurator.configure("Logger//Properties//test_log.properties");
	}
	
	@Before
	public void setUp() throws Exception {
		player = new Player("P1");
		effectToEnable = new CanPositioningEverywhereLeader();
	}

	@Test
	public void test() {		
		Logger.getLogger(CanPositioningEverywhereLeaderTest.class).info(effectToEnable.print());

		assertFalse(player.canPositioningEverywhere());
		effectToEnable.enableEffect(player);
		assertTrue(player.canPositioningEverywhere());
		
		Effect clonedEffect = effectToEnable.clone();
		
		assertTrue(clonedEffect != effectToEnable);
	}

}
