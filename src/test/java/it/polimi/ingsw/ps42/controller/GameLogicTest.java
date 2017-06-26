package it.polimi.ingsw.ps42.controller;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import it.polimi.ingsw.ps42.model.exception.ElementNotFoundException;
import it.polimi.ingsw.ps42.model.exception.GameLogicError;
import it.polimi.ingsw.ps42.model.exception.NotEnoughPlayersException;
import it.polimi.ingsw.ps42.server.match.ServerView;
import it.polimi.ingsw.ps42.view.TerminalView;

public class GameLogicTest {

	@Test
	public void test() throws NotEnoughPlayersException, GameLogicError, IOException, ElementNotFoundException {
		GameLogic logic;
		TerminalView view1 = new TerminalView();
		TerminalView view2 = new TerminalView();
		
		view1.addPlayer("Player 1");
		view2.addPlayer("Player 2");
		
		ArrayList<String> players = new ArrayList<>();
		ArrayList<String> players1View = new ArrayList<>();
		ArrayList<String> players2View = new ArrayList<>();
		
		players.add(view1.getViewPlayerID());
		players.add(view2.getViewPlayerID());
		
		players1View.add(view1.getViewPlayerID());
		players1View.add(view2.getViewPlayerID());
		
		players2View.add(view1.getViewPlayerID());
		players2View.add(view2.getViewPlayerID());
		
		view1.createTable(players1View);
		view2.createTable(players2View);
		ServerView serverView = new ServerView();
		logic = new GameLogic(players, serverView);
		logic.loadGame();
	}

}
