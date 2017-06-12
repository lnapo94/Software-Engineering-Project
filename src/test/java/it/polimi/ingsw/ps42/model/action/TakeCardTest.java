package it.polimi.ingsw.ps42.model.action;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.ps42.message.RequestInterface;
import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.StaticList;
import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.enumeration.Response;
import it.polimi.ingsw.ps42.model.exception.FamiliarInWrongPosition;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.position.TowerPosition;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

public class TakeCardTest {
	
	private Player p1;
	Action takeCardAction;
	StaticList<TowerPosition> tower;

	@Before
	public void setUp() throws Exception {
		//Create the player
		p1 = new Player("Player 1");
		
		//Create one tower
		//With the lasts 2 floor with an effect
		Packet packet = new Packet();
		packet.addUnit(new Unit(Resource.MONEY, 2));
		Obtain bonus = new Obtain(null, packet, null);
		
		TowerPosition first = new TowerPosition(ActionType.TAKE_GREEN, 1, null, 0);
		TowerPosition second = new TowerPosition(ActionType.TAKE_GREEN, 3, null, 0);
		TowerPosition third = new TowerPosition(ActionType.TAKE_GREEN, 5, bonus, 0);
		TowerPosition fourth = new TowerPosition(ActionType.TAKE_GREEN, 7, bonus, 0);
		
		tower = new StaticList<>(4);
		tower.add(first);
		tower.add(second);
		tower.add(third);
		tower.add(fourth);
		
		//Set a familiar to play
		p1.setFamiliarValue(FamiliarColor.ORANGE, 5);
		
		//Create costs for the card
		Packet cost1 = new Packet();
		cost1.addUnit(new Unit(Resource.MONEY, 2));
		cost1.addUnit(new Unit(Resource.MILITARYPOINT, 2));
		
		Packet cost2 = new Packet();
		cost2.addUnit(new Unit(Resource.MONEY, 2)); 
		
		List<Packet> costs = new ArrayList<>();
		costs.add(cost1);
		costs.add(cost2);
		
		//Create one immediate effect for the card
		Packet effect1 = new Packet();
		effect1.addUnit(new Unit(Resource.MONEY, 2));
		Obtain immediateEffect1 = new Obtain(null, effect1, null);
		
		Packet effect2 = new Packet();
		effect2.addUnit(new Unit(Resource.MILITARYPOINT, 2));
		Obtain immediateEffect2 = new Obtain(effect2, effect2, null);
		
		List<Effect> immediateEffects = new ArrayList<>();
		immediateEffects.add(immediateEffect1);
		immediateEffects.add(immediateEffect2);
		
		//Create the card
		Card c = new Card("Card", "", CardColor.GREEN, 1, 3, costs, immediateEffects, null, null, null);
		Card useless = new Card("", "", CardColor.GREEN, 1, 3, null, null, null, null, null);
		
		//Add cards to tower
		third.setCard(c);
		first.setCard(useless);
		second.setCard(useless);
		fourth.setCard(useless);	

	}

	@Test
	public void test() {
		try {
			takeCardAction = new TakeCardAction(ActionType.TAKE_GREEN, p1.getFamiliar(FamiliarColor.ORANGE), tower, 2);
			Response checker = takeCardAction.checkAction();
			assertTrue(checker == Response.SUCCESS);
		} catch (NotEnoughResourcesException e) {
			System.out.println("Player hasn't enough resources");
		}
		List<RequestInterface> requests = p1.getRequests();
		
		//Control request
		if(!requests.isEmpty()) {
			//Now there is one request with only one cost
			//Player can enable it
			assertEquals(1, requests.size());
			for(RequestInterface request : requests) {
				//Control if there is only one possible cost
				assertEquals(1, request.showChoice().size());
				request.setChoice(0);
			}
		}
		//Here player has two money from the bonus position 
		assertEquals(2, p1.getResource(Resource.MONEY));
		try {
			takeCardAction.doAction();
			//Now player has 2 money, he payed two money for the card but he has earned two money from the position
			assertEquals(2, p1.getResource(Resource.MONEY));
		} catch (FamiliarInWrongPosition e) {
			System.out.println("ERROR");
		}
	}
	
}
