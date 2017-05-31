package it.polimi.ingsw.ps42.model;

import java.util.ArrayList;

import org.junit.Test;

import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

public class CardTest {

	@Test
	public void test() throws NotEnoughResourcesException {

		//Create a costs array
		Packet cost = new Packet();
		Packet cost2 = new Packet();

		
		// # CASE CARD COST = PLAYER RESOURCES  //
		//Create a costs array for card
		Packet costCard = new Packet();
		Packet costPlayer = new Packet();

		Unit unit = new Unit(Resource.MONEY, 10);
		Unit unit2 = new Unit(Resource.SLAVE, 5);
		
		ArrayList<Packet> costs = new ArrayList<>();

		costs.add(cost);
		costs.add(cost2);
		
		cost.addUnit(unit);
		cost2.addUnit(unit2);

		costs.add(costCard);
		
		costCard.addUnit(unit);
		costPlayer.addUnit(unit);


		//End creation
		
		//Create an obtain effect for the card
			//case 1 cart cost is = player resources
		ArrayList<Effect> effects = new ArrayList<>();
		Obtain obtain1 = new Obtain(costCard, costCard);
		Obtain obtain2 = new Obtain(null, costCard);
		
		effects.add(obtain1);
		effects.add(obtain2);
		
		//Create a card with a cost and a player who pays the card
		//card cost = player resources
		Card card = new Card("Prova", "description", CardColor.GREEN, 1, 3, 
				costs, effects, costs, null, null);
		
		Player p1 = new Player("CIAO");
		Player p2 = new Player("ciao2");
		p1.increaseResource(costPlayer);
		p1.synchResource();
		
		System.out.println("Player money: " + p1.getResource(Resource.MONEY));
		card.setPlayer(p1);
		try {
			card.payCard(p1, cost);
		} catch (NotEnoughResourcesException e1) {
			System.out.println("Unable to pay the card");
		}
		p1.synchResource();
		System.out.println("Player money: " + p1.getResource(Resource.MONEY));
		
		try {
			card.enableImmediateEffect();
		} catch (NotEnoughResourcesException e) {
			System.out.println("Card: \"" + card.getName() + "\" cannot be activated, you haven't enough resource");
		}
		System.out.println("Player money: " + p1.getResource(Resource.MONEY));
	
		card.enableImmediateEffect(1);
		p1.synchResource();


		System.out.println("Player money: " + p1.getResource(Resource.MONEY));
	}	


		
		// # CASE CARD COST < PLAYER RESOURCES  //
		
		
		
		
		costPlayer.addUnit(unit);
		

	
		
		//Create a card with a cost and a player who pays the card
		//card cost < player resources
		 card = new Card("Prova", "description", CardColor.GREEN, 1, 3, 
				costs, effects, null, null, null);
		
		 p1 = new Player("CIAO");
		 p2 = new Player("ciao2");
		p1.increaseResource(costPlayer);
		p1.synchResource();
		System.out.println("2# case: card cost = player resources");
		System.out.println("Player money: " + p1.getResource(Resource.MONEY));
		card.setPlayer(p1);
		card.payCard(0);
		p1.synchResource();
		System.out.println("Player money: " + p1.getResource(Resource.MONEY));
		
		try {
			card.enableImmediateEffect();
		} catch (NotEnoughResourcesException e) {
			System.out.println("Card: \"" + card.getName() + "\" cannot be activated, you haven't enough resource");
		}
		System.out.println("Player money: " + p1.getResource(Resource.MONEY));
	
		card.enableImmediateEffect(1);
		p1.synchResource();
		
		
		// # CASE CARD COST < PLAYER RESOURCES  //
		
		
		
		
		costCard.addUnit(unit);
		costCard.addUnit(unit);

			
				
	//Create a card with a cost and a player who pays the card
	//card cost < player resources
	 card = new Card("Prova", "description", CardColor.GREEN, 1, 3, 
			costs, effects, null, null, null);
	
	 p1 = new Player("CIAO");
	 p2 = new Player("ciao2");
	p1.increaseResource(costPlayer);
	p1.synchResource();
	System.out.println("2# case: card cost = player resources");
	System.out.println("Player money: " + p1.getResource(Resource.MONEY));
	card.setPlayer(p1);
	card.payCard(0);
	p1.synchResource();
	System.out.println("Player money: " + p1.getResource(Resource.MONEY));
	
	try {
		card.enableImmediateEffect();
	} catch (NotEnoughResourcesException e) {
		System.out.println("Card: \"" + card.getName() + "\" cannot be activated, you haven't enough resource");
	}
	System.out.println("Player money: " + p1.getResource(Resource.MONEY));

	card.enableImmediateEffect(1);
	p1.synchResource();
		
		
		
		
		
		

	}
}
	



