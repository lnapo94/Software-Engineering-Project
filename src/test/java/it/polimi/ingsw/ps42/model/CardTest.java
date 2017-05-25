package it.polimi.ingsw.ps42.model;

import java.util.ArrayList;

import org.junit.Test;

import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.enumeration.Color;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

public class CardTest {

	@Test
	public void test() {
		//Create a costs array
		Packet cost = new Packet();
		Unit unit = new Unit(Resource.MONEY, 10);
		
		ArrayList<Packet> costs = new ArrayList<>();
		costs.add(cost);
		
		cost.addUnit(unit);
		//End creation
		
		//Create an obtain effect for the card
		ArrayList<Effect> effects = new ArrayList<>();
		Obtain obtain1 = new Obtain(cost, cost);
		Obtain obtain2 = new Obtain(null, cost);
		
		effects.add(obtain1);
		effects.add(obtain2);
		
		//Create a card with a cost and a player who pays the card
		Card card = new Card("Prova", "description", Color.GREEN, 1, 3, 
				costs, effects, null, null, null);
		
		Player p1 = new Player("CIAO");
		p1.increaseResource(cost);
		p1.synchResource();
		
		System.out.println("Player money: " + p1.getResource(Resource.MONEY));
		card.setPlayer(p1);
		card.payCard(0);
		//p1.synchResource();
		System.out.println("Player money: " + p1.getResource(Resource.MONEY));
		
		card.enableImmediateEffect();
		p1.synchResource();
		System.out.println("Player money: " + p1.getResource(Resource.MONEY));
				
	}

}
