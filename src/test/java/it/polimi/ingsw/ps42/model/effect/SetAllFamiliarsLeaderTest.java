package it.polimi.ingsw.ps42.model.effect;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.player.Player;

public class SetAllFamiliarsLeaderTest {
	
	private Player player;
	private Effect effectToEnable;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		PropertyConfigurator.configure("Logger//Properties//test_log.properties");
	}

	@Before
	public void setUp() throws Exception {
		player = new Player("P1");
		effectToEnable = new SetAllFamiliarsLeader(5);
		
		player.setFamiliarValue(FamiliarColor.ORANGE, 3);
		player.setFamiliarValue(FamiliarColor.BLACK, 3);
		player.setFamiliarValue(FamiliarColor.WHITE, 3);
		player.setFamiliarValue(FamiliarColor.NEUTRAL, 3);
	}

	@Test
	public void test() {
		Logger.getLogger(SetAllFamiliarsLeaderTest.class).info(effectToEnable.print());
		//Control if all familiars value is 3
		assertEquals(3, player.getFamiliarValue(FamiliarColor.ORANGE));
		assertEquals(3, player.getFamiliarValue(FamiliarColor.BLACK));
		assertEquals(3, player.getFamiliarValue(FamiliarColor.WHITE));
		assertEquals(3, player.getFamiliarValue(FamiliarColor.NEUTRAL));
		
		//Apply the effect
		effectToEnable.enableEffect(player);
		
		//Control if all colored familiars value is 5 now, and for the neutral familiar the value
		//must be always 3
		assertEquals(5, player.getFamiliarValue(FamiliarColor.ORANGE));
		assertEquals(5, player.getFamiliarValue(FamiliarColor.BLACK));
		assertEquals(5, player.getFamiliarValue(FamiliarColor.WHITE));
		assertEquals(3, player.getFamiliarValue(FamiliarColor.NEUTRAL));
		
		//Clone the effect
		Effect clonedEffect = effectToEnable.clone();
		
		assertTrue(clonedEffect != effectToEnable);
		assertTrue(clonedEffect.getTypeOfEffect() == effectToEnable.getTypeOfEffect());		
	}

}
