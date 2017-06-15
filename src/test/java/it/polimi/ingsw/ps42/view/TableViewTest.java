package it.polimi.ingsw.ps42.view;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.ps42.model.effect.CardBan;
import it.polimi.ingsw.ps42.model.effect.CardCostBan;
import it.polimi.ingsw.ps42.model.effect.ObtainBan;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

public class TableViewTest {

	private TableView table;
	private List<Player> players;
	
	@Before
	public void setup() {

		//Create the Players 
		players = new ArrayList<>();
		
		for(int i=0; i<4; i++){
			Player player = new Player("testPlayer"+i);
			players.add(player);
		}
		
		//Build the table with different numbers of players
		table = new TableView(players.get(0), players.get(1), players.get(2), players.get(3));
		
		
	}
	
	public void testDice(){
		
		table.setBlackDie(2);
		table.setOrangeDie(3);
		table.setWhiteDie(4);
		assertEquals(2,table.getBlackDie());
		assertEquals(3,table.getOrangeDie());
		assertEquals(4,table.getWhiteDie());
		
		for (Player player : players) {
			assertEquals(2,player.getFamiliarValue(FamiliarColor.BLACK));
			assertEquals(3,player.getFamiliarValue(FamiliarColor.ORANGE));
			assertEquals(4,player.getFamiliarValue(FamiliarColor.WHITE));

		}
	}
	
	public void testBanMethods(){
		
		table.addFirstBan(new CardBan(CardColor.BLUE));
		table.addSecondBan(new ObtainBan(new Unit(Resource.MONEY, 1)));
		table.addThirdBan(new CardCostBan(CardColor.YELLOW));
		
		table.setPlayerFirstBan("testPlayer1");
		table.setPlayerFirstBan("testPlayer2");
		table.setPlayerFirstBan("testPlayer3");
		
		//assertTrue(players.get(0).getPlayerID().equals(table.getPlayersWithFirstBan().get(0)));
	}
	
	@Test
	public void test() {

		setup();
		
		testDice();
		
		testBanMethods();
	
	}

}
