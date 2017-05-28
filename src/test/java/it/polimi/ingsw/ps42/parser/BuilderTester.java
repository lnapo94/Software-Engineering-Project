package it.polimi.ingsw.ps42.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class BuilderTester {
	
	@Test
	public void builderTest() throws Exception {
		
		CardBuilder builder = new CardBuilder("prova.json");
		builder.addCard();
		builder.close();
		
	}

}
