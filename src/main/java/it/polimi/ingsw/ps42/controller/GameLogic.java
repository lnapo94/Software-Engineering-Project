package it.polimi.ingsw.ps42.controller;


import it.polimi.ingsw.ps42.controller.cardCreator.CardsCreator;
import it.polimi.ingsw.ps42.controller.cardCreator.CardsFirstPeriod;
import it.polimi.ingsw.ps42.message.BanUpdateMessage;
import it.polimi.ingsw.ps42.message.CardRequest;
import it.polimi.ingsw.ps42.message.CouncilRequest;
import it.polimi.ingsw.ps42.message.LeaderCardMessage;
import it.polimi.ingsw.ps42.message.Message;
import it.polimi.ingsw.ps42.message.PlayerToken;
import it.polimi.ingsw.ps42.message.WinnerMessage;
import it.polimi.ingsw.ps42.message.leaderRequest.LeaderFamiliarRequest;
import it.polimi.ingsw.ps42.message.visitorPattern.ControllerVisitor;
import it.polimi.ingsw.ps42.model.Table;
import it.polimi.ingsw.ps42.model.action.Action;
import it.polimi.ingsw.ps42.model.action.ActionPrototype;
import it.polimi.ingsw.ps42.model.action.TakeCardAction;
import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.enumeration.Response;
import it.polimi.ingsw.ps42.model.exception.ElementNotFoundException;
import it.polimi.ingsw.ps42.model.exception.FamiliarInWrongPosition;
import it.polimi.ingsw.ps42.model.exception.GameLogicError;
import it.polimi.ingsw.ps42.model.exception.NotEnoughPlayersException;
import it.polimi.ingsw.ps42.model.leaderCard.LeaderCard;
import it.polimi.ingsw.ps42.model.player.BonusBar;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.player.VictoryPointComparator;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;
import it.polimi.ingsw.ps42.parser.BanLoader;
import it.polimi.ingsw.ps42.parser.BonusBarLoader;
import it.polimi.ingsw.ps42.parser.ConversionLoader;
import it.polimi.ingsw.ps42.parser.FaithPathLoader;
import it.polimi.ingsw.ps42.parser.LeaderCardLoader;
import it.polimi.ingsw.ps42.server.match.ServerView;

import java.io.IOException;
import java.util.*;

import org.apache.log4j.Logger;

public class GameLogic implements Observer {
	
	private static final long TIMER_SECONDS = 5;
	
    private static final int MAX_BANS_IN_FILE = 7;
    private static final int FAMILIARS_NUMBER = 4;

    private static final int THIRD_PERIOD = 2;
    private static final int FIRST_PERIOD = 0;
    private static final int SECOND_PERIOD = 1;
    
    //Variable used to know if the gameLogic is starting the game
    private boolean isInitGame;

    //List of players in this match
    private List<Player> playersList;

    private List<Player> roundOrder;
    private List<Player> actionOrder;

    //Variables used in control flow
    private List<Player> playersWithRequest;
    private Player currentPlayer;
    private Action currentAction;
    private ActionPrototype bonusAction;

    private HashMap<Player, List<LeaderCard> > leaderCardTable;
    private List<BonusBar> bonusBarList;

    private Table table;
    private ControllerVisitor controllerVisitor;
    private CardsCreator cardsCreator;

    private int currentRound;

    private ServerView view;
    
    private HashMap<Player, Timer> timerTable;
    
    //Logger
    private transient Logger logger = Logger.getLogger(GameLogic.class);

    /** Private method used when GameLogic need to search the player
     *
     * @param playerID  the player ID String
     * @return Player   the player whose playerID is equal to the passed playerID
     */
    public Player searchPlayer(String playerID) throws ElementNotFoundException {
        for(Player player : playersList)
            if(player.getPlayerID().equals(playerID))
                return player;
        throw new ElementNotFoundException("The player isn't in GameLogic");
    }

    public GameLogic(List<String> playerIDList, ServerView view) throws NotEnoughPlayersException, GameLogicError, IOException {
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
        
        //Initialize playerWithRequest arrayList
        playersWithRequest = new ArrayList<>();

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

            logger.error("Unable to open the ban file in GameLogic");
            logger.info(e);
            throw new GameLogicError("File not found");

        } catch (ElementNotFoundException e) {

            logger.error("Unable to find the correct ban");
            logger.info(e);
            throw new GameLogicError("Ban not found");

        }

        //Construct the visitor to parse the message
        controllerVisitor = new ControllerVisitor(this);

        //Initialize the view array
        this.view = view;
        this.view.addObserver(this);

