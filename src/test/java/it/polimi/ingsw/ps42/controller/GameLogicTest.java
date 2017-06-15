package it.polimi.ingsw.ps42.controller;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.Table;
import it.polimi.ingsw.ps42.model.exception.ElementNotFoundException;
import it.polimi.ingsw.ps42.model.exception.GameLogicError;
import it.polimi.ingsw.ps42.model.exception.NotEnoughPlayersException;
import it.polimi.ingsw.ps42.model.player.Player;

public class GameLogicTest {

	@Test
	public void test() throws NotEnoughPlayersException, GameLogicError, IOException, ElementNotFoundException {
		GameLogic logic;
		Player p1 = new Player("Player 1");
		Player p2 = new Player("Player 2");
		
		List<String> players = new ArrayList<>();
		players.add(p1.getPlayerID());
		players.add(p2.getPlayerID());
		
		logic = new GameLogic(players);
		
		Table table = logic.getTable();
		
		assertEquals(4, table.getBlueTower().size());
		
		logic.initGame();
		
		Card card = table.getBlueTower().get(3).getCard();
		System.out.println(card.toString());
	}

}
