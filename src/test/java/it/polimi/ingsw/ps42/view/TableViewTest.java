package it.polimi.ingsw.ps42.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.StaticList;
import it.polimi.ingsw.ps42.model.effect.CardBan;
import it.polimi.ingsw.ps42.model.effect.CardCostBan;
import it.polimi.ingsw.ps42.model.effect.ObtainBan;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.exception.ElementNotFoundException;
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
		
		table.setPlayerFirstBan("testPlayer0");
		table.setPlayerFirstBan("testPlayer1");
		table.setPlayerSecondBan("testPlayer1");
		table.setPlayerThirdBan("testPlayer2");
		
		List<String> bannedPlayers = table.getPlayersWithFirstBan();
		assertTrue(bannedPlayers.get(0).equals(players.get(0).getPlayerID()));
		assertTrue(bannedPlayers.get(1).equals(players.get(1).getPlayerID()));

		bannedPlayers = table.getPlayersWithSecondBan();
		assertTrue(bannedPlayers.get(0).equals(players.get(1).getPlayerID()));

		bannedPlayers = table.getPlayersWithThirdBan();
		assertTrue(bannedPlayers.get(0).equals(players.get(2).getPlayerID()));

	}
	
	public void testFamiliarSetter(){
		
		try {
			table.placeInBlueTower(players.get(0).getFamiliar(FamiliarColor.BLACK), 2);
			
			
		} catch (ElementNotFoundException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public void testCardGetters() throws ElementNotFoundException{
		
		//Create 4 Cards 
		StaticList<Card> deck = new StaticList<>(4);
		
		for(int i=0; i<4; i++){
			
			Card temp = new Card("testCard"+i, "", CardColor.GREEN, 1, 3, null, null, null, null, null);
			deck.add(temp);
		}
		
		//Place the Cards in Table
		table.placeGreenTower(deck);
		
		//Get them back
		for (int i=0; i<4; i++){
			Card temp = table.getGreenCard(i);
			//Verify they have the same name
			assertTrue(temp.getName().equals(deck.get(i).getName()));
		}
		
		//Verify the Cards have been really removed from the Table
		try{
			table.getGreenCard(0);
		}
		catch(ElementNotFoundException e){
			assertTrue(true);
		}
		
	}
	
	@Test
	public void test() {

		setup();
		
		testDice();
		
		testBanMethods();
	
		testFamiliarSetter();
		
		try {
			testCardGetters();
		} catch (ElementNotFoundException e) {
			e.printStackTrace();
		}
		
	}

}
