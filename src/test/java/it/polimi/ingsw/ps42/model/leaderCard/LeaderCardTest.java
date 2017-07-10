package it.polimi.ingsw.ps42.model.leaderCard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polimi.ingsw.ps42.message.leaderRequest.LeaderFamiliarRequest;
import it.polimi.ingsw.ps42.message.leaderRequest.LeaderRequest;
import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.effect.CanPositioningEverywhereLeader;
import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.effect.SetSingleFamiliarLeader;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

public class LeaderCardTest {

	private LeaderCard requirementsCard;
	private Player enableEffectPlayer;
	private LeaderCard onceATimeEffectLeaderCard;
	private static Logger logger;
	
	@BeforeClass
	public static void classSetUp() {
		PropertyConfigurator.configure("Logger//Properties//test_log.properties");
		logger = Logger.getLogger(LeaderCardTest.class);
	}
	
	@Before
	public void setUpSatisfyRequirementsTest() throws Exception {
		//Create a requirement for leader card
		HashMap<CardColor, Integer> cardRequirements = new HashMap<>();
		
		cardRequirements.put(CardColor.GREEN, 5);
		
		LeaderRequirements requirements = new LeaderRequirements(null, cardRequirements);
		
		//Create an effect for leadercard
		Effect permanentEffect = new CanPositioningEverywhereLeader();
		Effect increaseSingleFamiliar = new SetSingleFamiliarLeader(3);
		
		//Create a new LeaderCard
		requirementsCard = new LeaderCard("TryCard", "", requirements, increaseSingleFamiliar, permanentEffect, null);

		createPlayer();
	}
	

	@Before
	public void setUpOnceATimeEffectTest() {
		//This time give to the card a resource requirement
		Packet requirement = new Packet();
		requirement.addUnit(new Unit(Resource.VICTORYPOINT, 5));
		
		LeaderRequirements requirements = new LeaderRequirements(requirement, null);
		
		//Give to the player the resources he needs
		createPlayer();
		enableEffectPlayer.increaseResource(requirement.clone());
		enableEffectPlayer.synchResource();
		
		//Create a SetSingleFamiliarLeader effect
		Effect effect = new SetSingleFamiliarLeader(5);
		
		onceATimeEffectLeaderCard = new LeaderCard("Second Leader Cards", "", requirements, effect, null, null);
	}
	
	/**
	 * Method used to create the player with the correct resources
	 */
	private void createPlayer() {
		Card card = new Card("", "", CardColor.GREEN, 1, 1, null, null, null, null, null);
		
		enableEffectPlayer = new Player("Player requirements");
		
		for(int i = 0; i < 5; i++)
			enableEffectPlayer.addCard(card);
	}

	/**
	 * This test tries if the leader card requirement work well:
	 * The leader card has a requirement to satisfy, and the player has this requirement to take this card
	 */
	@Test
	public void enableAPermanentEffectTest() {
		logger.info("Enable permanent effect for the card: " + requirementsCard.toString());
		enableEffectPlayer.setLeaderCard(requirementsCard);
		requirementsCard.setOwner(enableEffectPlayer);
		
		assertEquals(1, enableEffectPlayer.getLeaderCardList().size());
		assertEquals(0, enableEffectPlayer.getActivatedLeaderCard().size());
		
		//From the view
		LeaderCard requirementsCardFromTheView = new LeaderCard("TryCard", null, null, null, null, null);
		
		if(requirementsCard.canEnableCard())
			enableEffectPlayer.enableLeaderCard(requirementsCardFromTheView);
		
		assertEquals(0, enableEffectPlayer.getLeaderCardList().size());
		assertEquals(1, enableEffectPlayer.getActivatedLeaderCard().size());
		
		//Requirements in player to satisfy
		assertEquals(1, enableEffectPlayer.getLeaderRequests().size());
		
	}
	
	/**
	 * This test give to a card a resource requirement this time, and tries if the 
	 * once a time effect works
	 */
	@Test
	public void enableOnceATimeEffectTest() {
		logger.info("Enable once a round effect for the card: " + onceATimeEffectLeaderCard.toString());
		enableEffectPlayer.setLeaderCard(onceATimeEffectLeaderCard);
		onceATimeEffectLeaderCard.setOwner(enableEffectPlayer);
		
		assertEquals(1, enableEffectPlayer.getLeaderCardList().size());
		assertEquals(0, enableEffectPlayer.getActivatedLeaderCard().size());
		
		//Control the value of neutral familiar
		assertEquals(0, enableEffectPlayer.getFamiliarValue(FamiliarColor.NEUTRAL));
		
		//Enable the card effect
		if(onceATimeEffectLeaderCard.canEnableCard())
			enableEffectPlayer.enableLeaderCard(onceATimeEffectLeaderCard);
		
		assertEquals(0, enableEffectPlayer.getLeaderCardList().size());
		assertEquals(1, enableEffectPlayer.getActivatedLeaderCard().size());
		
		//Control now if player has one leader request, due to the SetSingleFamiliarLeader effect
		assertFalse(enableEffectPlayer.isLeaderRequestEmpty());
		
		//Take the request and answers to it
		LeaderRequest request = enableEffectPlayer.removeLeaderRequest();
		
		//Now player hasn't leader request
		assertTrue(enableEffectPlayer.isLeaderRequestEmpty());
		
		//Control if the request is a familiar request and if it is, apply it
		if(request instanceof LeaderFamiliarRequest) {
			LeaderFamiliarRequest familiarRequest = (LeaderFamiliarRequest) request;
			familiarRequest.setFamiliarColor(FamiliarColor.ORANGE);
			familiarRequest.apply(enableEffectPlayer);
		}
		
		//Now control if the orange familiar of the player has 5 as its value
		
		assertEquals(5,enableEffectPlayer.getFamiliarValue(FamiliarColor.ORANGE));

	}
	
	/**
	 * This test tries to clone the card and verify if the cloned card is correct
	 */
	@Test
	public void cloneCard() {
		logger.info("Clone the \"" + onceATimeEffectLeaderCard.toString() + "\" card");
		
		//Test used to clone a leader card
		LeaderCard clonedCard = onceATimeEffectLeaderCard.clone();
		
		//Control if the cards names are equals
		clonedCard.equals(onceATimeEffectLeaderCard);
		
		//Verified if the two cards has the some once a round effect
		assertTrue(clonedCard.getOnceARoundEffect().getTypeOfEffect() == onceATimeEffectLeaderCard.getOnceARoundEffect().getTypeOfEffect());
	}

}
