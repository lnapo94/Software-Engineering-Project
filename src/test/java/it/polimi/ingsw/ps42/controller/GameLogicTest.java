package it.polimi.ingsw.ps42.controller;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;

import it.polimi.ingsw.ps42.model.exception.ElementNotFoundException;
import it.polimi.ingsw.ps42.model.exception.GameLogicError;
import it.polimi.ingsw.ps42.model.exception.NotEnoughPlayersException;
import it.polimi.ingsw.ps42.server.Server;
import it.polimi.ingsw.ps42.server.match.ServerView;
import it.polimi.ingsw.ps42.view.TerminalView;

/**
 * This class tests the main functionality of the GameLogic class:
 *  it tries to create a pair of View and a ServerView and then add them in the game
 *  
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class GameLogicTest {

	@Test
	public void test() throws NotEnoughPlayersException, GameLogicError, IOException, ElementNotFoundException {

		PropertyConfigurator.configure("Logger//Properties//test_log.properties"); 
		
		GameLogic logic;
		TerminalView view1 = new TerminalView();
		TerminalView view2 = new TerminalView();
		
		//Create the Views
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
		
		//Create the ServerView and the GameLogic and load the Game
		ServerView serverView = new ServerView(new Server());
		logic = new GameLogic(players, serverView);
		logic.loadGame();
	}

}
