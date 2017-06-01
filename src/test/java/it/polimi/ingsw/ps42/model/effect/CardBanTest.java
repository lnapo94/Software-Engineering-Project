package it.polimi.ingsw.ps42.model.effect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.player.Player;

public class CardBanTest {
	
	CardBan ban;
	Player p1;
	Card green;
	Card yellow;

	@Before
	public void setUp() throws Exception {
		//Create the effect. In this case, at the end of the match, I remove the card
		//before I counting the victory point from cards
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
		//Assign the ban to the player
		ban.enableEffect(p1);
		assertEquals(1, p1.getCardList(CardColor.YELLOW).size());
		assertEquals(0, p1.getCardList(CardColor.GREEN).size());
		assertTrue(p1.getCardList(CardColor.YELLOW).get(0) == yellow);
		
	}

}
