package it.polimi.ingsw.ps42.model;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.ps42.model.player.Player;

public class TableTest {
	
	Table table;

	@Before
	public void setUp() throws Exception {
		Player p1 = new Player("P1");
		Player p2 = new Player("P2");
		table = new Table(p1, p2, p1, p2);
	}

	@Test
	public void test() {
		assertTrue(true);
	}

}
