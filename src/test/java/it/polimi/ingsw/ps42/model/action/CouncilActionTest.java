package it.polimi.ingsw.ps42.model.action;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polimi.ingsw.ps42.message.CouncilRequest;
import it.polimi.ingsw.ps42.model.action.CouncilAction;
import it.polimi.ingsw.ps42.model.effect.CouncilObtain;
import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.effect.IncreaseAction;
import it.polimi.ingsw.ps42.model.effect.NoFirstActionBan;
import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.enumeration.Response;
import it.polimi.ingsw.ps42.model.exception.FamiliarInWrongPosition;
import it.polimi.ingsw.ps42.model.exception.WrongChoiceException;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.position.CouncilPosition;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

/**
 * This class tests the CouncilAction class:
 * it tries to create different kind of CouncilActions and verify that the 
 * checkAction() and doAction() methods are right 
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class CouncilActionTest {

	private Player player;
	private Familiar familiar;
	private List<CouncilPosition> tablePosition;
	private Action action;
	private CouncilObtain councilObtain;
	
	@BeforeClass
	public static void classSetUp() {
		PropertyConfigurator.configure("Logger//Properties//test_log.properties");
	}
	
	@Before
	public void setup(){
		
		//Create the player
		player = new Player("playerTest ");
		Packet resources = new Packet();
		resources.addUnit(new Unit(Resource.SLAVE, 3));
		player.increaseResource(resources);
		player.synchResource();
		
		//Setup the familiar
		familiar = player.getFamiliar(FamiliarColor.BLACK);
		familiar.setValue(1);
		
		//Create the CouncilObtain effect and the Conversion List:
		// obtain 2 money or 2 wood or (1 stone and 1 slave)
		List<Obtain> possibleConversion = new ArrayList<>();
		Packet gains1 = new Packet();
		gains1.addUnit(new Unit(Resource.MONEY, 2));
		Obtain conversion1 = new Obtain(null, gains1, null);
		
		Packet gains2 = new Packet();
		gains2.addUnit(new Unit(Resource.WOOD, 2));
		Obtain conversion2 = new Obtain(null, gains2, null);
		
		Packet gains3 = new Packet();
		gains3.addUnit(new Unit(Resource.STONE, 1));
		gains3.addUnit(new Unit(Resource.SLAVE, 1));
		Obtain conversion3 = new Obtain(null, gains3, null);
		
		possibleConversion.add(conversion1);
		possibleConversion.add(conversion2);
		possibleConversion.add(conversion3);
		
		councilObtain = new CouncilObtain( 1, possibleConversion);
		
		//Create the positions
		tablePosition = new ArrayList<>();
		CouncilPosition position1 = new CouncilPosition(0, null, 0, councilObtain);
		CouncilPosition position2 = position1.clone();
		tablePosition.add(position1);
		tablePosition.add(position2);
		
	}
	
	/**
	 * Create a simple council action
	 */
	public void setupSimpleAction() {
		setup();
		action = new CouncilAction(familiar, tablePosition.get(0));
		
		
	}
	
	/**
	 * Create a bonus council action
	 */
	public void setupBonusAction() {
		setup();
		action = new CouncilAction(player, tablePosition.get(1), 1);
		
	}
	
	/**
	 * Create an action that can not be performed since the player has a FirstPlayBan
	 */
	public void setupCannotPlayAction() {
		setup();
		Effect ban = new NoFirstActionBan();
		ban.enableEffect(player);
		familiar.setIncrement(1);
		action = new CouncilAction(familiar, tablePosition.get(0));
		
	}
	
	/**
	 * Create an action that can not be performed since the familiar has a low level
	 */
	public void setupLowLevelAction() {
		setup();
		Familiar neutral = player.getFamiliar(FamiliarColor.NEUTRAL);
		neutral.setValue(0);
		neutral.setIncrement(2);
		CouncilPosition tempPosition = new CouncilPosition(3, null, 0, councilObtain);
		tablePosition.add(tempPosition);
		action = new CouncilAction(neutral, tablePosition.get(2));
	}
	
	/**
	 * Create an Increase Action effect for the CouncilAction and do an Action that use that increment
	 */
	public void setupIncrementedAction() {
		setup();
		IncreaseAction increaseEffect = new IncreaseAction(ActionType.COUNCIL, 3, null);
		increaseEffect.enableEffect(player);
		familiar.setIncrement(1);
		Packet tempGains = new Packet();
		tempGains.addUnit(new Unit(Resource.MILITARYPOINT, 2));
		Obtain bouns = new Obtain(null, tempGains, null);
		CouncilPosition tempPosition = new CouncilPosition(4, bouns, 0, councilObtain);
		tablePosition.add(tempPosition);
		action = new CouncilAction(familiar, tablePosition.get(2));
	}
	
	@Test
	public void test() {

		//Test different kinds of council actions
		assertEquals(3, player.getResource(Resource.SLAVE));
		assertEquals(0, player.getResource(Resource.MONEY));
		assertEquals(0, player.getResource(Resource.STONE));
		assertEquals(0, player.getResource(Resource.WOOD));
				
		//Simple CouncilAction Test
		try {
			setupSimpleAction();
			assertEquals(Response.SUCCESS, action.checkAction());
			player.synchResource();
			assertEquals(3, player.getResource(Resource.SLAVE));
			action.doAction();
			CouncilRequest request = player.getCouncilRequests().get(0);
			assertEquals(1,request.getQuantity());
			request.addChoice(2);
			request.apply(player);
			player.synchResource();
			assertEquals(4, player.getResource(Resource.SLAVE));
			assertEquals(1, player.getResource(Resource.STONE));
		} catch (WrongChoiceException | FamiliarInWrongPosition e) {
			e.printStackTrace();
		}
		
		//Bonus CouncilAction Test
		try {
			setupBonusAction();
			assertEquals(Response.SUCCESS, action.checkAction());
			player.synchResource();
			assertEquals(3, player.getResource(Resource.SLAVE));
			action.doAction();
			CouncilRequest request = player.getCouncilRequests().get(0);
			assertEquals(1,request.getQuantity());
			request.addChoice(1);
			request.apply(player);
			player.synchResource();
			assertEquals(3, player.getResource(Resource.SLAVE));
			assertEquals(0, player.getResource(Resource.STONE));			
			assertEquals(2, player.getResource(Resource.WOOD));			

		} catch (FamiliarInWrongPosition | WrongChoiceException e) {
			e.printStackTrace();
		}
				
		setupCannotPlayAction();
		assertEquals(Response.CANNOT_PLAY, action.checkAction());
		player.restoreResource();
		assertEquals(3, player.getResource(Resource.SLAVE));
		//Nothing to do more since the action do not passed the check
		
		setupLowLevelAction();
		assertEquals(Response.LOW_LEVEL, action.checkAction());
		player.restoreResource();
		assertEquals(3, player.getResource(Resource.SLAVE));
		//Nothing to do more since the action do not passed the check
		
		//Incremented CouncilAction Test
		try {
			
			setupIncrementedAction();
			assertEquals(Response.SUCCESS, action.checkAction());
			player.synchResource();
			assertEquals( 3, player.getResource(Resource.SLAVE));
			assertEquals( 0, player.getResource(Resource.MILITARYPOINT));
			action.doAction();
			assertEquals(2, player.getResource(Resource.MILITARYPOINT));
			CouncilRequest request = player.getCouncilRequests().get(0);
			request.addChoice(0);
			request.apply(player);
			player.synchResource();
			assertEquals( 2, player.getResource(Resource.MONEY));
		} catch (FamiliarInWrongPosition | WrongChoiceException e) {
			e.printStackTrace();
		}
		
	}

}
