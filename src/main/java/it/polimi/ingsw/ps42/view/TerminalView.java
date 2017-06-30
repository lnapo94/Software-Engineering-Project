package it.polimi.ingsw.ps42.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.message.CardRequest;
import it.polimi.ingsw.ps42.message.PlayerMove;
import it.polimi.ingsw.ps42.message.PlayerToken;
import it.polimi.ingsw.ps42.message.leaderRequest.LeaderFamiliarRequest;
import it.polimi.ingsw.ps42.model.Printable;
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
	
	private transient Logger logger = Logger.getLogger(TerminalView.class);
	
	public TerminalView() {
		
		super();
		scanner = new Scanner(System.in);
	}
	
	@Override
	protected void chooseBonusBar(List<BonusBar> bonusBarList) {
		System.out.println("Scegli una BonusBar per la partita [0-"+(bonusBarList.size()-1)+"]");
		for (BonusBar bonusBar : bonusBarList) {
			//Show possible BonusBar
			System.out.println(bonusBar.toString());
		}
		setBonusBarChoice(bonusBarList, Integer.parseInt(scanner.nextLine()));
	}

	@Override
	protected void chooseLeaderCard(List<LeaderCard> leaderCardList) {
		System.out.println("Scegli una Leader Card per la partita [0-"+(leaderCardList.size()-1)+"]");
		for (LeaderCard leaderCard : leaderCardList) {
			//Show possible LeaderCard
			System.out.println(leaderCard.toString());
		}
		this.setLeaderCardChoice(leaderCardList, Integer.parseInt(scanner.nextLine()));
	}

	@Override
	protected void chooseCouncilConversion(List<Obtain> possibleConversions, int quantity) {
		List<Integer> choiceList = new ArrayList<>();
		for(int i=0; i<quantity; i++){
			System.out.println("Scegli una Conversione per il privilegio del consiglio [0-"+(possibleConversions.size()-1)+"]");
			for (Obtain obtain : possibleConversions) {
				System.out.println(obtain.print());
			}			
		choiceList.add(Integer.parseInt(scanner.nextLine()));
		}
		this.setCouncilRequestResponse(possibleConversions, choiceList, quantity);
	}

	@Override
	protected void choosePlayerMove(ActionPrototype prototype, boolean isRetrasmission) {
		if(isRetrasmission)
			System.out.println("Mossa precedente errata, riprova con un'altra mossa");
		else
			System.out.println("Nuova mossa per il giocatore corrente");
		int increaseValue, position;
		ActionType moveType;
		FamiliarColor familiarColor;
		
		printPlayerResources( player);
		do{
			if(prototype != null)
				System.out.println("é una mossa bonus del tipo"+ prototype.getType().toString()+ 
									" livello: "+ prototype.getLevel());
			
			System.out.println("tipo mossa?");
			
			System.out.println(ActionType.COUNCIL+"\n "+ActionType.MARKET+"\n "+ActionType.PRODUCE+"\n "+ActionType.TAKE_ALL+"\n "
					+ActionType.TAKE_BLUE+"\n "+ActionType.TAKE_GREEN+"\n "+ActionType.TAKE_VIOLET+"\n "+ActionType.TAKE_YELLOW+
					"\n "+ActionType.YIELD);
			
			moveType = ActionType.parseInput(scanner.nextLine());
	
			System.out.println("colore familiare?");
	
			/*for (FamiliarColor color : FamiliarColor.values()) {
				if(!player.getFamiliar(color).isPositioned())
					System.out.println(color.toString());
			}
			*/
			familiarColor = FamiliarColor.parseInput(scanner.nextLine());
			System.out.println("posizione?");
			position = Integer.parseInt(scanner.nextLine());
			
			System.out.println("incremento del familiare?");
			increaseValue = Integer.parseInt(scanner.nextLine());
			
			showPosition(moveType, position);
			System.out.println("Confermi mossa?(si/no)");
		}
		while(scanner.nextLine().toUpperCase().equals("NO"));
		
		this.setNewMove(new PlayerMove(this.getViewPlayerID(), moveType, familiarColor, position, increaseValue));
	}

	@Override
	protected void chooseIfPayBan(int banPeriod) {
		System.out.println("Scegli se pagare una scomunica (si/no)");
		String response = scanner.nextLine();
		
		this.setPayBanResponse(response.toUpperCase().equals("SI"), banPeriod);
	}

	@Override
	protected void answerCardRequest(CardRequest message) {
		System.out.println("Risolvi una richiesta della carta [0-"+(message.getPossibleChoiceIndex().size()-1)+"]");
		for (Printable possibleChoice : message.showChoice()) {
			System.out.println(possibleChoice.toString());
		}
		this.setCardRequestResponse(message, Integer.parseInt(scanner.nextLine()));
	}

	@Override
	protected void notifyLeaderCardActivation() {
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
			logger.error("Player not found in terminalView");
			logger.info(e);
		}
	}
	private void printPlayerResources( Player p){
		System.out.println("Current resources:");
		for (Resource resource: Resource.values()) {
			
			System.out.println(resource.toString()+": "+p.getResource(resource));
		}
	}

	@Override
	protected void chooseFamiliarColor(LeaderFamiliarRequest message) {
		System.out.println("Choose a color for the familar increment");
		this.setLeaderFamiliarRequestResponse(FamiliarColor.parseInput(scanner.nextLine()), message);
	}

	@Override
	public void askNewPlayerID() {
		System.out.println("Insert player username");
		
		this.setNewPlayerID(scanner.nextLine());
	}
	
	private void showPosition(ActionType type, int position){
		System.out.println("CARTA SELEZIONATA: \n");
		System.out.println(table.showCard(type, position));
	}

	@Override
	protected void showResult(List<String> finalChart) {
		System.out.println("FINE DELLA PARTITA!");
		if(finalChart.get(0).equals(player.getPlayerID()))
			System.out.println("HAI VINTO!!!");
		else
			System.out.println("HAI PERSO...");
		System.out.println("Classifica finale:");
		for (String playerName: finalChart) {
			System.out.println(playerName);
		}
	}

	@Override
	protected void askIfWantToPlay(PlayerToken moveToken) {
		System.out.println("Vuoi fare una nuova mossa?(si/no)");
		if(scanner.nextLine().equalsIgnoreCase("SI")){
			this.choosePlayerMove(moveToken.getActionPrototype(), moveToken.isRetrasmission());
		}
		else
			this.setEmptyMove();
	}

}
