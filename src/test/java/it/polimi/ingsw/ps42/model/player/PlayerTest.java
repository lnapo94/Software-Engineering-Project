package it.polimi.ingsw.ps42.model.player;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.effect.ForEachObtain;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

public class PlayerTest {

	@Test
	public void test() throws NotEnoughResourcesException {
		Player player = new Player("Player 1");
		BonusBar bonusBar = new BonusBar();
		player.setBonusBar(bonusBar);
		
		
		Unit requirement = new Unit(Resource.FAITHPOINT, 3);
		Unit gain = new Unit(Resource.MONEY, 5);
		
		Packet requirements = new Packet();
		requirements.addUnit(requirement);
		Packet gains = new Packet();
		gains.addUnit(gain);
		
		Effect effect = new ForEachObtain(requirements, gains);
		
		ArrayList<Effect> effects = new ArrayList<>();
		effects.add(effect);
		
		Card c = new Card("Prova", "description", CardColor.GREEN, 1, 3,
				null, effects, null, null, null);
		
		player.increaseResource(requirements);
		player.increaseResource(requirements);
		player.increaseResource(requirements);
		// check that resources won't be available till SynchResource method
		assertEquals("Expected 0 FAITHPOINT",0,player.getResource(Resource.FAITHPOINT));
		player.synchResource();
		assertEquals("Expected 9 FAITHPOINT",9,player.getResource(Resource.FAITHPOINT));
		assertEquals("Expected 0 MONEY",0,player.getResource(Resource.MONEY));
		c.setPlayer(player);
		player.addCard(c);
		c.enableImmediateEffect();
		player.synchResource();
		assertEquals("Expected 15 money", 15, player.getResource(Resource.MONEY));
		assertEquals("Expected 9 FAITHPOINT",9,player.getResource(Resource.FAITHPOINT));
		
		//NOW WITH 2 EFFECTS
		effects.add(effect);
		Card c2 = new Card("Prova", "description", CardColor.GREEN, 1, 3,
				null, effects, null, null, null); 
		player.addCard(c2);
		c2.setPlayer(player);
		assertEquals("Expected 9 FAITHPOINT",9,player.getResource(Resource.FAITHPOINT));
		c2.enableImmediateEffect(0);	
		player.synchResource();
		// first effect
		assertEquals("Expected 30 money", 30, player.getResource(Resource.MONEY));
		// second effect
		c2.enableImmediateEffect(1);	
		player.synchResource();
		assertEquals("Expected 45 money", 45, player.getResource(Resource.MONEY));
		
		player.decreaseResource(requirements);
		assertEquals("Expected 9 FAITHPOINT",9,player.getResource(Resource.FAITHPOINT));
		player.synchResource();
		assertEquals("Expected 6 FAITHPOINT",6,player.getResource(Resource.FAITHPOINT));
		
		// verify  all get method
		assertTrue("Get the id of the player", "Player 1" == player.getPlayerID());
		Familiar orange = new Familiar(player,FamiliarColor.ORANGE);
		assertEquals("familiar value",orange.getValue(),player.getFamiliar(FamiliarColor.ORANGE).getValue());
		assertEquals("Increment",orange.getIncrement(),player.getFamiliar(FamiliarColor.ORANGE).getIncrement());
		assertEquals("player ID ",orange.getPlayer().getPlayerID(),player.getFamiliar(FamiliarColor.ORANGE).getPlayer().getPlayerID());
		//set familiar value, increment and get the correct results (is it ok 18 ) ? 
		player.setFamiliarValue(FamiliarColor.ORANGE,18);
		assertEquals("Familiar value equal to",18, player.getFamiliarValue(FamiliarColor.ORANGE));
		//################# how to increase familiar value ? ###############
		
		//##################################################################
		//		ActionPrototype,divisory, enableBonus- should see action						   #							
		//##################################################################
		
		
		//################### all ban control ################
		player.setCanPlay(false);
		player.setNoMarketBan();
		player.disableBonusInTower();
		
		assertFalse(player.canTakeBonusFromTower());
		assertFalse(player.canStayInMarket());
		assertFalse(player.canPlay());
	
		
		
		
	}

}
