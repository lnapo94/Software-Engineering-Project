package it.polimi.ingsw.ps42.controller;


import it.polimi.ingsw.ps42.controller.cardCreator.CardsCreator;
import it.polimi.ingsw.ps42.controller.cardCreator.CardsFirstPeriod;
import it.polimi.ingsw.ps42.message.LeaderCardMessage;
import it.polimi.ingsw.ps42.message.Message;
import it.polimi.ingsw.ps42.message.leaderRequest.LeaderRequest;
import it.polimi.ingsw.ps42.message.visitorPattern.ControllerVisitor;
import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.Table;
import it.polimi.ingsw.ps42.model.action.Action;
import it.polimi.ingsw.ps42.model.action.ActionPrototype;
import it.polimi.ingsw.ps42.model.exception.ElementNotFoundException;
import it.polimi.ingsw.ps42.model.exception.GameLogicError;
import it.polimi.ingsw.ps42.model.exception.NotEnoughPlayersException;
import it.polimi.ingsw.ps42.model.leaderCard.LeaderCard;
import it.polimi.ingsw.ps42.model.player.BonusBar;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.parser.BanLoader;
import it.polimi.ingsw.ps42.parser.BonusBarLoader;
import it.polimi.ingsw.ps42.parser.LeaderCardLoader;

import java.io.IOException;
import java.util.*;

public class GameLogic2 extends Observable {
    private static final int MAX_BANS_IN_FILE = 7;
    private static final int FAMILIARS_NUMBER = 4;

    private static final int THIRD_PERIOD = 2;
    private static final int FIRST_PERIOD = 0;
    private static final int SECOND_PERIOD = 1;

    //List of players in this match
    private List<Player> playersList;

    private List<Player> roundOrder;
    private List<Player> actionOrder;

    //Variables used in control flow
    private Player currentPlayer;
    private Action currentAction;
    private ActionPrototype bonusAction;

    private HashMap<Player, List<LeaderCard> > leaderCardTable;
    private List<BonusBar> bonusBarList;

    private Table table;
    private ControllerVisitor controllerVisitor;
    private CardsCreator cardsCreator;

    private int currentPeriod;

    private ServerView view;

    /** Private method used when GameLogic need to search the player
     *
     * @param playerID  the player ID String
     * @return Player   the player whose playerID is equal to the passed playerID
     */
    public Player searchPlayer(String playerID) throws ElementNotFoundException {
        for(Player player : playersList)
            if(player.getPlayerID() == playerID)
                return player;
        throw new ElementNotFoundException("The player isn't in GameLogic");
    }

    public GameLogic2(List<String> playerIDList, ServerView view) throws NotEnoughPlayersException, GameLogicError, IOException {
        playersList = new ArrayList<>();

        //Construct the real players
        for(String playerID : playerIDList)
            playersList.add(new Player(playerID));

        //Create the round order
        roundOrder = new ArrayList<>();

        for(Player player : playersList)
            roundOrder.add(player);

        //Create the action order for the first round
        actionOrder = new ArrayList<>();

        for(int i = 0; i < FAMILIARS_NUMBER; i++) {
            for(Player player : roundOrder)
                actionOrder.add(player);
        }

        //Create the table
        table = constructTable(playersList);

        //Load the three bans from a file
        try {
            BanLoader loader = new BanLoader("Resource//BansFile//firstPeriodBans.json");
            table.addFirstBan(loader.getBan(new Random().nextInt(MAX_BANS_IN_FILE)));

            loader.setFileName("Resource//BansFile//secondPeriodBans.json");
            table.addSecondBan(loader.getBan(new Random().nextInt(MAX_BANS_IN_FILE)));

            loader.setFileName("Resource//BansFile//thirdPeriodBans.json");
            table.addThirdBan(loader.getBan(new Random().nextInt(MAX_BANS_IN_FILE)));
            loader.close();
        } catch (IOException e) {

            System.out.println("Unable to open the ban file in GameLogic");
            throw new GameLogicError("File not found");

        } catch (ElementNotFoundException e) {

            System.out.println("Unable to find the correct ban");
            throw new GameLogicError("Ban not found");

        }

        //Construct the visitor to parse the message
        //TODO messageVisitor = new ControllerVisitor(this);

        //Initialize the view array
        this.view = view;
        //TODO this.view.addObserver(this);

        for(Player player : playersList)
            player.addObserver(view);
        table.addObserver(view);


        //Initialize the card creator
        cardsCreator = new CardsFirstPeriod();

        //Initialize the map
        leaderCardTable = new HashMap<>();
    }

    /**
     *
     * @param players                       The players used to construct the correct kind of table
     * @return Table                        The correct table for the match
     * @throws NotEnoughPlayersException    In case of the game logic hasn't a correct number of players
     */
    private Table constructTable(List<Player> players) throws NotEnoughPlayersException {
        if(players.size() == 2)
            return new Table(players.get(0), players.get(1));
        if(players.size() == 3)
            return new Table(players.get(0), players.get(1), players.get(2));
        if(players.size() == 4)
            return new Table(players.get(0), players.get(1), players.get(2), players.get(3));
        throw new NotEnoughPlayersException("There isn't enough player for this match");
    }

