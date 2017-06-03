package it.polimi.ingsw.ps42.model.effect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.StaticList;
import it.polimi.ingsw.ps42.model.action.TakeCardAction;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.enumeration.Response;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.position.TowerPosition;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

public class IncreaseActionTest {
	
	private Player p1;
	private TakeCardAction action;
	@Before
	public void setUp() throws Exception {
		//Create the player
		p1 = new Player("Player 1");
		
		//Create the increaseAction effect
		Packet discount = new Packet();
		discount.addUnit(new Unit(Resource.MONEY, 2));
		IncreaseAction effect = new IncreaseAction(ActionType.TAKE_GREEN, 3, discount);
		
		//Assign this effect to player only enable the effect
		effect.enableEffect(p1);
		
		//Familiar now has 0 point
		p1.setFamiliarValue(FamiliarColor.WHITE, 0);
		
		//Create the tower
		TowerPosition first = new TowerPosition(ActionType.TAKE_GREEN, 1, null, 0);
		TowerPosition second = new TowerPosition(ActionType.TAKE_GREEN, 3, null, 0);
		TowerPosition third = new TowerPosition(ActionType.TAKE_GREEN, 5, null, 0);
		TowerPosition fourth = new TowerPosition(ActionType.TAKE_GREEN, 7, null, 0);
		
		StaticList<TowerPosition> tower = new StaticList<>(4);
		
		tower.add(first);
		tower.add(second);
		tower.add(third);
		tower.add(fourth);
		
		//Create a cost for the card
		Packet cost = new Packet();
		cost.addUnit(new Unit(Resource.MONEY, 2));
		ArrayList<Packet> costs = new ArrayList<>();
		costs.add(cost);
		//Add a card to tower
		Card c = new Card("", "", CardColor.GREEN, 0, 0, costs, null, null, null, null);
		first.setCard(c);
		second.setCard(c);
		third.setCard(c);
		fourth.setCard(c);
		
		
		//Create the action, player can take a card from the second floor
		action = new TakeCardAction(ActionType.TAKE_GREEN, p1.getFamiliar(FamiliarColor.WHITE), tower, 1);
	}

	@Test
	public void test() {
		//At this point, player has an IncreaseEffect, so he can take the card
		//from the first or the second floor
		assertEquals(0, action.getActionValue());
		Response checker = action.checkAction();
		action.doAction();
		assertTrue(checker == Response.SUCCESS);
		assertEquals(3, action.getActionValue());
		assertEquals(1, p1.getCardList(CardColor.GREEN).size());
	}

}
