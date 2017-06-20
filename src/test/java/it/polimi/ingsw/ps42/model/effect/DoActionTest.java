package it.polimi.ingsw.ps42.model.effect;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.ps42.message.CardRequest;
import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.action.ActionPrototype;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

public class DoActionTest {

	private Player player;
	private Card card;
	private Packet discount;
	
	@Before
	public void setup(){

		//Build the player and add resources
		player = new Player("playerTest");
		Packet playerResources = new Packet();
		playerResources.addUnit(new Unit(Resource.MONEY, 6));
		playerResources.addUnit(new Unit(Resource.WOOD, 2));
		player.increaseResource(playerResources);
		player.synchResource();
		
		//Build the discount
		discount = new Packet();
		discount.addUnit(new Unit(Resource.WOOD, 3));
		
		//Build the cards with a DoAction Effect
		
		//Build the card costs
		Packet cost1 = new Packet();
		cost1.addUnit(new Unit(Resource.MONEY, 2));
		ArrayList<Packet> costs = new ArrayList<>();
		costs.add(cost1);

		Packet cost2 = new Packet();
		cost2.addUnit(new Unit(Resource.WOOD, 5));
		costs.add(cost2);
		
		//Build the effects
		ArrayList<Effect> immediateEffects = new ArrayList<>();
		immediateEffects.add(buildEasyEffect());
		immediateEffects.add(buildEasyEffect());
		
		//Build the card
		card = new Card("card test", "desc", CardColor.VIOLET, 2, 2, costs, immediateEffects, null , null , null);
		
	}

	private Effect buildEasyEffect(){
		
		Packet discount = new Packet();
		discount.addUnit(new Unit(Resource.MONEY, 2));
		return new DoAction( ActionType.MARKET, 2, discount, null);
	}
	@Test
	public void test() {
		
		setup();

		assertEquals( 2, player.getResource(Resource.WOOD));
		assertEquals( 6, player.getResource(Resource.MONEY));
		
		try {
			//Pay the card cost that use the player discount
			card.payCard(player, discount);
			CardRequest payRequest = player.getRequests().get(0);
			payRequest.setChoice(1);
			payRequest.apply(player);
			card.setPlayer(player);
			player.addCard(card);
			player.synchResource();
			
			//Check player has correctly payed the card cost
			assertEquals( 0, player.getResource(Resource.WOOD));
			assertEquals( 6, player.getResource(Resource.MONEY));
			
			card.enableImmediateEffect();
			CardRequest effectRequest = player.getRequests().get(0);
			effectRequest.setChoice(1);
			effectRequest.apply(player);
			
			ActionPrototype bonusAction = player.getBonusAction();
			
			assertEquals(2 , bonusAction.getLevel());
			assertEquals(ActionType.MARKET, bonusAction.getType());
			
			
		} catch (NotEnoughResourcesException e) {
	
			e.printStackTrace();
		}
		
	}

}
