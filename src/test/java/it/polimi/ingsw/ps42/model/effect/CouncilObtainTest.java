package it.polimi.ingsw.ps42.model.effect;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.exception.WrongChoiceException;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.request.CouncilRequest;
import it.polimi.ingsw.ps42.model.request.RequestInterface;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

public class CouncilObtainTest {

	private Player player;
	private Card card;
	
	@Before
	public void setup(){
		
		//Build the player and add resources
		player = new Player("playerTest");
		Packet playerResources = new Packet();
		playerResources.addUnit(new Unit(Resource.MONEY, 2));
		playerResources.addUnit(new Unit(Resource.WOOD, 5));
		player.increaseResource(playerResources);
		player.synchResource();
		
		//Build a card with a CouncilObtain effect
		
		//Build the card cost
		Packet cost = new Packet();
		cost.addUnit(new Unit(Resource.WOOD, 5));
		ArrayList<Packet> costs = new ArrayList<>();
		costs.add(cost);
		
		//Build the effects
		ArrayList<Effect> immediateEffects = new ArrayList<>();
		immediateEffects.add(buildEasyEffect());
		
		//Build the card
		card = new Card("provaCouncil", "desc", CardColor.BLUE, 2, 2, costs, immediateEffects, null, null, null);
		
	}
	
	private Effect buildEasyEffect(){
		
		ArrayList<Obtain> possibleConversion = new ArrayList<>();
		Packet gains1 = new Packet();
		Packet gains2 = new Packet();
		Packet gains3 = new Packet();
		
		gains1.addUnit(new Unit(Resource.MILITARYPOINT, 2));
		gains2.addUnit(new Unit(Resource.VICTORYPOINT, 2));
		gains3.addUnit(new Unit(Resource.WOOD, 2));
		
		possibleConversion.add(new Obtain( null, gains1));
		possibleConversion.add(new Obtain( null, gains2));
		possibleConversion.add(new Obtain( null, gains3));
		
		return new CouncilObtain( 2, possibleConversion);
	}
	
	@After
	public void finalCheck(){
		
		assertEquals(2, player.getResource(Resource.MONEY));
		assertEquals(7, player.getResource(Resource.WOOD));
		assertEquals(2, player.getResource(Resource.VICTORYPOINT));
		
	}
	
	@Test
	public void test() {
		
		setup();
		player.addCard(card);
		card.setPlayer(player);
		player.synchResource();
		
		assertEquals( 2, player.getResource(Resource.MONEY));
		assertEquals( 5, player.getResource(Resource.WOOD));
		
		try {
			
			card.enableImmediateEffect();
			CouncilRequest requestCouncil= player.getCouncilRequests().get(0);
			try {
				requestCouncil.addChoice(1);
				requestCouncil.addChoice(1);

				requestCouncil.apply(player);
				player.synchResource();
				
				finalCheck();
				
			} catch (WrongChoiceException e) {
				e.printStackTrace();
			}
			
			
		} catch (NotEnoughResourcesException e) {
			
			e.printStackTrace();
		}

		
	}

}
