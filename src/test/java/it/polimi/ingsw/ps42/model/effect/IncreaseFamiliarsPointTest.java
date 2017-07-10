package it.polimi.ingsw.ps42.model.effect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.player.Player;

/**
 * This class is used to test the IncreaseFamiliarsPoint effect. This effect increases all
 * the familiars value, except the neutral familiar.
 * After the application of the effect, this test aims to verify if the clone method works correctly
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class IncreaseFamiliarsPointTest {
	
	private Player player;
	private Effect effectToEnable;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		PropertyConfigurator.configure("Logger//Properties//test_log.properties");
	}

	@Before
	public void setUp() throws Exception {
		player = new Player("P1");
		effectToEnable = new IncreaseFamiliarsPoint(5);
		
		//Set all the player's familiars value to 1 point
		player.setFamiliarValue(FamiliarColor.ORANGE, 1);
		player.setFamiliarValue(FamiliarColor.BLACK, 1);
		player.setFamiliarValue(FamiliarColor.WHITE, 1);
		player.setFamiliarValue(FamiliarColor.NEUTRAL, 1);
	}

	@Test
	public void test() {
		Logger.getLogger(IncreaseFamiliarsPointTest.class).info(effectToEnable.print());
		//Control that all the player's familiars value is setted to 1
		assertEquals(1, player.getFamiliarValue(FamiliarColor.ORANGE) + player.getFamiliar(FamiliarColor.ORANGE).getIncrement());
		assertEquals(1, player.getFamiliarValue(FamiliarColor.BLACK) + player.getFamiliar(FamiliarColor.BLACK).getIncrement());
		assertEquals(1, player.getFamiliarValue(FamiliarColor.WHITE) + player.getFamiliar(FamiliarColor.WHITE).getIncrement());
		assertEquals(1, player.getFamiliarValue(FamiliarColor.NEUTRAL) + player.getFamiliar(FamiliarColor.NEUTRAL).getIncrement());
		
		//Enable the effect
		effectToEnable.enableEffect(player);
		
		//Now all the familiars, except the neutral familiar, must have 6 as setted value
		assertEquals(6, player.getFamiliarValue(FamiliarColor.ORANGE) + player.getFamiliar(FamiliarColor.ORANGE).getIncrement());
		assertEquals(6, player.getFamiliarValue(FamiliarColor.BLACK) + player.getFamiliar(FamiliarColor.BLACK).getIncrement());
		assertEquals(6, player.getFamiliarValue(FamiliarColor.WHITE) + player.getFamiliar(FamiliarColor.WHITE).getIncrement());
		assertEquals(1, player.getFamiliarValue(FamiliarColor.NEUTRAL) + player.getFamiliar(FamiliarColor.NEUTRAL).getIncrement());
		
		Effect clonedEffect = effectToEnable.clone();
		
		assertTrue(clonedEffect != effectToEnable);
		assertTrue(clonedEffect.getTypeOfEffect() == effectToEnable.getTypeOfEffect());
	}

}
