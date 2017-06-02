package it.polimi.ingsw.ps42.model.effect;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.request.RequestInterface;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

public class DoActionTest {

	private Player player;
	private Card card;
	
	@Before
	public void setup(){

		//Build the player and add resources
		player = new Player("playerTest");
		Packet playerResources = new Packet();
		playerResources.addUnit(new Unit(Resource.MONEY, 6));
		playerResources.addUnit(new Unit(Resource.WOOD, 2));
		player.increaseResource(playerResources);
		player.synchResource();
		//Build the cards with a DoAction Effect
		
		//Build the card costs
		Packet cost = new Packet();
		cost.addUnit(new Unit(Resource.MONEY, 2));
		ArrayList<Packet> costs = new ArrayList<>();
		costs.add(cost);
		
		//Build the effects
		ArrayList<Effect> immediateEffects = new ArrayList<>();
		immediateEffects.add(buildEasyEffect());
		
		//Build the card
		card = new Card("card test", "desc", CardColor.VIOLET, 2, 2, costs, immediateEffects, null , null , null);
		
	}

	private Effect buildEasyEffect(){
		
		Packet discount = new Packet();
		discount.addUnit(new Unit(Resource.MONEY, 2));
		return new DoAction( ActionType.MARKET, 2, discount);
	}
	@Test
	public void test() {
		
		setup();
		
		player.addCard(card);
		card.setPlayer(player);
		
		assertEquals( 2, player.getResource(Resource.WOOD));
		assertEquals( 6, player.getResource(Resource.MONEY));
		
		try {
			card.enableImmediateEffect();
			//TO-DO finire il test
			
		} catch (NotEnoughResourcesException e) {
	
			e.printStackTrace();
		}
		
	}

}
