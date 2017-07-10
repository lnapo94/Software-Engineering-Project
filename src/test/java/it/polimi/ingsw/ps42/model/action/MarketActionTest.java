package it.polimi.ingsw.ps42.model.action;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polimi.ingsw.ps42.model.StaticList;
import it.polimi.ingsw.ps42.model.effect.CanPositioningEverywhereLeader;
import it.polimi.ingsw.ps42.model.effect.CouncilObtain;
import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.effect.IncreaseAction;
import it.polimi.ingsw.ps42.model.effect.NoFirstActionBan;
import it.polimi.ingsw.ps42.model.effect.NoMarketBan;
import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.enumeration.Response;
import it.polimi.ingsw.ps42.model.exception.FamiliarInWrongPosition;
import it.polimi.ingsw.ps42.model.exception.WrongChoiceException;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.position.MarketPosition;
import it.polimi.ingsw.ps42.message.CouncilRequest;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

/**
 * This class tests the functionality of the MarketAction class, so it tries to perform 
 * different kinds of this Actions and verify the checkAction() and doAction() methods
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class MarketActionTest {

	private Player player;
	private Familiar familiar;
	private Familiar incrementedFamiliar;
	private StaticList<MarketPosition> tablePosition;
	private Action marketAction;
	private Action marketActionIncremented;
	private Action action;
	private CouncilObtain councilBonus;
	
	@BeforeClass
	public static void classSetUp() {
		PropertyConfigurator.configure("Logger//Properties//test_log.properties");
	}
	
	@Before
	public void setup(){
		
		//Create the player
		player = new Player("playerTest ");
		Packet resources = new Packet();
		resources.addUnit(new Unit(Resource.SLAVE, 5));
		player.increaseResource(resources);
		player.synchResource();
		
		//Setup the familiar
		familiar = player.getFamiliar(FamiliarColor.BLACK);
		familiar.setValue(1);
		
		incrementedFamiliar = player.getFamiliar(FamiliarColor.NEUTRAL);
		incrementedFamiliar.setIncrement(3);
		
		//Create the positions and their bonuses
		Packet gains1 = new Packet();
		gains1.addUnit(new Unit(Resource.MONEY, 2));

		Packet gains2 = new Packet();
		gains2.addUnit(new Unit(Resource.MONEY, 4));
		
		Obtain bonus1 = new Obtain(null, gains1, null);
		Obtain bonus2 = new Obtain(null, gains2, null);
		
		//Create the CouncilObtain Bonus
		List<Obtain> possibleConversion = new ArrayList<>();
		
		Packet councilGains1 = new Packet();
		councilGains1.addUnit(new Unit(Resource.MILITARYPOINT, 2));

		Packet councilGains2 = new Packet();
		councilGains2.addUnit(new Unit(Resource.FAITHPOINT, 2));
		
		Obtain councilBonus1 = new Obtain(null, councilGains1, null);
		Obtain councilBonus2 = new Obtain(null, councilGains2, null);
		possibleConversion.add(councilBonus1);
		possibleConversion.add(councilBonus2);
		
		councilBonus = new CouncilObtain(2, possibleConversion);
		
		//Create two market positions,
		//position1: obtain 2 money
		//position 2: obtain 4 money
		MarketPosition position1 = new MarketPosition(0, bonus1, 0, null);
		MarketPosition position2 = new MarketPosition(0, bonus2, 3, null);
		
		tablePosition = new StaticList<>(4);
		tablePosition.add(position1);
		tablePosition.add(position2);

		
	}
	
	/**
	 * Create an action that can be performed
	 */
	public void setupSimpleAction(){
		
		setup();		
		marketAction = new MarketAction(familiar , tablePosition, 0);
		
	}
	
	/**
	 * Create an action that can not be performed since the player can not pay the familiar increment
	 */
	public void setupFailAction(){
		
		setup();
		Familiar tempFamiliar = player.getFamiliar(FamiliarColor.ORANGE);
		tempFamiliar.setValue(1);
		tempFamiliar.setIncrement(7);
		action = new MarketAction(tempFamiliar, tablePosition, 1);
		assertTrue(true);
	}
	
	/**
	 * Create an action with a familiar increment that can be performed
	 */
	public void setupFamiliarIncrementAction(){
		
		setup();
		marketActionIncremented = new MarketAction(incrementedFamiliar, tablePosition, 1);
		
	}
	
	/**
	 * Create an action with a familiar increment but that can not be performed because of a ban
	 */
	public void setupFamiliarIncrementFailAction() {
		
		setup();
		Effect ban = new NoFirstActionBan();
		ban.enableEffect(player);
		action = new MarketAction(incrementedFamiliar, tablePosition, 1);
		
	}
	
	/**
	 * Create an action with a position level higher than the familiar
	 */
	public void setupLowLevelAction(){
		
		setup();
		MarketPosition position3 = new MarketPosition(4, null, 0, null);
		tablePosition.add(position3);
		action = new MarketAction(familiar, tablePosition, 2);
	}
	
	/**
	 * Create a position occupied and try to perform an action on that position
	 * @throws FamiliarInWrongPosition if the Familiar is in the wrong position
	 */
	public void setupOccupiedPositionAction() throws FamiliarInWrongPosition {
		setup();
		MarketPosition position3 = new MarketPosition(0, null, 0, null);
		Familiar tempFamiliar = new Familiar(player, FamiliarColor.BLACK);
		
		position3.setFamiliar( tempFamiliar);
		tablePosition.add(position3);
		action = new MarketAction(familiar, tablePosition, 2);
	}

	/**
	 * Create an action that can be performed but set the market ban to the player
	 */
	public void setupBanAction() {
		
		setup();
		Effect ban = new NoMarketBan();
		ban.enableEffect(player);
		
		action = new MarketAction(familiar, tablePosition, 0);
		
	}

	/**
	 * 	Create a bonus action
	 */
	public void setupBonusAction() {
		setup();
		action = new MarketAction(player, tablePosition, 0, 0);
		
	}
	
	/**
	 * 	Add to the player an increase effect and check if the action can be performed
	 */
	public void setupIncreasedAction() {
		setup();
		Effect increaseEffect = new IncreaseAction(ActionType.MARKET, 2, null);
		increaseEffect.enableEffect(player);
		
		action = new MarketAction(familiar, tablePosition, 1);
		
	}
	
	/**
	 * 	Create a market position with a councilObtain bonus and perform an action on that position
	 */
	public void setupCouncilObtainMarketAction() {
		setup();

		MarketPosition position3 = new MarketPosition(0, null, 0, councilBonus);		
		tablePosition.add(position3);
		action = new MarketAction(familiar, tablePosition, 2);
		
	}
	
	/**
	 * Do a simple action action (Familiar in position 0 of Market) and then 
	 * Enable the Player to place his familiar in occupied positions
	 * 
	 * @throws FamiliarInWrongPosition if the Familiar is in the wrong position
	 */
	public void setupDoubleFamiliarAction() throws FamiliarInWrongPosition {
		
		setupSimpleAction();
		
		Effect canPositioningEverywhere = new CanPositioningEverywhereLeader();
		canPositioningEverywhere.enableEffect(player);
		
		marketAction.checkAction();
		player.synchResource();
		marketAction.doAction();
		player.synchResource();

		//Verify Player Resources
		assertEquals(2, player.getResource(Resource.MONEY));
		//Do another action in the same position		
		player.setFamiliarValue(FamiliarColor.WHITE, 3);
		marketAction = new MarketAction(player.getFamiliar(FamiliarColor.WHITE) , tablePosition, 0);
		
	}
	
	
	@Test
	public void test() {
		//Test different kinds of market actions
		assertEquals(5, player.getResource(Resource.SLAVE));
		assertEquals(0, player.getResource(Resource.MONEY));
		
		//Simple Action Test
		setupSimpleAction();
		assertEquals(Response.SUCCESS, marketAction.checkAction());
		player.synchResource();
		assertEquals(5, player.getResource(Resource.SLAVE));
		assertEquals(0, player.getResource(Resource.MONEY));
		try {
			//Do the action and check the position bonus income (2 money)
			marketAction.doAction();
			assertEquals( 2, player.getResource(Resource.MONEY));
			assertEquals(5, player.getResource(Resource.SLAVE));
		} catch (FamiliarInWrongPosition e1) {
			e1.printStackTrace();
		}
		
		
		//Fail to create Action Test
		setupFailAction();
		player.synchResource();
		assertEquals(5, player.getResource(Resource.SLAVE));
		//Nothings to do since the action fails to create
		
		
		//Familiar Incremented Action Test
		setupFamiliarIncrementAction();
		assertEquals(Response.SUCCESS, marketActionIncremented.checkAction() );
		player.synchResource();
		assertEquals(5, player.getResource(Resource.SLAVE));
		assertEquals(0, player.getResource(Resource.MONEY));
		try {
			//Do the action and check the position bonus income (4 money)
			marketActionIncremented.doAction();
			assertEquals( 4, player.getResource(Resource.MONEY));
			assertEquals(5, player.getResource(Resource.SLAVE));
		} catch (FamiliarInWrongPosition e1) {
			e1.printStackTrace();
		}
		
		
		setupFamiliarIncrementFailAction();
		assertEquals(Response.CANNOT_PLAY, action.checkAction());
		player.restoreResource();
		assertEquals(5, player.getResource(Resource.SLAVE));
		assertEquals(0, player.getResource(Resource.MONEY));
		
		
		//Low level Familiar Action Test
		setupLowLevelAction();
		assertEquals(Response.LOW_LEVEL , action.checkAction());
		player.restoreResource();
		assertEquals(5, player.getResource(Resource.SLAVE));
		assertEquals(0, player.getResource(Resource.MONEY));
		//Nothing to do since the action do not passed the check
		
		
		//Occupied position Action Test
		try {
			
			setupOccupiedPositionAction();	
			assertEquals(Response.FAILURE, action.checkAction());

			player.restoreResource();
			assertEquals(5, player.getResource(Resource.SLAVE));
			assertEquals(0, player.getResource(Resource.MONEY));
			//Nothing to do since the action do not passed the check
			
		} catch (FamiliarInWrongPosition e) {
			e.printStackTrace();
		}
		
		
		setupBanAction();
		assertEquals( Response.FAILURE, action.checkAction());
		player.restoreResource();
		assertEquals(5, player.getResource(Resource.SLAVE));
		assertEquals(0, player.getResource(Resource.MONEY));
		//Nothing to do since the action do not passed the check
		
		
		//Market Bonus Action Test
		try {
			setupBonusAction();	
			assertEquals(Response.SUCCESS, action.checkAction());
			player.synchResource();
			assertEquals( 5, player.getResource(Resource.SLAVE));
			assertEquals(0 , player.getResource(Resource.MONEY));
			action.doAction();
			assertEquals(2 , player.getResource(Resource.MONEY));
		} catch (FamiliarInWrongPosition e) {
			e.printStackTrace();
		}
	
		
		//Increased by Increase Effect Action Test
		try {
			setupIncreasedAction();
			assertEquals(Response.SUCCESS, action.checkAction() );
			player.synchResource();
			assertEquals( 5, player.getResource(Resource.SLAVE));
			assertEquals( 0, player.getResource(Resource.MONEY));
			action.doAction();
			assertEquals(4 , player.getResource(Resource.MONEY));
		} catch (FamiliarInWrongPosition e) {
			e.printStackTrace();
		}
		
		//CouncilObtain effect for a Market Action Test
		try {
			setupCouncilObtainMarketAction();
			assertEquals(Response.SUCCESS, action.checkAction());
			player.synchResource();
			assertEquals( 5, player.getResource(Resource.SLAVE));
			assertEquals( 0, player.getResource(Resource.MONEY));
			assertEquals( 0, player.getResource(Resource.MILITARYPOINT));
			assertEquals( 0, player.getResource(Resource.FAITHPOINT));
			action.doAction();
			//Get the request and answer to it
			CouncilRequest request = player.getCouncilRequests().get(0);
			request.addChoice(0);
			request.addChoice(0);
			request.apply(player);
			player.synchResource();
			
			assertEquals( 5, player.getResource(Resource.SLAVE));
			assertEquals( 0, player.getResource(Resource.MONEY));
			assertEquals( 2, player.getResource(Resource.MILITARYPOINT));
			assertEquals( 2, player.getResource(Resource.FAITHPOINT));
		} 
		catch (FamiliarInWrongPosition | WrongChoiceException e) {
			e.printStackTrace();
		}
		
		//Double Action in the same position 
		try {
			setupDoubleFamiliarAction();
			assertEquals(Response.SUCCESS, marketAction.checkAction());
			player.synchResource();
			marketAction.doAction();
			player.synchResource();
			assertEquals(4, player.getResource(Resource.MONEY));
			assertEquals(player.getFamiliar(FamiliarColor.WHITE), tablePosition.get(0).getBonusFamiliar().get(0));
			
			//Reset the Familiar from their position and increment
			tablePosition.get(0).removeBonusFamiliars();
			tablePosition.get(0).removeFamiliar();
			
			assertEquals(0, player.getFamiliar(FamiliarColor.WHITE).getIncrement());
			assertEquals(0, player.getFamiliar(FamiliarColor.BLACK).getIncrement());
		} catch (FamiliarInWrongPosition e) {
			e.printStackTrace();
		}
		
		
	}

}
