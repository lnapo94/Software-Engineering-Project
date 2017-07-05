package it.polimi.ingsw.ps42.model.effect;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polimi.ingsw.ps42.model.player.Player;

public class NoMarketBanTest {
	
	private Player player;
	private Effect effectToEnable;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		PropertyConfigurator.configure("Logger//Properties//test_log.properties");
	}

	@Before
	public void setUp() throws Exception {
		player = new Player("P1");
		effectToEnable = new NoMarketBan();
	}

	@Test
	public void test() {
		Logger.getLogger(NoMarketBanTest.class).info(effectToEnable.print());
		assertTrue(player.canStayInMarket());
		effectToEnable.enableEffect(player);
		assertFalse(player.canStayInMarket());
		
		Effect clonedEffect = effectToEnable.clone();
		
		assertTrue(clonedEffect != effectToEnable);
		assertTrue(clonedEffect.getTypeOfEffect() == effectToEnable.getTypeOfEffect());
	}

}
