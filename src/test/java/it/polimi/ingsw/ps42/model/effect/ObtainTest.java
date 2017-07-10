package it.polimi.ingsw.ps42.model.effect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polimi.ingsw.ps42.message.CardRequest;
import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

/**
 * This testing unit is used to test the Obtain effect.
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class ObtainTest {
	
	private Player p1;
	private Obtain obtainToTest;
	private Card card;
	
	@BeforeClass
	public static void classSetUp() {
		PropertyConfigurator.configure("Logger//Properties//test_log.properties");
	}
	
	@Before
	public void setUp() {
		//Create a player
		p1 = new Player("Player1");
		
		//Create the effect to test
		Packet cost = new Packet();
		Packet gain = new Packet();
		cost.addUnit(new Unit(Resource.MONEY, 3));
		gain.addUnit(new Unit(Resource.STONE, 3));
		obtainToTest = new Obtain(cost, gain, null);
		
		//Create the card
		ArrayList<Effect> immediateEffects = new ArrayList<>();
		immediateEffects.add(obtainToTest);
		card = new Card("Prova", "Descrizione", CardColor.YELLOW, 1, 3, null, immediateEffects,
				null, null, null);
		
		//Adding resource to player to pay the effect
		p1.increaseResource(cost);
		p1.synchResource();
	}
	
	/**
	 * This test creates a card and give to it the Obtain effect. Then the player pay
	 * that card and enable its immediate effect, and finally the player can choose from
	 * the effect requests and take the gains.
	 */
	@Test
	public void test() {
		p1.addCard(card);
		//Control if card is added to player's correct arraylist
		assertEquals(1, p1.getCardList(CardColor.YELLOW).size());
		assertTrue("Prova" == p1.getCardList(CardColor.YELLOW).get(0).getName());
		
		//Control the player resource
		assertEquals(3, p1.getResource(Resource.MONEY));
		
		
		//Now p1 must pay the card
		try {
			card.payCard(p1, null);
		} catch (NotEnoughResourcesException e) {
			System.out.println("Player can't pay the card");
		}
		
		card.setPlayer(p1);
		
		//After the card has been payed, it's time to enable the immediate effect
		try {
			card.enableImmediateEffect();
		} catch (NotEnoughResourcesException e) {
			System.out.println("There isn't an effect that the player can use");
		}
		
		List<CardRequest> requestsInPlayer;
		requestsInPlayer = p1.getRequests();
		//Now player has a request, because the obtain effect has a cost
		assertEquals(1, requestsInPlayer.size());
		
		//If player want to pay
		requestsInPlayer.get(0).setChoice(0);
		p1.addRequest(requestsInPlayer.get(0));
		
		//Then i want to apply the request from gameLogic (exactly like in gameLogic)
		requestsInPlayer = p1.getRequests();
		if(!requestsInPlayer.isEmpty())
			for(CardRequest request : requestsInPlayer)
				request.apply(p1);
		p1.synchResource();
		
		//Now the player has changed is money in stone, thanks to the obtainEffect
		assertEquals(0, p1.getResource(Resource.MONEY));
		assertEquals(3, p1.getResource(Resource.STONE));
		System.out.println(obtainToTest.print());
	}

}
