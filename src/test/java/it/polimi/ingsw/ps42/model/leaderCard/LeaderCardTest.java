package it.polimi.ingsw.ps42.model.leaderCard;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.effect.CanPositioningEverywhereLeader;
import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.effect.SetSingleFamiliarLeader;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.player.Player;

public class LeaderCardTest {

	LeaderCard requirementsCard;
	Player enableEffectPlayer;
	
	@BeforeClass
	public static void classSetUp() {
		PropertyConfigurator.configure("Logger//Properties//test_log.properties");
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
	
	private void createPlayer() {
		Card card = new Card("", "", CardColor.GREEN, 1, 1, null, null, null, null, null);
		
		enableEffectPlayer = new Player("Player requirements");
		
		for(int i = 0; i < 5; i++)
			enableEffectPlayer.addCard(card);
	}

	
	@Test
	public void enableAnEffectTest() {
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

}