        for(Player player : playersList)
            player.addObserver(view);
        table.addObserver(view);


        //Initialize the card creator
        cardsCreator = new CardsFirstPeriod();

        //Initialize the map
        leaderCardTable = new HashMap<>();
        
        //Initialize the timer map
        timerTable = new HashMap<>();
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
    
    public boolean isInitGame() {
    	return this.isInitGame;
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
        isInitGame = true;
        askBonusBar();
    }

    /**
     * Method used to ask to the next player what bonus bar he wants
     */
    private synchronized void askBonusBar() {
        if(!roundOrder.isEmpty()) {
            currentPlayer = roundOrder.remove(0);
            currentPlayer.askChooseBonusBar(bonusBarList);
            
            //Set the timer
            Timer timer = new Timer();
            timer.schedule(new InitGameTimer(currentPlayer, this), TIMER_SECONDS * 1000);
            timerTable.put(currentPlayer, timer);
        }
        else {
            roundOrder.addAll(playersList);
            currentPlayer = null;
            askLeaderCard();
        }
    }

    /**
     * Method used by visitor to set the bonusbar to the player
     * @param choice    Used to select the correct bonus bar
     */
    public void setBonusBar(int choice, String playerID) {
        if(playerID.equals(currentPlayer.getPlayerID())) {
        	
        	//Stop the timer
        	timerTable.remove(currentPlayer).cancel();
        	
            currentPlayer.setBonusBar(bonusBarList.remove(choice));
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

    private synchronized void askLeaderCard() {
        //If the player 1's arraylist of leader cards is empty, also the other must be empty, so end this procedure
        if(!leaderCardTable.isEmpty()) {
            reOrderHashMap();
            for(Player player : roundOrder) {
                player.askChooseLeaderCard(leaderCardTable.get(player));

                //Set the timer
                Timer timer = new Timer();
                timer.schedule(new InitGameTimer(player, this), TIMER_SECONDS * 1000);
                timerTable.put(player, timer);
            }
        }
        else
            startMatch();
    }

    public void setLeaderCard(int choice, String playerID) {
        try {
            Player player = searchPlayer(playerID);
            
            if(roundOrder.contains(player)) {
	            //Control if the message is uncorrect
	            if(choice > leaderCardTable.get(player).size()) {
	                //Retrasmit
	                Message message = new LeaderCardMessage(player.getPlayerID(), leaderCardTable.get(player));
	                message.setRetrasmission();
	                player.retrasmitMessage(message);
	                logger.info("Retrasmit the message because isn't correct");
	            }
	            else {
	            	//Cancel the timer
	            	timerTable.get(player).cancel();
	            	
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
            }
        } catch (ElementNotFoundException e) {
            logger.error("Unable to find the player in GameLogic");
            logger.info(e);
        }
    }

    private void startMatch() {
        //Ready to start with the first round
    	isInitGame = false;
        currentRound = 1;
        initRound();
    }

    private void initRound() {
    	//Debug
    	System.out.println("Start the round number: " + getCurrentRound());
    	
        //Place the cards in the towers
        table.placeGreenTower(cardsCreator.getNextGreenCards());
        table.placeYellowTower(cardsCreator.getNextYellowCards());
        table.placeBlueTower(cardsCreator.getNextBlueCards());
        table.placeVioletTower(cardsCreator.getNextVioletCards());

        //Go to the next state
        try {
            cardsCreator = cardsCreator.nextState();
        } catch (IOException e) {
            logger.error("Unable to open the cards file");
            logger.info(e);
        }

        table.throwDice(new Random());


        for(Player player : playersList) {

            //Enable once a round effect of leader cards
            for(LeaderCard card : player.getActivatedLeaderCard())
                card.enableOnceARoundEffect();

            //Control for some request
            playersWithRequest.add(player);
            checkOtherPlayerLeaderRequest(player);
        }
        
        if(playersWithRequest.isEmpty()) {
            initAction();
        }
    }
    
    private void checkOtherPlayerLeaderRequest(Player player) {
    	if(!player.isLeaderRequestEmpty()) {
    		player.askLeaderRequest(player.removeLeaderRequest());

            //Set the timer
            Timer timer = new Timer();
            timer.schedule(new PlayerMoveTimer(player, this), TIMER_SECONDS * 1000);
            timerTable.put(player, timer);
    	}
    	else
    		playersWithRequest.remove(player);
    }
    
    public void handleLeaderFamiliarRequest(LeaderFamiliarRequest request) {
    	//Method used to manage the leader familiar request
    	try {
			Player player = searchPlayer(request.getPlayerID());
			//Control if the player has a request
			if(playersWithRequest.contains(player)) {
				
				timerTable.remove(player).cancel();
				
				request.apply(player);
				checkOtherPlayerLeaderRequest(player);
			}
			
			//If roundOrder is empty, then refill the array and start the round
			if(playersWithRequest.isEmpty()) {
				initAction();
			}
		} catch (ElementNotFoundException e) {
			logger.error("Unable to find the player in GameLogic");
			logger.info(e);
		}
    }
    
	//Private Method to control the ban for all the players
	private void checkBan(Effect ban, int faithPointToHave, int period) {
		
		for(Player player : this.playersList) {
			if(player.getResource(Resource.FAITHPOINT) < faithPointToHave) {
				//Create the message
				BanUpdateMessage message = new BanUpdateMessage(player.getPlayerID(), period);
				if(ban.getTypeOfEffect() == EffectType.OBTAIN_BAN || ban.getTypeOfEffect() == EffectType.INCREASE_FAMILIARS) {
					player.setBan(ban);
				}
				else {
					ban.enableEffect(player);
				}
				//Send the message
				player.notifyNewBan(message);
				
			}
			else {
				playersWithRequest.add(player);
				player.askIfPayTheBan(currentRound);

                //Set the timer
                Timer timer = new Timer();
                timer.schedule(new PlayerMoveTimer(player, this, true), TIMER_SECONDS * 1000);
                timerTable.put(player, timer);
			}
		}
	}
	
	private void endMatch() {
		//At the end of the match
		for(Player player : this.playersList) {
			//Enable the third ban
			if(player.getResource(Resource.FAITHPOINT) < 5) {
    			System.out.println("Check the third period ban");
				table.getThirdBan().enableEffect(player);
				BanUpdateMessage message = new BanUpdateMessage(player.getPlayerID(), THIRD_PERIOD);
				
				player.notifyNewBan(message);
			}
		}
		
		checkRequest();
	}
	
	private void calculateWinner() {
		
		for(Player player : this.playersList) {
			//Apply the victory point for the resources
			Packet victoryPoint;
			try {
				ConversionLoader loader = new ConversionLoader("Resource//Configuration//finalResourceConfiguration.json");
				victoryPoint = new Packet();
				victoryPoint.addUnit(loader.getGreenConversion(player.getCardList(CardColor.GREEN).size()));
				victoryPoint.addUnit(loader.getBlueConversion(player.getCardList(CardColor.BLUE).size()));
				
				//Counting the other resources
				int totalPlayerResources = player.getResource(Resource.WOOD) + player.getResource(Resource.MONEY) +
											player.getResource(Resource.STONE) + player.getResource(Resource.SLAVE);
				victoryPoint.addUnit(loader.getOtherResourcesConversion(totalPlayerResources));
				
				player.increaseResource(victoryPoint);
				player.synchResource();
				
				faithPathIncrease(player);
				
				loader.close();
			} catch (IOException e) {
				logger.error("Unable to open the conversion file");
				logger.info(e);
			}
		}
		
		//Reorder the playersList thanks to the comparator
		playersList.sort(new VictoryPointComparator());
		
		List<String> ranking = new ArrayList<>();
		
		for(Player player : playersList)
			ranking.add(player.getPlayerID());
		
		WinnerMessage message = new WinnerMessage(ranking.get(0), ranking);
		
		for(Player player : playersList)
			player.notifyRanking(message);
		
	}
	
	private void restartRound() {
		//Control the new order and reset the table
		List<Player> newOrder = table.resetTable();
		if(!newOrder.isEmpty()) {
			//Remove the newOrder player from the previous order
			for(Player player : newOrder) {
				roundOrder.remove(roundOrder.indexOf(player));
			}
					
			List<Player> temporary = new ArrayList<>();
			temporary.addAll(newOrder);
			temporary.addAll(roundOrder);
			roundOrder = temporary;
		}
		
		//Create a new Action order
		for(int i = 0; i < FAMILIARS_NUMBER; i++)
			actionOrder.addAll(roundOrder);
		
		//Now init a new round
		currentRound++;
		initRound();
	}

    public void initAction() {
    	if(currentPlayer != null && timerTable.containsKey(currentPlayer))
    		timerTable.remove(currentPlayer).cancel();
    		
    	if(!actionOrder.isEmpty()) {
    		currentPlayer = actionOrder.remove(0);
    		
    		//DEBUG
    		logger.debug("Player: " + currentPlayer.getPlayerID() + " is playing...");
    		currentPlayer.askMove();
    		
            //Set the timer
            Timer timer = new Timer();
            timer.schedule(new PlayerMoveTimer(currentPlayer, this), TIMER_SECONDS * 1000);
            timerTable.put(currentPlayer, timer);
    	}
    	else {
    		if(currentRound == 2) {
    			logger.debug("Check the first period ban");
    			checkBan(table.getFirstBan(), 3, FIRST_PERIOD);
    			
    			//If there isn't request, then restart with a new round
    			if(playersWithRequest.isEmpty()) {
    				restartRound();
    			}
    		}
    		else if(currentRound == 4) {
    			logger.debug("Check the second period ban");
    			checkBan(table.getSecondBan(), 4, SECOND_PERIOD);
    			
    			//If there isn't request, then restart with a new round
    			if(playersWithRequest.isEmpty()) {
    				restartRound();
    			}
    		}
    		else if(currentRound == 6) {
    			endMatch();
    		}
    		else
    			restartRound();
    	}
    }
    
    private void faithPathIncrease(Player player) throws IOException {
    	FaithPathLoader loader = new FaithPathLoader("Resource//Configuration//faithPointPathConfiguration.json");
		Packet victoryPoint = new Packet();
		victoryPoint.addUnit(loader.conversion(player.getResource(Resource.FAITHPOINT)));
		
		//If player has the leader card
		if(player.hasMoreVictoryPoint())
			victoryPoint.addUnit(new Unit(Resource.VICTORYPOINT, 5));
		
		player.setToZero(Resource.FAITHPOINT);
		player.increaseResource(victoryPoint);
		player.synchResource();
		
		loader.close();
    }

    public void handleBan(String playerID, int index, boolean wantToPayBan) {
    	try {
			Player player = searchPlayer(playerID);
			
			//Control if player has a pending request
			if(playersWithRequest.contains(player)) {
				
				timerTable.remove(player).cancel();
				
				if(wantToPayBan) {
					faithPathIncrease(player);
				}
				else {
					//Player wants the ban
					BanUpdateMessage message;
					if(index == FIRST_PERIOD) {
						//For the first period ban we have two kind of ban
						if(table.getFirstBan().getTypeOfEffect() == EffectType.OBTAIN_BAN || table.getFirstBan().getTypeOfEffect() == EffectType.INCREASE_FAMILIARS)
							player.setBan(table.getFirstBan());
						else
							table.getFirstBan().enableEffect(player);
						
						message = new BanUpdateMessage(player.getPlayerID(), FIRST_PERIOD);
						player.notifyNewBan(message);
					}
					if(index == SECOND_PERIOD) {
						table.getSecondBan().enableEffect(player);
						message = new BanUpdateMessage(player.getPlayerID(), SECOND_PERIOD);
						player.notifyNewBan(message);
					}
					if(index == THIRD_PERIOD) {
						table.getThirdBan().enableEffect(player);
						message = new BanUpdateMessage(player.getPlayerID(), THIRD_PERIOD);
						player.notifyNewBan(message);
					}
					
				}
			}
			
			playersWithRequest.remove(player);
			if(playersWithRequest.isEmpty())
				restartRound();
				
		} catch (ElementNotFoundException e) {
			logger.error("Player " + playerID + " cannot be found in handleBan");
			logger.info(e);
		} catch (IOException e) {
			logger.error("Cannot open the faithPath coversion table in GameLogic");
			logger.info(e);
		}
    }
    
    public synchronized void handleAction(Action action, String playerID) {
    	try {
			Player player = searchPlayer(playerID);
			if(player == currentPlayer) {
				
				if(bonusAction != null && !bonusAction.checkAction(action)) {
					PlayerToken message = new PlayerToken(currentPlayer.getPlayerID(), bonusAction);
					//Retrasmit the message
					player.setBonusAction(bonusAction);
					message.setRetrasmission();
					player.retrasmitMessage(message);
				}
				else {
					
					//Control if there is a discount
					if(bonusAction != null)
						action.addDiscount(bonusAction.getDiscount());
					
					Response response = action.checkAction();
					
					if(response == Response.CANNOT_PLAY) {
						//Cancel the timer
						timerTable.remove(player).cancel();
						
						//If player can't play, end his action, and move he to the end of actionOrder array
						player.setCanPlay(true);
						actionOrder.remove(actionOrder.indexOf(player));
						actionOrder.add(player);
					}
					else if(response == Response.FAILURE || response == Response.LOW_LEVEL) {
						PlayerToken message = new PlayerToken(player.getPlayerID());
						message.setRetrasmission();
						player.retrasmitMessage(message);
					}
					else if(currentAction == null){
						//Cancel the timer
						timerTable.remove(player).cancel();
						
						currentAction = action;
						checkRequest();
					}
				}
			}
		} catch (ElementNotFoundException e) {
			logger.error("Unable to find the player in handleAction");
			logger.info(e);
		}
    }
    
    private void checkRequest() {
    	
    	if(timerTable.containsKey(currentPlayer))
    		timerTable.remove(currentPlayer).cancel();
    	
    	if(currentPlayer.hasRequestToAnswer()) {
    		
    		if(!currentPlayer.isRequestsEmpty()) {
        		playersWithRequest.add(currentPlayer);
        		currentPlayer.askRequest(currentPlayer.removeRequest());
        	}
    		

        	if(!currentPlayer.isCouncilRequestsEmpty()) {
        		playersWithRequest.add(currentPlayer);
        		currentPlayer.askCouncilRequest(currentPlayer.removeCouncilRequest());
        	}
        
            //Set the timer
            Timer timer = new Timer();
            timer.schedule(new PlayerMoveTimer(currentPlayer, this), TIMER_SECONDS * 1000);
            timerTable.put(currentPlayer, timer);
    	}
    	else if(currentAction != null)
    		doAction();
    	else if(currentRound != 6)
    		finishAction();
    	else
    		calculateWinner();
    }
    
    private synchronized void doAction() {
    	try {
			currentAction.doAction();
			this.currentAction = null;
			
			checkRequest();
		} catch (FamiliarInWrongPosition e) {
			logger.debug("[DEBUG]: familiar in wrong position in gamelogic");
			logger.info(e);
		}
    }
    
    private void finishAction() {
    	bonusAction = currentPlayer.getBonusAction();
    	currentPlayer.synchResource();
    	
    	if(bonusAction != null)
    		currentPlayer.askMove();
    	else {
    		currentPlayer = null;
    		initAction();
    	}
    }
    
    public void handleRequest(CardRequest request) {
    	try {
			Player player = searchPlayer(request.getPlayerID());
			
			if(player == currentPlayer && playersWithRequest.contains(player)) {
				
				if(request.getChoice() < request.showChoice().size()) {
					//If there is an action, add the request to the player
					//else apply directly the request
					if(this.currentAction != null) {
						player.addRequest(request);
					}
					else {
						request.apply(player);
						player.synchResource();
					}
					playersWithRequest.remove(player);
					checkRequest();
				}
				else {
					//In case the player hasn't insert the correct information
					request.setRetrasmission();
					player.retrasmitMessage(request);
				}
			}
		} catch (ElementNotFoundException e) {
			logger.error("Unable to find the player to satisfy the requests");
			logger.info(e);
		}
    }
    
	public void handleCouncilRequest(CouncilRequest councilRequest){
		/* Method called by the Visitor to set a council request response to a Player:
		 * if something wrong retransmit the message, else add the request to the player council request
		 */
		try {
			Player player = searchPlayer(councilRequest.getPlayerID());
			
			if(player == currentPlayer && playersWithRequest.contains(player)) {
				councilRequest.apply(player);
				player.synchResource();
				
				playersWithRequest.remove(player);
			}
		} catch(ElementNotFoundException e) {
			logger.error("Unable to find the player to satisfy the council requests");
			logger.info(e);
		}
	}	
	
	public Table getTable() {
		return this.table;
	}
	
	public int getBonusActionValue() {
		if(this.bonusAction != null)
			return this.bonusAction.getLevel();
		return 0;
	}

	public void HandleLeaderUpdate(Player searchPlayer, LeaderCard card) {
		searchPlayer.enableLeaderCard(card);
		checkOtherPlayerLeaderRequest(searchPlayer);
	}
	
	public Player getCurrentPlayer() {
		return this.currentPlayer;
	}
	
	public boolean isThereAnAction() {
		return this.currentAction != null;
	}
	
	public void rollBackTakeCardAction() {
		if(currentAction != null && currentAction instanceof TakeCardAction) {
			TakeCardAction action = (TakeCardAction) currentAction;
			action.rollBackAction();
		}
	}
	
	public int getCurrentRound() {
		return this.currentRound;
	}
	
	public void removePlayerFromPendingRequest(Player player) {
		while(playersWithRequest.contains(player))
			playersWithRequest.remove(player);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		Message message;
		if(arg1 instanceof Message) {
			message = (Message) arg1;
			message.accept(controllerVisitor);
		}
	}
}
