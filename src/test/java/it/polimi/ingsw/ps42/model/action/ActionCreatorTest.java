package it.polimi.ingsw.ps42.model.action;

import static org.junit.Assert.*;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polimi.ingsw.ps42.message.PlayerMove;
import it.polimi.ingsw.ps42.model.Table;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.player.Player;

public class ActionCreatorTest {
	
	private Table table;
	private Player player;
	private PlayerMove move;
	private Action action;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		PropertyConfigurator.configure("Logger//Properties//test_log.properties");
	}

	@Before
	public void setUp() throws Exception {
		player = new Player("1");
		table = new Table(player, player);
		player.setFamiliarValue(FamiliarColor.ORANGE, 5);
		player.setFamiliarValue(FamiliarColor.BLACK, 5);
		player.setFamiliarValue(FamiliarColor.WHITE, 5);
		player.setFamiliarValue(FamiliarColor.NEUTRAL, 5);

	}

	@Test
	public void takeGreenCreationBonus() throws NotEnoughResourcesException {
		move = new PlayerMove(player.getPlayerID(), ActionType.TAKE_GREEN, null, 0, 0);
		action = new ActionCreator(player, table, move, 3).getCreatedAction();
		assertEquals(3, action.getActionValue());
		assertTrue(action.getType() == ActionType.TAKE_GREEN);
		assertTrue(action.isBonusAction());
	}
	
	@Test
	public void takeYellowCreationBonus() throws NotEnoughResourcesException {
		move = new PlayerMove(player.getPlayerID(), ActionType.TAKE_YELLOW, null, 0, 0);
		action = new ActionCreator(player, table, move, 3).getCreatedAction();
		assertEquals(3, action.getActionValue());
		assertTrue(action.getType() == ActionType.TAKE_YELLOW);
		assertTrue(action.isBonusAction());
	}
	
	@Test
	public void takeBlueCreationBonus() throws NotEnoughResourcesException {
		move = new PlayerMove(player.getPlayerID(), ActionType.TAKE_BLUE, null, 0, 0);
		action = new ActionCreator(player, table, move, 3).getCreatedAction();
		assertEquals(3, action.getActionValue());
		assertTrue(action.getType() == ActionType.TAKE_BLUE);
		assertTrue(action.isBonusAction());
	}
	
	@Test
	public void takeVioletCreationBonus() throws NotEnoughResourcesException {
		move = new PlayerMove(player.getPlayerID(), ActionType.TAKE_VIOLET, null, 0, 0);
		action = new ActionCreator(player, table, move, 3).getCreatedAction();
		assertEquals(3, action.getActionValue());
		assertTrue(action.getType() == ActionType.TAKE_VIOLET);
		assertTrue(action.isBonusAction());
	}
	
	@Test
	public void produceCreationBonus() throws NotEnoughResourcesException {
		move = new PlayerMove(player.getPlayerID(), ActionType.PRODUCE, null, 0, 0);
		action = new ActionCreator(player, table, move, 3).getCreatedAction();
		assertEquals(3, action.getActionValue());
		assertTrue(action.getType() == ActionType.PRODUCE);
		assertTrue(action.isBonusAction());
	}
	
	@Test
	public void yieldCreationBonus() throws NotEnoughResourcesException {
		move = new PlayerMove(player.getPlayerID(), ActionType.YIELD, null, 0, 0);
		action = new ActionCreator(player, table, move, 3).getCreatedAction();
		assertEquals(3, action.getActionValue());
		assertTrue(action.getType() == ActionType.YIELD);
		assertTrue(action.isBonusAction());
	}
	
	@Test
	public void marketCreationBonus() throws NotEnoughResourcesException {
		move = new PlayerMove(player.getPlayerID(), ActionType.MARKET, null, 0, 0);
		action = new ActionCreator(player, table, move, 3).getCreatedAction();
		assertEquals(1, action.getActionValue());
		assertTrue(action.getType() == ActionType.MARKET);
		assertTrue(action.isBonusAction());
	}
	
	@Test
	public void councilCreationBonus() throws NotEnoughResourcesException {
		move = new PlayerMove(player.getPlayerID(), ActionType.COUNCIL, null, 0, 0);
		action = new ActionCreator(player, table, move, 3).getCreatedAction();
		assertEquals(1, action.getActionValue());
		assertTrue(action.getType() == ActionType.COUNCIL);
		assertTrue(action.isBonusAction());
	}
	
	@Test
	public void takeGreenCreation() throws NotEnoughResourcesException {
		move = new PlayerMove(player.getPlayerID(), ActionType.TAKE_GREEN, FamiliarColor.ORANGE, 0, 0);
		action = new ActionCreator(player, table, move, 3).getCreatedAction();
		assertEquals(5, action.getActionValue());
		assertTrue(action.getType() == ActionType.TAKE_GREEN);
	}
	
	@Test
	public void takeYellowCreation() throws NotEnoughResourcesException {
		move = new PlayerMove(player.getPlayerID(), ActionType.TAKE_YELLOW, FamiliarColor.ORANGE, 0, 0);
		action = new ActionCreator(player, table, move, 3).getCreatedAction();
		assertEquals(5, action.getActionValue());
		assertTrue(action.getType() == ActionType.TAKE_YELLOW);
	}
	
	@Test
	public void takeBlueCreation() throws NotEnoughResourcesException {
		move = new PlayerMove(player.getPlayerID(), ActionType.TAKE_BLUE, FamiliarColor.ORANGE, 0, 0);
		action = new ActionCreator(player, table, move, 3).getCreatedAction();
		assertEquals(5, action.getActionValue());
		assertTrue(action.getType() == ActionType.TAKE_BLUE);
	}
	
	@Test
	public void takeVioletCreation() throws NotEnoughResourcesException {
		move = new PlayerMove(player.getPlayerID(), ActionType.TAKE_VIOLET, FamiliarColor.ORANGE, 0, 0);
		action = new ActionCreator(player, table, move, 3).getCreatedAction();
		assertEquals(5, action.getActionValue());
		assertTrue(action.getType() == ActionType.TAKE_VIOLET);
	}
	
	@Test
	public void produceCreation() throws NotEnoughResourcesException {
		move = new PlayerMove(player.getPlayerID(), ActionType.PRODUCE, FamiliarColor.ORANGE, 0, 0);
		action = new ActionCreator(player, table, move, 3).getCreatedAction();
		assertEquals(5, action.getActionValue());
		assertTrue(action.getType() == ActionType.PRODUCE);
	}
	
	@Test
	public void yieldCreation() throws NotEnoughResourcesException {
		move = new PlayerMove(player.getPlayerID(), ActionType.YIELD, FamiliarColor.ORANGE, 0, 0);
		action = new ActionCreator(player, table, move, 3).getCreatedAction();
		assertEquals(5, action.getActionValue());
		assertTrue(action.getType() == ActionType.YIELD);
	}
	
	@Test
	public void marketCreation() throws NotEnoughResourcesException {
		move = new PlayerMove(player.getPlayerID(), ActionType.MARKET, FamiliarColor.ORANGE, 0, 0);
		action = new ActionCreator(player, table, move, 3).getCreatedAction();
		assertEquals(5, action.getActionValue());
		assertTrue(action.getType() == ActionType.MARKET);
	}
	
	@Test
	public void councilCreation() throws NotEnoughResourcesException {
		move = new PlayerMove(player.getPlayerID(), ActionType.COUNCIL, FamiliarColor.ORANGE, 0, 0);
		action = new ActionCreator(player, table, move, 3).getCreatedAction();
		assertEquals(5, action.getActionValue());
		assertTrue(action.getType() == ActionType.COUNCIL);
	}

}
