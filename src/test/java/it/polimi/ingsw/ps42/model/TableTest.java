package it.polimi.ingsw.ps42.model;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.ps42.controller.cardCreator.CardsCreator;
import it.polimi.ingsw.ps42.controller.cardCreator.CardsFirstPeriod;
import it.polimi.ingsw.ps42.message.PlayerMove;
import it.polimi.ingsw.ps42.model.action.Action;
import it.polimi.ingsw.ps42.model.action.ActionCreator;
import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.enumeration.Response;
import it.polimi.ingsw.ps42.model.exception.ElementNotFoundException;
import it.polimi.ingsw.ps42.model.exception.FamiliarInWrongPosition;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.position.TowerPosition;
import it.polimi.ingsw.ps42.parser.BanLoader;

public class TableTest {
	
	private Table table;
	private Player p1;
	private Player p2;
	private Player p3;
	private Player p4;
	private CardsCreator cardCreator;
	private BanLoader banLoader;
	private Logger logger = Logger.getLogger(TableTest.class);
	
	@Before
	public void setUp() throws Exception {
		PropertyConfigurator.configure("Logger//Properties//test_log.properties");
		p1 = new Player("P1");
		p2 = new Player("P2");
		p3 = new Player("P3");
		p4 = new Player("P4");
		table = new Table(p1, p2, p3, p4);
		
		cardCreator = new CardsFirstPeriod();
		
		banLoader = new BanLoader("Resource//BansFile//firstPeriodBans.json");

	}

	@Test
	public void test() throws NotEnoughResourcesException, FamiliarInWrongPosition {
		p1.setFamiliarValue(FamiliarColor.ORANGE, 3);
		PlayerMove move = new PlayerMove(p1.getPlayerID(), ActionType.MARKET,FamiliarColor.ORANGE, 3, 0);
		Action action = new ActionCreator(p1, table, move, 1).getCreatedAction();
		assertTrue(Response.SUCCESS == action.checkAction());
		action.doAction();
		assertTrue(p1.getCouncilRequests().size() == 1);
		
		table.resetTable();
	}
	
	@Test
	public void test1(){
		//Test the throwDice method
		table.throwDice(new Random());
		checkFamiliarValue(FamiliarColor.BLACK);
		checkFamiliarValue(FamiliarColor.ORANGE);
		checkFamiliarValue(FamiliarColor.WHITE);
		
		//Test the placeCards method
		table.placeBlueTower(cardCreator.getNextBlueCards());
		table.placeGreenTower(cardCreator.getNextGreenCards());
		table.placeYellowTower(cardCreator.getNextYellowCards());
		table.placeVioletTower(cardCreator.getNextVioletCards());
		
		checkCardsPlaced(table.getBlueTower(), 1);
		checkCardsPlaced(table.getGreenTower(), 1);
		checkCardsPlaced(table.getVioletTower(), 1);
		checkCardsPlaced(table.getYellowTower(), 1);
		
		//Test the setBans method
		try{
			Effect ban = banLoader.getBan(1);
			table.addFirstBan(ban, 1);
			table.addSecondBan(ban, 1);
			table.addThirdBan(ban, 1);
			
			assertEquals(ban.getTypeOfEffect(), table.getFirstBan().getTypeOfEffect());
			assertEquals(ban.getTypeOfEffect(), table.getSecondBan().getTypeOfEffect());
			assertEquals(ban.getTypeOfEffect(), table.getThirdBan().getTypeOfEffect());
		}
		catch(ElementNotFoundException e){
			logger.error("Ban not Found while testing the Table");
			logger.info(e);
		}
	} 
	
	public void checkCardsPlaced(StaticList<TowerPosition> tower, int period){
		for (TowerPosition position : tower) {
			assertEquals(position.getCard().getPeriod(), period );
		}
	}
	
	public void checkFamiliarValue(FamiliarColor color){
		
		assertEquals(p1.getFamiliar(color).getValue(), p2.getFamiliar(color).getValue() );
		assertEquals(p2.getFamiliar(color).getValue(), p3.getFamiliar(color).getValue() );
		assertEquals(p3.getFamiliar(color).getValue(), p4.getFamiliar(color).getValue() );

	}

}
