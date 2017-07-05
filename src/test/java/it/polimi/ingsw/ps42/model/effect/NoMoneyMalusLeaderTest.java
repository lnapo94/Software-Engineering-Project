package it.polimi.ingsw.ps42.model.effect;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polimi.ingsw.ps42.model.player.Player;

public class NoMoneyMalusLeaderTest {
	
	private Player player;
	private Effect effectToEnable;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		PropertyConfigurator.configure("Logger//Properties//test_log.properties");
	}

	@Before
	public void setUp() throws Exception {
		player = new Player("P1");
		effectToEnable = new NoMoneyMalusLeader();
	}

	@Test
	public void test() {
		Logger.getLogger(NoMoneyMalusLeaderTest.class).info(effectToEnable.print());
		assertFalse(player.hasNoMoneyBonus());
		effectToEnable.enableEffect(player);
		assertTrue(player.canTakeBonusFromTower());
		
		Effect clonedEffect = effectToEnable.clone();
		
		assertTrue(clonedEffect != effectToEnable);
		assertTrue(clonedEffect.getTypeOfEffect() == effectToEnable.getTypeOfEffect());
	}

}
