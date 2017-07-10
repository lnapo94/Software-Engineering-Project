package it.polimi.ingsw.ps42.model.effect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

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
 * This class tests the functionality of the CardCostBan effect, so it creates two player with some 
 *  victory points and some cards, later activate the CardCostBan on each of the players and checks 
 *  their victory points
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class CardCostBanTest {
	
	private Player p1;
	private Player p2;
	private CardCostBan ban;
	
	@BeforeClass
	public static void classSetUp() {
		PropertyConfigurator.configure("Logger//Properties//test_log.properties");
	}

	@Before
	public void setUp() throws Exception {
		//Create the player
		p1 = new Player("Player 1");
		
		//Assign to the player a number of victory point
		//These will be decreased later
		Packet victoryPoint = new Packet();
		victoryPoint.addUnit(new Unit(Resource.VICTORYPOINT, 25));
		p1.increaseResource(victoryPoint);
		p1.synchResource();
		
		//control if p1 has 25 victory point
		assertEquals(25, p1.getResource(Resource.VICTORYPOINT));
		
		//Now set up the ban
		ban = new CardCostBan(CardColor.VIOLET);
		
		//Creation of some cards to apply the ban
		Card c1 = new Card("Green", "", CardColor.GREEN, 1, 1, null, null, null, null, null);
		Card c2 = new Card("Yellow", "", CardColor.YELLOW, 1, 1, null, null, null, null, null);
		Card c3 = new Card("Violet", "", CardColor.VIOLET, 1, 1, null, null, null, null, null);
		
		//This is the card with a cost, and this card enable the ban
		Packet cost1 = new Packet();
		cost1.addUnit(new Unit(Resource.WOOD, 10));
		cost1.addUnit(new Unit(Resource.STONE, 5));
		
		Packet cost2 = new Packet();
		cost2.addUnit(new Unit(Resource.STONE, 5));
		cost2.addUnit(new Unit(Resource.FAITHPOINT, 2));
		
		Packet cost3 = new Packet();
		cost3.addUnit(new Unit(Resource.MILITARYPOINT, 2));
		
		ArrayList<Packet> totalCosts = new ArrayList<>();
		totalCosts.add(cost1);
		totalCosts.add(cost2);
		totalCosts.add(cost3);
		
		Card c4 = new Card("Violet", "", CardColor.VIOLET, 1, 1, totalCosts, null, null, null, null);
		
		//Set these cards to the player
		p1.addCard(c1);
		p1.addCard(c2);
		p1.addCard(c3);
		p1.addCard(c4);
		c1.setPlayer(p1);
		c2.setPlayer(p1);
		c3.setPlayer(p1);
		c4.setPlayer(p1);
		
		//Verify the correct position in player's arraylist
		assertTrue(p1.getCardList(CardColor.GREEN).get(0) == c1);
		assertTrue(p1.getCardList(CardColor.YELLOW).get(0) == c2);
		assertTrue(p1.getCardList(CardColor.VIOLET).get(0) == c3);
		assertTrue(p1.getCardList(CardColor.VIOLET).get(1) == c4);
		
		//Create a second player with victoryPoint < banCosts
		p2 = new Player("Player 2");
		Packet victoryPointForSecondPlayer = new Packet();
		victoryPointForSecondPlayer.addUnit(new Unit(Resource.VICTORYPOINT, 5));
		p2.increaseResource(victoryPointForSecondPlayer);
		
		//Add the fourth card to the second player
		p2.addCard(c4);

	}

	@Test
	public void test() {
		//Try to enable the ban
		//This ban is enabled by gamelogic at the end of the match before the final count
		
		ban.enableEffect(p1);
		p1.synchResource();
		assertEquals(5, p1.getResource(Resource.VICTORYPOINT));
		
		//Now verify if the second player, with less victory point than stone and wood
		//card costs
		
		ban.enableEffect(p2);
		p2.synchResource();
		assertEquals(0, p2.getResource(Resource.VICTORYPOINT));
	}

}
