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
 * This class is used to test the IncreaseSingleFamiliar effect
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class IncreaseSingleFamiliarTest {

	private Player player;
	private Effect effectToEnable;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		PropertyConfigurator.configure("Logger//Properties//test_log.properties");
	}

	@Before
	public void setUp() throws Exception {
		player = new Player("P1");
		effectToEnable = new IncreaseSingleFamiliar(5, FamiliarColor.NEUTRAL);
		player.setFamiliarValue(FamiliarColor.NEUTRAL, 1);
	}

	/**
	 * This test tries to increase the neutral familiars with the application of the 
	 * increase single familiar effect. After the application, the test verify the correctness
	 * of the results
	 */
	@Test
	public void test() {
		Logger.getLogger(IncreaseSingleFamiliarTest.class).info(effectToEnable.print());
		
		assertEquals(0, player.getFamiliarValue(FamiliarColor.ORANGE) + player.getFamiliar(FamiliarColor.ORANGE).getIncrement());
		assertEquals(0, player.getFamiliarValue(FamiliarColor.BLACK) + player.getFamiliar(FamiliarColor.BLACK).getIncrement());
		assertEquals(0, player.getFamiliarValue(FamiliarColor.WHITE) + player.getFamiliar(FamiliarColor.WHITE).getIncrement());
		assertEquals(1, player.getFamiliarValue(FamiliarColor.NEUTRAL) + player.getFamiliar(FamiliarColor.NEUTRAL).getIncrement());

		effectToEnable.enableEffect(player);
		
		assertEquals(0, player.getFamiliarValue(FamiliarColor.ORANGE) + player.getFamiliar(FamiliarColor.ORANGE).getIncrement());
		assertEquals(0, player.getFamiliarValue(FamiliarColor.BLACK) + player.getFamiliar(FamiliarColor.BLACK).getIncrement());
		assertEquals(0, player.getFamiliarValue(FamiliarColor.WHITE) + player.getFamiliar(FamiliarColor.WHITE).getIncrement());
		assertEquals(6, player.getFamiliarValue(FamiliarColor.NEUTRAL) + player.getFamiliar(FamiliarColor.NEUTRAL).getIncrement());

		Effect clonedEffect = effectToEnable.clone();
		
		assertTrue(clonedEffect != effectToEnable);
		assertTrue(clonedEffect.getTypeOfEffect() == effectToEnable.getTypeOfEffect());
	}

}
