package it.polimi.ingsw.ps42.model.effect;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

/**
 * This class tests the CardForEach effect, so it creates a Player with different cards, enable on him 
 * the CardForEach effect and later verifies that the right number of resources has been given to the player
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class CardForEachObtainTest {

	private Player player;
	private Effect effectToEnable;
	
	@BeforeClass
	public static void setUpClass() {
		PropertyConfigurator.configure("Logger//Properties//test_log.properties");
	}
	
	@Before
	public void setUp() throws Exception {
		player = new Player("P1");
		
		//Create a packet of gains to give to the player
		Packet gains = new Packet();
		
		//Adding to the packet 3 money and 5 victory point
		gains.addUnit(new Unit(Resource.MONEY, 3));
		gains.addUnit(new Unit(Resource.VICTORYPOINT, 5));
		
		//Give the packet to the effect
		effectToEnable = new CardForEachObtain(CardColor.GREEN, gains);
		
		//Create a single green card to give to the player twice (So he has 2 green cards)
		Card greenCard = new Card("Green Card", null, CardColor.GREEN, 1, 1, null, null, null, null, null);
		
		//Now create a single yellow card to give to the player once
		Card yellowCard = new Card("Yellow Card", null, CardColor.YELLOW, 1, 1, null, null, null, null, null);
		
		player.addCard(greenCard);
		player.addCard(greenCard);
		player.addCard(yellowCard);
		
	}

	@Test
	public void test() {
		Logger.getLogger(CardForEachObtainTest.class).info(effectToEnable.print());
		
		//Control if the player has 2 green cards and 1 yellow card
		assertEquals(2, player.getCardList(CardColor.GREEN).size());
		assertEquals(1, player.getCardList(CardColor.YELLOW).size());
		
		//Control if the player has 0 money and 0 victory point
		assertEquals(0, player.getResource(Resource.MONEY));
		assertEquals(0, player.getResource(Resource.VICTORYPOINT));
		
		//Now enable the effect
		effectToEnable.enableEffect(player);
		player.synchResource();
		
		//Control if the player has always has 2 green cards and 1 yellow card
		assertEquals(2, player.getCardList(CardColor.GREEN).size());
		assertEquals(1, player.getCardList(CardColor.YELLOW).size());

		//Control if the player has 6 money and 10 victory point, given from the effect
		assertEquals(6, player.getResource(Resource.MONEY));
		assertEquals(10, player.getResource(Resource.VICTORYPOINT));
		
		//Now try to clone the effect
		Effect clonedEffect = effectToEnable.clone();
		
		assertTrue(clonedEffect != effectToEnable);
		
		assertTrue(clonedEffect.getTypeOfEffect() == effectToEnable.getTypeOfEffect());
	}

}
