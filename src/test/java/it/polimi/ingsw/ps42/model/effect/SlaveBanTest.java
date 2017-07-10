package it.polimi.ingsw.ps42.model.effect;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polimi.ingsw.ps42.message.CardRequest;
import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

/**
 * This Testing Unit aims to test the SlaveBan effect, for this reason it tries
 * to enable that effect and verify if it is correctly activated
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class SlaveBanTest {

	private Player player;
	private Card card;
	private SlaveBan ban;
	private Familiar familiar;
	
	@BeforeClass
	public static void classSetUp() {
		PropertyConfigurator.configure("Logger//Properties//test_log.properties");
	}
	
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
	
	/**
	 * Apply a card effect with the Slave Ban effect to the current player
	 * and verify if the divisory is changed correctly
	 */
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
			CardRequest request = player.getRequests().get(0);
			request.setChoice(0);
			request.apply(player);
			
			assertEquals(2, player.getDivisory());
			
			
		} catch (NotEnoughResourcesException e) {
			e.printStackTrace();
		}
		
		
	}

}
