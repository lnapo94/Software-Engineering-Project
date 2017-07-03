package it.polimi.ingsw.ps42.message.visitorPattern;

import it.polimi.ingsw.ps42.message.BanMessage;
import it.polimi.ingsw.ps42.message.BanRequest;
import it.polimi.ingsw.ps42.message.BanUpdateMessage;
import it.polimi.ingsw.ps42.message.BonusBarMessage;
import it.polimi.ingsw.ps42.message.CancelCardRequest;
import it.polimi.ingsw.ps42.message.CardRequest;
import it.polimi.ingsw.ps42.message.CardUpdateMessage;
import it.polimi.ingsw.ps42.message.CardsMessage;
import it.polimi.ingsw.ps42.message.CouncilRequest;
import it.polimi.ingsw.ps42.message.DiceMessage;
import it.polimi.ingsw.ps42.message.EmptyMove;
import it.polimi.ingsw.ps42.message.FamiliarUpdateMessage;
import it.polimi.ingsw.ps42.message.LeaderCardMessage;
import it.polimi.ingsw.ps42.message.LeaderCardUpdateMessage;
import it.polimi.ingsw.ps42.message.PlayerMove;
import it.polimi.ingsw.ps42.message.PlayerToken;
import it.polimi.ingsw.ps42.message.ResourceUpdateMessage;
import it.polimi.ingsw.ps42.message.WinnerMessage;
import it.polimi.ingsw.ps42.message.leaderRequest.LeaderFamiliarRequest;

/**
 * Common interface for all the Visitors
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public interface Visitor {
	//All the methods to visit the messages
	
	public void visit(BonusBarMessage message);
	public void visit(LeaderCardMessage message);
	public void visit(BanMessage message);
	public void visit(DiceMessage message);
	public void visit(CardsMessage message);
	public void visit(ResourceUpdateMessage message);
	public void visit(FamiliarUpdateMessage message);
	public void visit(CardUpdateMessage message);
	public void visit(BanUpdateMessage message);
	public void visit(PlayerMove message);
	public void visit(CardRequest message);
	public void visit(CouncilRequest message);
	public void visit(PlayerToken message);
	public void visit(LeaderCardUpdateMessage message);
	public void visit(BanRequest message);
	public void visit(LeaderFamiliarRequest message);
	public void visit(EmptyMove message);
	public void visit(CancelCardRequest message);
	public void visit(WinnerMessage message);
}
