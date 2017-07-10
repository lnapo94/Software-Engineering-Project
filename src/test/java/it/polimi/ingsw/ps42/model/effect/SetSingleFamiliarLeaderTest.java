package it.polimi.ingsw.ps42.model.effect;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.player.Player;

/**
 * This test aims to verify if the SetSingleFamiliarLeader effect works well
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class SetSingleFamiliarLeaderTest {
	
	private Player player;
	private SetSingleFamiliarLeader effectToEnable;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		PropertyConfigurator.configure("Logger//Properties//test_log.properties");
	}

	@Before
	public void setUp() throws Exception {
		player = new Player("P1");
		effectToEnable = new SetSingleFamiliarLeader(5);
		
		player.setFamiliarValue(FamiliarColor.ORANGE, 3);
		player.setFamiliarValue(FamiliarColor.BLACK, 3);
		player.setFamiliarValue(FamiliarColor.WHITE, 3);
		player.setFamiliarValue(FamiliarColor.NEUTRAL, 3);
	}

	/**
	 * This test create a SetSingleFamiliarLeader effect and enable it. When it will be activated, 
	 * the value of the white familiar will change to a new value, specify in the effect
	 */
	@Test
	public void test() {
		Logger.getLogger(SetSingleFamiliarLeaderTest.class).info(effectToEnable.print());
		//Control if all familiars value is 3
		assertEquals(3, player.getFamiliarValue(FamiliarColor.ORANGE));
		assertEquals(3, player.getFamiliarValue(FamiliarColor.BLACK));
		assertEquals(3, player.getFamiliarValue(FamiliarColor.WHITE));
		assertEquals(3, player.getFamiliarValue(FamiliarColor.NEUTRAL));
		
		//Adding a color to the effect
		effectToEnable.setFamiliarColor(FamiliarColor.WHITE);
		
		//Now enable the effect
		effectToEnable.enableEffect(player);
		
		//Control the increment of the effect
		assertEquals(5, effectToEnable.getIncrementValue());
		
		//Now only the white familiar has value 5
		assertEquals(3, player.getFamiliarValue(FamiliarColor.ORANGE));
		assertEquals(3, player.getFamiliarValue(FamiliarColor.BLACK));
		assertEquals(5, player.getFamiliarValue(FamiliarColor.WHITE));
		assertEquals(3, player.getFamiliarValue(FamiliarColor.NEUTRAL));
		
		//Clone the effect
		SetSingleFamiliarLeader clonedEffect = (SetSingleFamiliarLeader) effectToEnable.clone();
				
		assertTrue(clonedEffect != effectToEnable);
		assertTrue(clonedEffect.getTypeOfEffect() == effectToEnable.getTypeOfEffect());
		assertEquals(effectToEnable.getIncrementValue(), clonedEffect.getIncrementValue());
	}

}
