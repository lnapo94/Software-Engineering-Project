package it.polimi.ingsw.ps42.model.effect;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polimi.ingsw.ps42.model.player.Player;

public class FiveMoreVictoryPointLeaderTest {
	
	private Player player;
	private Effect effectToEnable;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		PropertyConfigurator.configure("Logger//Properties//test_log.properties");
	}

	@Before
	public void setUp() throws Exception {
		player = new Player("P1");
		effectToEnable = new FiveMoreVictoryPointLeader();
	}

	@Test
	public void test() {
		Logger.getLogger(FiveMoreVictoryPointLeaderTest.class).info(effectToEnable.print());
		assertFalse(player.hasMoreVictoryPoint());
		effectToEnable.enableEffect(player);
		assertTrue(player.hasMoreVictoryPoint());
		
		//Try to clone the effect
		Effect clonedEffect = effectToEnable.clone();
		assertTrue(clonedEffect != effectToEnable);
		assertTrue(clonedEffect.getTypeOfEffect() == effectToEnable.getTypeOfEffect());
	}

}
