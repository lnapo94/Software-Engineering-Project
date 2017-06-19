package it.polimi.ingsw.ps42.model.action;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
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
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.exception.WrongChoiceException;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.position.MarketPosition;
import it.polimi.ingsw.ps42.message.CouncilRequest;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

public class MarketActionTest {

	private Player player;
	private Familiar familiar;
	private Familiar incrementedFamiliar;
	private StaticList<MarketPosition> tablePosition;
	private Action marketAction;
	private Action marketActionIncremented;
	private Action action;
	private CouncilObtain councilBonus;
	
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
		
		MarketPosition position1 = new MarketPosition(0, bonus1, 0, null);
		MarketPosition position2 = new MarketPosition(0, bonus2, 3, null);
		
		tablePosition = new StaticList<>(4);
		tablePosition.add(position1);
		tablePosition.add(position2);

		
	}
	
	@Before
	public void setupSimpleAction(){
		
		setup();		
		//Create an action that can be performed
		try {
			marketAction = new MarketAction(ActionType.MARKET, familiar , tablePosition, 0);
		} catch (NotEnoughResourcesException e) {
			e.printStackTrace();
		}
		
	}
	@Before
	public void setupFailAction(){
		
		setup();
		//Create an action that can not be performed since the player can not pay the familiar increment
		Familiar tempFamiliar = player.getFamiliar(FamiliarColor.ORANGE);
		tempFamiliar.setValue(1);
		tempFamiliar.setIncrement(7);
		try {
			action = new MarketAction(ActionType.MARKET, tempFamiliar, tablePosition, 1);
			assertTrue(false);
		} catch (NotEnoughResourcesException e) {
			assertTrue(true);

		}
	}
	
	@Before
	public void setupFamiliarIncrementAction(){
		
		setup();
		//Create an action with a familiar increment that can be performed
		try {
			marketActionIncremented = new MarketAction(ActionType.MARKET, incrementedFamiliar, tablePosition, 1);
		} catch (NotEnoughResourcesException e) {
			
			e.printStackTrace();
		}
		
	}
	
	@Before
	public void setupFamiliarIncrementFailAction() throws NotEnoughResourcesException{
		
		//Create an action with a familiar increment but that can not be performed because of a ban
		setup();
		Effect ban = new NoFirstActionBan();
		ban.enableEffect(player);
		action = new MarketAction(ActionType.MARKET, incrementedFamiliar, tablePosition, 1);
		
	}
	
	@Before
	public void setupLowLevelAction(){
		
		setup();
		//Create an action with a position level higher than the familiar
		MarketPosition position3 = new MarketPosition(4, null, 0, null);
		tablePosition.add(position3);
		try {
			action = new MarketAction(ActionType.MARKET, familiar, tablePosition, 2);
		} catch (NotEnoughResourcesException e) {
			e.printStackTrace();
		}
	}
	
	@Before 
	public void setupOccupiedPositionAction() throws FamiliarInWrongPosition, NotEnoughResourcesException {
		//Create a position occupied and try to perform an action on that position
		setup();
		MarketPosition position3 = new MarketPosition(0, null, 0, null);
		Familiar tempFamiliar = new Familiar(player, FamiliarColor.BLACK);
		
		position3.setFamiliar( tempFamiliar);
		tablePosition.add(position3);
		action = new MarketAction(ActionType.MARKET, familiar, tablePosition, 2);
	}
	
	@Before
	public void setupBanAction() throws NotEnoughResourcesException{
		
		//Create an action that can be performed but set the market ban to the player
		setup();
		Effect ban = new NoMarketBan();
		ban.enableEffect(player);
		
		action = new MarketAction(ActionType.MARKET, familiar, tablePosition, 0);
		
	}
	
	@Before
	public void setupBonusAction() throws NotEnoughResourcesException{
		//Create a bonus action
		setup();
		action = new MarketAction(ActionType.MARKET, player, tablePosition, 0, 0);
		
	}
	
	@Before
	public void setupIncreasedAction() throws NotEnoughResourcesException{
		//Add to the player an increase effect and check if the action can be performed
		setup();
		Effect increaseEffect = new IncreaseAction(ActionType.MARKET, 2, null);
		increaseEffect.enableEffect(player);
		
		action = new MarketAction(ActionType.MARKET, familiar, tablePosition, 1);
		
	}
	
	@Before
	public void setupCouncilObtainMarketAction() throws NotEnoughResourcesException{
		//Create a market position with a councilObtain bonus and perform an action on that position
		setup();

		MarketPosition position3 = new MarketPosition(0, null, 0, councilBonus);		
		tablePosition.add(position3);
		action = new MarketAction(ActionType.MARKET, familiar, tablePosition, 2);
		
	}
	
	@Before
	public void setupDoubleFamiliarAction() throws NotEnoughResourcesException, FamiliarInWrongPosition{
		
		//Do a simple action action (Familiar in pos 0 of Market)
		setupSimpleAction();
		
		//Enable the Player to place his familiar in occupied position
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
		marketAction = new MarketAction(ActionType.MARKET, player.getFamiliar(FamiliarColor.WHITE) , tablePosition, 0);
		
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
		assertEquals(2, player.getResource(Resource.SLAVE));
		assertEquals(0, player.getResource(Resource.MONEY));
		try {
			//Do the action and check the position bonus income (4 money)
			marketActionIncremented.doAction();
			assertEquals( 4, player.getResource(Resource.MONEY));
			assertEquals(2, player.getResource(Resource.SLAVE));
		} catch (FamiliarInWrongPosition e1) {
			e1.printStackTrace();
		}
		
		
		//Familiar Incremented Fail to Play Action Test
		try {
			setupFamiliarIncrementFailAction();
			assertEquals(Response.CANNOT_PLAY, action.checkAction());
			player.restoreResource();
			assertEquals(5, player.getResource(Resource.SLAVE));
			assertEquals(0, player.getResource(Resource.MONEY));
		} catch (NotEnoughResourcesException e1) {
			e1.printStackTrace();
		}
		
		
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
		} catch (NotEnoughResourcesException e) {
			e.printStackTrace();
		}
		
		
		//Market Ban Action Test
		try {
			setupBanAction();
			assertEquals( Response.FAILURE, action.checkAction());
			player.restoreResource();
			assertEquals(5, player.getResource(Resource.SLAVE));
			assertEquals(0, player.getResource(Resource.MONEY));
			//Nothing to do since the action do not passed the check
			
		} catch (NotEnoughResourcesException e) {
			e.printStackTrace();
		}
		
		
		//Market Bonus Action Test
		try {
			setupBonusAction();	
			assertEquals(Response.SUCCESS, action.checkAction());
			player.synchResource();
			assertEquals( 5, player.getResource(Resource.SLAVE));
			assertEquals(0 , player.getResource(Resource.MONEY));
			action.doAction();
			assertEquals(2 , player.getResource(Resource.MONEY));
		} catch (NotEnoughResourcesException | FamiliarInWrongPosition e) {
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
		} catch (NotEnoughResourcesException | FamiliarInWrongPosition e) {
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
		catch (NotEnoughResourcesException | FamiliarInWrongPosition | WrongChoiceException e) {
			e.printStackTrace();
		}
		
		//Double Action in the same position
		try {
			setupDoubleFamiliarAction();
			assertEquals(Response.SUCCESS, marketAction.checkAction());
		} catch (NotEnoughResourcesException | FamiliarInWrongPosition e) {
			e.printStackTrace();
		}
		
	}

}
