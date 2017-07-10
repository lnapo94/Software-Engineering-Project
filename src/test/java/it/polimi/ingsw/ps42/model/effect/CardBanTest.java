package it.polimi.ingsw.ps42.model.effect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.player.Player;

/**
 * This class tests the CardBan class, so it create the effect, enable it on a player and 
 * later verify that he do not has anymore the cards according to the effect
 *  
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class CardBanTest {
	
	CardBan ban;
	Player p1;
	Card green;
	Card yellow;
	
	@BeforeClass
	public static void classSetUp() {
		PropertyConfigurator.configure("Logger//Properties//test_log.properties");
	}

	@Before
	public void setUp() throws Exception {
		//Create the effect. In this case, at the end of the match, the gameLogic remove the card
		//before counting the victory point from cards
		ban = new CardBan(CardColor.GREEN);
		
		//Create a player
		p1 = new Player("Player 1");
		
		//Create two cards, the first is green, the second is yellow
		green = new Card("", "", CardColor.GREEN, 1, 1, null, null, null, null, null);
		yellow = new Card("", "", CardColor.YELLOW, 1, 1, null, null, null, null, null);
		
		//Add the cards to the player arraylist
		p1.addCard(green);
		p1.addCard(yellow);
	}

	@Test
	public void test() {
		//Assign the ban to the player and verify he does not have the cards anymore
		ban.enableEffect(p1);
		assertEquals(1, p1.getCardList(CardColor.YELLOW).size());
		assertEquals(0, p1.getCardList(CardColor.GREEN).size());
		assertTrue(p1.getCardList(CardColor.YELLOW).get(0) == yellow);
		
	}

}