    private void loadLeaderCards() throws IOException {
        LeaderCardLoader loader = new LeaderCardLoader("Resource//LeaderCards//leaderCards.json");

        for(Player player : playersList)
            leaderCardTable.put(player, loader.getLeaderCards());

        loader.close();
    }

    private void loadBonusBars() throws IOException {
        BonusBarLoader loader = new BonusBarLoader("Resource//BonusBars//bonusBars.json");
        bonusBarList = loader.getBonusBars();
        loader.close();
    }

    /**
     * Used to start the game
     */
    public void loadGame() throws IOException {
        //First, load both bonus bars and leader cards
        loadBonusBars();
        loadLeaderCards();
        askBonusBar();
    }

    /**
     * Method used to ask to the next player what bonus bar he wants
     */
    private void askBonusBar() {
        if(!bonusBarList.isEmpty()) {
            currentPlayer = roundOrder.remove(0);
            currentPlayer.askChooseBonusBar(bonusBarList);
        }
        else {
            roundOrder.addAll(playersList);
            askLeaderCard();
        }
    }

    /**
     * Method used by visitor to set the bonusbar to the player
     * @param choice    Used to select the correct bonus bar
     */
    public void setBonusBar(int choice, String playerID) {
        if(playerID == currentPlayer.getPlayerID()) {
            currentPlayer.setBonusBar(bonusBarList.remove(choice));
            currentPlayer = null;
            askBonusBar();
        }
    }

    private void reOrderHashMap() {
        //Shift the map
        //Save the first deck of cards
        if(!leaderCardTable.isEmpty()) {
            List<LeaderCard> deck = leaderCardTable.get(playersList.get(0));

            //For all the players, copy the second cards in the first cards
            for (int i = 0; i < playersList.size() - 1; i++) {
                Player first = playersList.get(i);
                Player second = playersList.get(i + 1);

                List<LeaderCard> temporary = leaderCardTable.get(second);
                leaderCardTable.put(first, temporary);
            }
            //Finally copy the saved deck in the last position
            leaderCardTable.put(playersList.get(playersList.size() - 1), deck);
        }
    }

    private void askLeaderCard() {
        //If the player 1's arraylist of leader cards is empty, also the other must be empty, so end this procedure
        if(leaderCardTable.isEmpty()) {
            reOrderHashMap();
            for(Player player : roundOrder) {
                player.askChooseLeaderCard(leaderCardTable.get(player));
            }
        }
        else
            startMatch();
    }

    public void setLeaderCard(int choice, String playerID) {
        try {
            Player player = searchPlayer(playerID);

            //Control if the message is uncorrect
            if(choice > leaderCardTable.get(player).size()) {
                //Retrasmit
                Message message = new LeaderCardMessage(player.getPlayerID(), leaderCardTable.get(player));
                message.setRetrasmission();
                player.retrasmitMessage(message);
            }
            else {
                player.setLeaderCard(leaderCardTable.get(player).remove(choice));
                if (leaderCardTable.get(player).isEmpty())
                    leaderCardTable.remove(player);

                //Remove the player from the roundOrder and call askLeaderCard if this player is the last
                roundOrder.remove(player);

                if (roundOrder.isEmpty()) {
                    roundOrder.addAll(playersList);
                    askLeaderCard();
                }
            }
        } catch (ElementNotFoundException e) {
            System.out.println("Unable to find the player in GameLogic");
        }
    }

    private void startMatch() {
        //Ready to start with the first period
        currentPeriod = 1;
        initRound();
    }

    private void initRound() {

        //Place the cards in the towers
        table.placeGreenTower(cardsCreator.getNextGreenCards());
        table.placeYellowTower(cardsCreator.getNextYellowCards());
        table.placeBlueTower(cardsCreator.getNextBlueCards());
        table.placeVioletTower(cardsCreator.getNextVioletCards());

        //Go to the next state
        try {
            cardsCreator = cardsCreator.nextState();
        } catch (IOException e) {
            System.out.println("Unable to open the cards file");
        }

        table.throwDice(new Random());


        for(Player player : playersList) {

            //Enable once a round effect of leader cards
            for(LeaderCard card : player.getActivatedLeaderCard())
                card.enableOnceARoundEffect();

            //Control for some request
            List<LeaderRequest> leaderRequests = player.getLeaderRequests();

            if(!leaderRequests.isEmpty())
                player.askLeaderRequest(leaderRequests);
            else
                roundOrder.remove(player);
        }

        if(roundOrder.isEmpty()) {
            roundOrder.addAll(playersList);
            initAction();
        }
    }

    private void initAction() {
        
    }





}
