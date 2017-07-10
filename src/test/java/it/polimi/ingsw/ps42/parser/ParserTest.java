package it.polimi.ingsw.ps42.parser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.exception.ElementNotFoundException;
import it.polimi.ingsw.ps42.model.leaderCard.LeaderCard;
import it.polimi.ingsw.ps42.model.player.BonusBar;

/**
 * This test aims to try all the functionalities of the loader classes in the parser package.
 * In this way it is known if all the files and all the methods to load the files work well
 * @author luca
 *
 */
public class ParserTest {

	private BanLoader banLoader;
	private BonusBarLoader bonusBarLoader;
	private CardLoader cardLoader; 
	private ConversionLoader conversionLoader;
	private FaithPathLoader faithPathLoader;
	private ImageLoader imageLoader;
	private LeaderCardLoader leaderCardLoader;
	private PositionLoader positionLoader;
	private TimerLoader timerLoader;
	
	private Logger logger = Logger.getLogger(ParserTest.class);
	
	/**
	 * Load all the created files
	 */
	@Before
	public void setup(){
		
		PropertyConfigurator.configure("Logger//Properties//test_log.properties");

		//Build all the file loader for the game
		try {
			banLoader = new BanLoader("Resource//BansFile//firstPeriodBans.json");
			
			bonusBarLoader = new BonusBarLoader("Resource//BonusBars//bonusBars.json");
			
			cardLoader = new CardLoader("Resource//Cards//FirstPeriod//blue.json" );
			
			conversionLoader = new ConversionLoader("Resource//Configuration//finalResourceConfiguration.json");
			
			faithPathLoader = new FaithPathLoader("Resource//Configuration//faithPointPathConfiguration.json");
			
			imageLoader = new ImageLoader("Resource//Configuration//imagePaths.json");
			
			leaderCardLoader = new LeaderCardLoader("Resource//LeaderCards//leaderCards.json");
			
			positionLoader = new PositionLoader("Resource//Position//CouncilPosition//councilPosition.json" );
			
			timerLoader = new TimerLoader("Resource//Configuration//timers.json");
			
		} catch (IOException e) {
			logger.error("Problems in Loader creation");
			logger.info(e);
		} 
		 
	}
	
	/**
	 * This method tests all the useful methods of the classes in parser package
	 */
	@Test
	public void test() {
		//Check the objects loaded from file
		try{
		
			checkBanLoader();
			
			checkBonusBarLoader();
			
			checkCardLoader();
			
			checkConversionLoader();
			
			checkFaithPathLoader();
			
			checkImageLoader();
			
			checkLeaderCardLoader();
			
			checkPositionLoader();
			
			checkTimerLoader();
		
		}
		catch(ElementNotFoundException | IOException e){
			logger.error("Problem in file reading");
			logger.info(e); 
			fail();
		}
		
	}
	
	/**
	 * Control if the timers are correct
	 */
	private void checkTimerLoader() {

		assertTrue(timerLoader.getPlayerMoveTimer() >= 0);
		assertTrue(timerLoader.getServerTimer() >= 0);
	}

	/**
	 * Control if all positions in file are correct, and if the positions loaded exist
	 * @throws IOException
	 */
	private void checkPositionLoader() throws IOException {

		assertTrue(positionLoader.getNextCouncilPosition() != null );
		positionLoader.setFileName("Resource//Position//MarketPosition//marketPosition.json");
		assertTrue(positionLoader.getNextMarketPosition() != null );
		positionLoader.setFileName("Resource//Position//TowerPosition//blueTowerPosition.json");
		assertTrue(positionLoader.getNextTowerPosition() != null );
		positionLoader.setFileName("Resource//Position//YieldAndProductPosition//firstPosition.json");
		assertTrue(positionLoader.getNextYieldAndProductPosition() != null );
		
	}

	/**
	 * Check if the leader card files contains the cards
	 */
	private void checkLeaderCardLoader() {

		List<LeaderCard> deck = leaderCardLoader.getLeaderCards();
		assertTrue(deck.size() >= 0);
	}

	/**
	 * Check if the loader of the pictures in /Resource/ folder is correct
	 */
	private void checkImageLoader() {
		 
		try{
			imageLoader.loadBanImage(1, 2);
			
			imageLoader.loadBonusFamiliarImage();
			imageLoader.loadFamiliarImage(1, FamiliarColor.BLACK);
			
			imageLoader.loadBlackDieImage(1);
			imageLoader.loadOrangeDieImage(2);
			imageLoader.loadWhiteDieImage(3);
			
			imageLoader.loadCardImage("Warlord");
			imageLoader.loadLeaderCardImage("Ludovico il Moro");
			
			imageLoader.loadResourceImage(Resource.MONEY);
			
		}
		catch(IOException e){
			fail();
		}
	}

	/**
	 * Check if there are an HashMap to convert the faith point in victory point
	 */
	private void checkFaithPathLoader() {

		assertTrue(faithPathLoader.conversion(2).getQuantity() >= 0);
	}

	/**
	 * Check if the conversion loader file is loaded
	 */
	private void checkConversionLoader() {

			assertTrue(conversionLoader.getBlueConversion(2).getQuantity() >= 0);
			assertTrue(conversionLoader.getGreenConversion(2).getQuantity() >= 0);
			assertTrue(conversionLoader.getOtherResourcesConversion(2).getQuantity() >= 0);
	}

	/**
	 * Control if the card loader loads effectively a deck of cards
	 */
	private void checkCardLoader() {

		List<Card> deck = cardLoader.getCards();
		assertEquals(8, deck.size());
	}

	/**
	 * Check if the bonus bar loader works correctly
	 */
	private void checkBonusBarLoader() {
		List<BonusBar> bonusBars = bonusBarLoader.getBonusBars();
		assertTrue(bonusBars.size() >= 4 );
	}

	/**
	 * Check if the ban loader works correctly, loading 3 casually ban of the bans file 
	 * @throws ElementNotFoundException		Thrown if there is a problem with the file
	 */
	private void checkBanLoader() throws ElementNotFoundException{
		
		assertTrue(banLoader.getBan(3) != null);
		
	}
}
