package it.polimi.ingsw.ps42.controller;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.ps42.controller.cardCreator.CardsCreator;
import it.polimi.ingsw.ps42.controller.cardCreator.CardsFirstPeriod;
import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.StaticList;

public class CardsCreatorTest {

	private CardsCreator cardCreator;
	private StaticList<Card> blueCards;
	private StaticList<Card> greenCards;
	private StaticList<Card> yellowCards;
	private StaticList<Card> violetCards;
	
	private Logger logger = Logger.getLogger(CardsCreatorTest.class);
			
	@Before
	public void setup(){
		PropertyConfigurator.configure("Logger//Properties//test_log.properties");
		try {
			cardCreator = new CardsFirstPeriod();
		} catch (IOException e) {
			logger.error("Error in CardCreator creation");
			logger.info(e);
		}
	}
	
	public void verifyPeriod( int periodToCheck, StaticList<Card> deck){
		for (Card card : deck) {
			assertEquals(periodToCheck, card.getPeriod());
		}
	}
	
	@Test
	public void test() {
		
		//Load the First Round Cards and Verify their period
		blueCards = cardCreator.getNextBlueCards();
		greenCards = cardCreator.getNextGreenCards();
		yellowCards = cardCreator.getNextYellowCards();
		violetCards = cardCreator.getNextVioletCards();
		
		verifyPeriod(1, blueCards);
		verifyPeriod(1, greenCards);
		verifyPeriod(1, yellowCards);
		verifyPeriod(1, violetCards);
		
		try {
			cardCreator = cardCreator.nextState();
		} catch (IOException e) {
			logger.info(e);
			logger.error("Problems in opening Card file");
		}
		//Load the Second Round Cards and Verify their period
		blueCards = cardCreator.getNextBlueCards();
		greenCards = cardCreator.getNextGreenCards();
		yellowCards = cardCreator.getNextYellowCards();
		violetCards = cardCreator.getNextVioletCards();
		
		verifyPeriod(1, blueCards);
		verifyPeriod(1, greenCards);
		verifyPeriod(1, yellowCards);
		verifyPeriod(1, violetCards);
		
		try {
			cardCreator = cardCreator.nextState();
		} catch (IOException e) {
			logger.info(e);
			logger.error("Problems in opening Card file");
		}
		
		//Load the Third Round Cards and Verify their period
		blueCards = cardCreator.getNextBlueCards();
		greenCards = cardCreator.getNextGreenCards();
		yellowCards = cardCreator.getNextYellowCards();
		violetCards = cardCreator.getNextVioletCards();
				
		verifyPeriod(2, blueCards);
		verifyPeriod(2, greenCards);
		verifyPeriod(2, yellowCards);
		verifyPeriod(2, violetCards);
		
		try {
			cardCreator = cardCreator.nextState();
		} catch (IOException e) {
			logger.info(e);
			logger.error("Problems in opening Card file");
		}
		
		//Load the Fourth Round Cards and Verify their period
		blueCards = cardCreator.getNextBlueCards();
		greenCards = cardCreator.getNextGreenCards();
		yellowCards = cardCreator.getNextYellowCards();
		violetCards = cardCreator.getNextVioletCards();
				
		verifyPeriod(2, blueCards);
		verifyPeriod(2, greenCards);
		verifyPeriod(2, yellowCards);
		verifyPeriod(2, violetCards);
				
		try {
			cardCreator = cardCreator.nextState();
		} catch (IOException e) {
			logger.info(e);
			logger.error("Problems in opening Card file");
		}
		
		//Load the Fifth Round Cards and Verify their period
		blueCards = cardCreator.getNextBlueCards();
		greenCards = cardCreator.getNextGreenCards();
		yellowCards = cardCreator.getNextYellowCards();
		violetCards = cardCreator.getNextVioletCards();
				
		verifyPeriod(3, blueCards);
		verifyPeriod(3, greenCards);
		verifyPeriod(3, yellowCards);
		verifyPeriod(3, violetCards);
				
		try {
			cardCreator = cardCreator.nextState();
		} catch (IOException e) {
			logger.info(e);
			logger.error("Problems in opening Card file");
		}
		//Load the Sixth Round Cards and Verify their period
		blueCards = cardCreator.getNextBlueCards();
		greenCards = cardCreator.getNextGreenCards();
		yellowCards = cardCreator.getNextYellowCards();
		violetCards = cardCreator.getNextVioletCards();
				
		verifyPeriod(3, blueCards);
		verifyPeriod(3, greenCards);
		verifyPeriod(3, yellowCards);
		verifyPeriod(3, violetCards);
				
		try {
			cardCreator = cardCreator.nextState();
			assertTrue(cardCreator == null);
		} catch (IOException e) {
			logger.info(e);
			logger.error("Problems in closing Card file");
		}
	}

}
