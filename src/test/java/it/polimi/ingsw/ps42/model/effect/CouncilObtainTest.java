package it.polimi.ingsw.ps42.model.effect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polimi.ingsw.ps42.message.CouncilRequest;
import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.exception.WrongChoiceException;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

public class CouncilObtainTest {

	private Player player1;
	private Player player2;
	private Card card;
	private Card card2;
	
	@BeforeClass
	public static void classSetUp() {
		PropertyConfigurator.configure("Logger//Properties//test_log.properties");
	}
	
	@Before
	public void setup(){
		
		//Build the player and add resources
		player1 = new Player("playerTest");
		player2 = new Player("Player Test 2");
		Packet playerResources = new Packet();
		playerResources.addUnit(new Unit(Resource.MONEY, 2));
		playerResources.addUnit(new Unit(Resource.WOOD, 5));
		player1.increaseResource(playerResources);
		player1.synchResource();
		player2.increaseResource(playerResources);
		player2.synchResource();
		
		//Build cards with a CouncilObtain effect
		
		//Build the cards cost
		Packet cost = new Packet();
		cost.addUnit(new Unit(Resource.WOOD, 5));
		ArrayList<Packet> costs = new ArrayList<>();
		costs.add(cost);
		
		//Build the effects
		ArrayList<Effect> immediateEffects1 = new ArrayList<>();
		immediateEffects1.add(buildEasyEffect());
		ArrayList<Effect> immediateEffects2 = new ArrayList<>();
		immediateEffects2.add(buildEasyEffect());
		
		//Build the cards
		card = new Card("provaCouncil", "desc", CardColor.BLUE, 2, 2, costs, immediateEffects1, null, null, null);
		card2 = new Card("provaCouncil2", "desc2", CardColor.GREEN, 2, 2, costs, immediateEffects2, null, null, null);
		
	}
	
	private Effect buildEasyEffect(){
		
		ArrayList<Obtain> possibleConversion = new ArrayList<>();
		Packet gains1 = new Packet();
		Packet gains2 = new Packet();
		Packet gains3 = new Packet();
		
		gains1.addUnit(new Unit(Resource.MILITARYPOINT, 2));
		gains2.addUnit(new Unit(Resource.VICTORYPOINT, 2));
		gains3.addUnit(new Unit(Resource.WOOD, 2));
		
		possibleConversion.add(new Obtain( null, gains1, null));
		possibleConversion.add(new Obtain( null, gains2, null));
		possibleConversion.add(new Obtain( null, gains3, null));
		
		return new CouncilObtain( 2, possibleConversion);
	}
	
	@After
	public void finalCheck(){
		
		assertEquals(2, player1.getResource(Resource.MONEY));
		assertEquals(5, player1.getResource(Resource.WOOD));
		assertEquals(0, player1.getResource(Resource.VICTORYPOINT));
		

		assertEquals(2, player2.getResource(Resource.MONEY));
		assertEquals(7, player2.getResource(Resource.WOOD));
		assertEquals(2, player2.getResource(Resource.VICTORYPOINT));
	}
	
	@Test
	public void test() {
		
		setup();
		player1.addCard(card);
		card.setPlayer(player1);
		player1.synchResource();
		
		player2.addCard(card2);
		card2.setPlayer(player2);
		player2.synchResource();
		
		assertEquals( 2, player1.getResource(Resource.MONEY));
		assertEquals( 5, player1.getResource(Resource.WOOD));

		assertEquals( 2, player2.getResource(Resource.MONEY));
		assertEquals( 5, player2.getResource(Resource.WOOD));
		
		try {
			
			card.enableImmediateEffect();
			card2.enableImmediateEffect();
			CouncilRequest requestCouncil= player1.getCouncilRequests().get(0);
			CouncilRequest requestCouncil2= player2.getCouncilRequests().get(0);
			
			try {
				requestCouncil.addChoice(1);
				
				requestCouncil2.addChoice(1);
				requestCouncil2.addChoice(1);

				assertTrue(!requestCouncil.apply(player1));
				assertTrue(requestCouncil2.apply(player2));
				
				player1.synchResource();
				player2.synchResource();
				
				finalCheck();
				
			} catch (WrongChoiceException e) {
				e.printStackTrace();
			}
			
			
		} catch (NotEnoughResourcesException e) {
			
			e.printStackTrace();
		}

		
	}

}
