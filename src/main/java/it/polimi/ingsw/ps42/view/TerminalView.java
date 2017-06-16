package it.polimi.ingsw.ps42.view;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import it.polimi.ingsw.ps42.message.CardRequest;
import it.polimi.ingsw.ps42.message.PlayerMove;
import it.polimi.ingsw.ps42.message.leaderRequest.LeaderFamiliarRequest;
import it.polimi.ingsw.ps42.model.action.ActionPrototype;
import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.exception.ElementNotFoundException;
import it.polimi.ingsw.ps42.model.leaderCard.LeaderCard;
import it.polimi.ingsw.ps42.model.player.BonusBar;
import it.polimi.ingsw.ps42.model.player.Player;

public class TerminalView extends View {

	
	private Scanner scanner;
	
	public TerminalView() {
		
		super();
		scanner = new Scanner(System.in);
	}
	
	@Override
	protected int chooseBonusBar(List<BonusBar> bonusBarList) {
		System.out.println("Scegli una BonusBar per la partita [0-"+(bonusBarList.size()-1)+"]");
		return scanner.nextInt();
	}

	@Override
	protected int chooseLeaderCard(List<LeaderCard> leaderCardList) {
		System.out.println("Scegli una Leader Card per la partita [0-"+(leaderCardList.size()-1)+"]");
		return scanner.nextInt();
	}

	@Override
	protected int chooseCouncilConversion(List<Obtain> possibleConversions) {
		System.out.println("Scegli una Conversione per il privilegio del consiglio [0-"+(possibleConversions.size()-1)+"]");
		return scanner.nextInt();
	}

	@Override
	protected PlayerMove choosePlayerMove(ActionPrototype prototype) {
		System.out.println("Nuova mossa per il giocatore corrente");
		
		printPlayerResources( player);
		
		if(prototype != null)
			System.out.println("é una mossa bonus del tipo"+ prototype.getType().toString()+ 
								" livello: "+ prototype.getLevel());
		
		System.out.println("tipo mossa?");
		
		System.out.println(ActionType.COUNCIL+"\n "+ActionType.MARKET+"\n "+ActionType.PRODUCE+"\n "+ActionType.TAKE_ALL+"\n "
				+ActionType.TAKE_BLUE+"\n "+ActionType.TAKE_GREEN+"\n "+ActionType.TAKE_VIOLET+"\n "+ActionType.TAKE_YELLOW+
				"\n "+ActionType.YIELD);
		
		scanner.nextLine();
		ActionType moveType = ActionType.parseInput(scanner.nextLine());
		System.out.println("colore familiare?");

		System.out.println(FamiliarColor.BLACK+"\n "+FamiliarColor.NEUTRAL+"\n "+FamiliarColor.ORANGE+
							"\n "+FamiliarColor.WHITE);
		
		FamiliarColor familiarColor = FamiliarColor.parseInput(scanner.nextLine());
		System.out.println("posizione?");
		int position = scanner.nextInt();
		System.out.println("incremento del familiare?");
		int increaseValue= scanner.nextInt();
		
		
		return new PlayerMove(this.getViewPlayerID(), moveType, familiarColor, position, increaseValue);
	}

	@Override
	protected boolean chooseIfPayBan(int banPeriod) {
		System.out.println("Scegli se pagare una scomunica (si/no)");
		String response = scanner.nextLine();
		
		return response.toUpperCase().equals("SI");
	}

	@Override
	protected int answerCardRequest(CardRequest message) {
		System.out.println("Risolvi una richiesta della carta [0-"+(message.getPossibleChoiceIndex().size()-1)+"]");
		return scanner.nextInt();
	}

	@Override
	protected void notifyLeaderCardActiovation() {
		System.out.println("Il player corrente vuole attivare una carta leader");
		
	}

	@Override
	protected void notifyLeaderCardDiscard() {
		System.out.println("il player corrente vuole scartare una cata leader");
		
	}
	
	@Override
	public void setBlackDie(int value) {
			super.setBlackDie(value);
			System.out.println("Black familiar value: "+value);
	}
	
	@Override
	public void setOrangeDie(int value) {
			super.setOrangeDie(value);
			System.out.println("Orange familiar value: "+value);

	}
	
	@Override
	public void setWhiteDie(int value) {
			super.setWhiteDie(value);
			System.out.println("White familiar value: "+value);
	}
	
	@Override
	public void setResources(HashMap<Resource, Integer> resources, String playerID) {
		super.setResources(resources, playerID);
		try {
			printPlayerResources( searchPlayer(playerID));
		} catch (ElementNotFoundException e) {
			System.out.println("Player not found in terminalView");
		}
	}
	private void printPlayerResources( Player p){
		System.out.println("Current resources:");
		for (Resource resource: Resource.values()) {
			
			System.out.println(resource.toString()+": "+p.getResource(resource));
		}
	}

	@Override
	protected FamiliarColor chooseFamiliarColor(LeaderFamiliarRequest message) {
		System.out.println("Choose a color for the familar increment");
		return FamiliarColor.parseInput(scanner.nextLine());
	}

}
