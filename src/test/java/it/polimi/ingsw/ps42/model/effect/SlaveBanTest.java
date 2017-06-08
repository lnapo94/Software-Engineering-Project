package it.polimi.ingsw.ps42.model.effect;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.ps42.message.RequestInterface;
import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

public class SlaveBanTest {

	private Player player;
	private Card card;
	private SlaveBan ban;
	private Familiar familiar;
	
	@Before
	public void setup(){
		//Build the player and add resources
		player = new Player("playerTest");
		Packet playerResources = new Packet();
		playerResources.addUnit(new Unit(Resource.MONEY, 6));
		playerResources.addUnit(new Unit(Resource.STONE, 2));
		playerResources.addUnit(new Unit(Resource.SLAVE, 4));
		player.increaseResource(playerResources);
		player.synchResource();
		
		//Build the card with a SlaveBan
		
		//Build the card costs
		Packet cost = new Packet();
		cost.addUnit(new Unit(Resource.SLAVE, 4));
		ArrayList<Packet> costs = new ArrayList<>();
		costs.add(cost);

		Packet cost2 = new Packet();
		cost2.addUnit(new Unit(Resource.MONEY, 4));
		costs.add(cost2);
		
		//Build the cards
		card = new Card("cardSlaveBan", "", CardColor.BLUE, 2 , 2, costs,null , null , null, null);
		
		ban = new SlaveBan(2);
	}
	
	
	@Test
	public void test() {
		
		ban.enableEffect(player);
		player.setBan( ban );
		//Get the player familiar

		familiar = player.getFamiliar(FamiliarColor.BLACK);
		assertEquals(FamiliarColor.BLACK, familiar.getColor());
		player.setFamiliarValue(FamiliarColor.BLACK, 2);
		assertEquals( 2, familiar.getValue());
		
		try {
			card.payCard(player, null);
			RequestInterface request = player.getRequests().get(0);
			request.setChoice(0);
			request.apply();
			
			assertEquals(2, player.getDivisory());
			
			
		} catch (NotEnoughResourcesException e) {
			e.printStackTrace();
		}
		
		
	}

}
