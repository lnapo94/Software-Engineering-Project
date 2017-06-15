package it.polimi.ingsw.ps42.view;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.ps42.model.player.Player;

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
	
	@Test
	public void test() {

		setup();
		
	
	}

}
