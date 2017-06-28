package it.polimi.ingsw.ps42.model;

import static org.junit.Assert.assertTrue;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.ps42.message.PlayerMove;
import it.polimi.ingsw.ps42.model.action.Action;
import it.polimi.ingsw.ps42.model.action.ActionCreator;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.enumeration.Response;
import it.polimi.ingsw.ps42.model.exception.FamiliarInWrongPosition;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.player.Player;

public class TableTest {
	
	Table table;
	Player p1;

	@Before
	public void setUp() throws Exception {
		PropertyConfigurator.configure("Logger//Properties//test_log.properties");
		p1 = new Player("P1");
		Player p2 = new Player("P2");
		table = new Table(p1, p2, p1, p2);
	}

	@Test
	public void test() throws NotEnoughResourcesException, FamiliarInWrongPosition {
		p1.setFamiliarValue(FamiliarColor.ORANGE, 3);
		PlayerMove move = new PlayerMove(p1.getPlayerID(), ActionType.MARKET,FamiliarColor.ORANGE, 3, 0);
		Action action = new ActionCreator(p1, table, move, 1).getCreatedAction();
		assertTrue(Response.SUCCESS == action.checkAction());
		action.doAction();
		assertTrue(p1.getCouncilRequests().size() == 1);
	}

}
