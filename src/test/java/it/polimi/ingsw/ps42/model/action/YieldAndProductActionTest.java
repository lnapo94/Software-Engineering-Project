package it.polimi.ingsw.ps42.model.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.effect.CanPositioningEverywhereLeader;
import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.enumeration.Response;
import it.polimi.ingsw.ps42.model.exception.FamiliarInWrongPosition;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.player.BonusBar;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.position.YieldAndProductPosition;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

public class YieldAndProductActionTest {
	
	private YieldAndProductPosition firstProductPosition;
	private List<YieldAndProductPosition> otherPositions;
	private Player player;
	private Player secondPlayer;
	
	@BeforeClass
	public static void classSetUp() {
		PropertyConfigurator.configure("Logger//Properties//test_log.properties");
	}

	@Before
	public void setUp() throws Exception {
		player = new Player("ID 1");
		secondPlayer = new Player("ID 2");
		
		BonusBar bar = new BonusBar();
		
		player.setBonusBar(bar);
		bar.setPlayer(player);
		
		BonusBar bar2 = new BonusBar();
		secondPlayer.setBonusBar(bar2);
		bar2.setPlayer(secondPlayer);
		
		createPositions();
		
		createSomeCards(player);
		createSomeCards(secondPlayer);
	}

	@Test
	public void test1() {
		//Test with cards that aren't activated
		try {
			Action action = new YieldAndProductAction(ActionType.PRODUCE, player, otherPositions, firstProductPosition, 1, 0);
			assertTrue(Response.SUCCESS == action.checkAction());
			action.doAction();
			player.synchResource();
			assertEquals(2, player.getResource(Resource.MONEY));
			assertEquals(1, player.getResource(Resource.MILITARYPOINT));
		} catch (NotEnoughResourcesException e) {
			fail();
		} catch (FamiliarInWrongPosition e) {
			fail();
		}
	}
	 
	@Test
	public void test2() {
		//In this test yellow cards are activated
		try {
			Action action = new YieldAndProductAction(ActionType.PRODUCE, player, otherPositions, firstProductPosition, 4, 0);
			assertTrue(Response.SUCCESS == action.checkAction());
			action.doAction();
			player.synchResource();
			assertEquals(4, player.getResource(Resource.MONEY));
			assertEquals(1, player.getResource(Resource.MILITARYPOINT));
		} catch (NotEnoughResourcesException e) {
			fail();
		} catch (FamiliarInWrongPosition e) {
			fail();
		}
	}

	@Test
	public void test3() {
		
		//First player can do an action, while second player can't do it because
		//he hasn't enough familiar value
		
		//Increase familiars in players
		player.setFamiliarValue(FamiliarColor.ORANGE, 4);
		secondPlayer.setFamiliarValue(FamiliarColor.ORANGE, 2);
		
		Action action;
		try {
			action = new YieldAndProductAction(ActionType.PRODUCE, player.getFamiliar(FamiliarColor.ORANGE), otherPositions, firstProductPosition);
			assertTrue(Response.SUCCESS == action.checkAction());
			action.doAction();
			player.synchResource();
			assertEquals(4, player.getResource(Resource.MONEY));
			assertEquals(1, player.getResource(Resource.MILITARYPOINT));
			
			action = new YieldAndProductAction(ActionType.PRODUCE, secondPlayer.getFamiliar(FamiliarColor.ORANGE), otherPositions, firstProductPosition);
			assertTrue(Response.LOW_LEVEL == action.checkAction());
		} catch (NotEnoughResourcesException | FamiliarInWrongPosition e) {
			fail();
		}

	}
	
	@Test
	public void test4(){
		
		//Add the can positioning everywhere bonus to the player and check if it works
		Effect effect = new CanPositioningEverywhereLeader();
		effect.enableEffect(player);

		//Increase familiars in players
		player.setFamiliarValue(FamiliarColor.ORANGE, 4);
		player.setFamiliarValue(FamiliarColor.NEUTRAL, 3);
		try {
			Action action = new YieldAndProductAction(ActionType.PRODUCE, player.getFamiliar(FamiliarColor.ORANGE), otherPositions, firstProductPosition);
			Response response = action.checkAction();
			if(response == Response.SUCCESS){
				action.doAction();
				//Verify the familiar is in the first position
				assertTrue(!firstProductPosition.isEmpty());
				Action secondAction  = new YieldAndProductAction(ActionType.PRODUCE, player.getFamiliar(FamiliarColor.NEUTRAL), otherPositions, firstProductPosition);
				Response secondResponse = secondAction.checkAction();
				if(secondResponse == Response.SUCCESS){
					//Verify the familiar is in the first position
					secondAction.doAction();
					assertEquals(1 ,firstProductPosition.getBonusFamiliar().size());
				}
				else{
					fail();
				}
			}
			else{
				fail();
			}
		} catch (NotEnoughResourcesException | FamiliarInWrongPosition e) {
			e.printStackTrace();
		}
		
		
	}
	
	private void createPositions() {
		//Create the first position
		firstProductPosition = new YieldAndProductPosition(ActionType.PRODUCE, 1, null, 0);
		
		otherPositions = new ArrayList<>();
		
		//Create the others positions (now only 4 positions)
		for(int i = 0; i < 4; i++)
			otherPositions.add(new YieldAndProductPosition(ActionType.PRODUCE, 1, null, 3));
	}
	
	private void createSomeCards(Player player) {
		
		//Create a permanent effect for the first yellow card
		Packet gainYellow = new Packet();
		gainYellow.addUnit(new Unit(Resource.MONEY, 2));
		
		//Create a permanent effect for the first green card
		Packet gainGreen = new Packet();
		gainGreen.addUnit(new Unit(Resource.MILITARYPOINT, 2));
		
		//Create effect for the yellow card
		List<Effect> permanentYellow = new ArrayList<>();
		permanentYellow.add(new Obtain(null, gainYellow, null));
		
		//Create effect for the green card
		List<Effect> permanentGreen = new ArrayList<>();
		permanentGreen.add(new Obtain(null, gainGreen, null));
		
		Card yellow = new Card("YELLOw", "", CardColor.YELLOW, 2, 4, null, null, null, permanentYellow, null);
		Card green = new Card("GREEN", "", CardColor.GREEN, 1, 4, null, null, null, permanentGreen, null);
		player.addCard(green);
		player.addCard(yellow);
		yellow.setPlayer(player);
		green.setPlayer(player);
		
	}
}
