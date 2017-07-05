package it.polimi.ingsw.ps42.model.position;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.exception.ElementNotFoundException;
import it.polimi.ingsw.ps42.model.exception.FamiliarInWrongPosition;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;

/**
 * This is the abstract class for the position, familiar will be set by the game logic when necessary.
 * A Position has a Type that represent the kind of Action that can be performed while in it; 
 * a position bonus that is activated when a Familiar is placed in it;
 * a malus that is subtracted to the familiar level when a special situation occurs;
 * a level that represent the least value of the action that can be performed on it.
 * 
 * @author Luca Napoletano, Claudio Montanari
 */
public abstract class Position {

	private ActionType type;
	private Familiar familiar;
	private Obtain bonus;
	private int malus;
	private int level;
	
	protected transient Logger logger;
	
	/**
	 * Default Constructor that sets the Position Logger
	 */
	public Position() {
		logger = Logger.getLogger(Position.class);
	}
	
	/**
	 * Constructor for a general Position
	 * @param type the action that can be performed on the Position
	 * @param level the level required to place the Familiar
	 * @param bonus the bonus obtained when a familiar is placed
	 * @param malus the malus payed by the Player when there is another familiar of the same player or another player familiar 
	 * in the same type of position
	 */
	public Position(ActionType type, int level, Obtain bonus, int malus){
		this();
		this.type=type;
		this.level=level;
		this.bonus=bonus;
		this.malus=malus;	
	}
	
	/**
	 * Getter for the Position Bonus
	 * @return the Position Bonus, if not null
	 */
	public Obtain getBonus() {
		//Clone of the position bonus to avoid problem from position clone in the setup
		if(bonus != null) 
			return new Obtain(bonus.getCosts(), bonus.getGains(), bonus.getCouncilObtain());
		
		logger = Logger.getLogger(Position.class);
		logger.info("Position has not a bonus");
		return null;
	}
	
	/**
	 * Getter for the Position malus
	 * @return the position malus
	 */
	public int getMalus() {
		return malus;
	}
	
	/**
	 * Getter for the Familiar in the Position
	 * @return the Familiar in the Position
	 */
	public Familiar getFamiliar() {
		return familiar;
	}
	
	/**
	 * Getter for the Position level
	 * @return the level of the Position
	 */
	public int getLevel() {
		return level;
	}
	
	/**
	 * Getter for the position type
	 * @return the type of the Position
	 */
	public ActionType getType() {
		return type;
	}
	
	/**
	 * Setter for the Familiar in the Position, also checks if the Player can set the Familiar 
	 * @param familiar the Familiar to set in the Position
	 * @throws FamiliarInWrongPosition if the FAmiliar does not satisfy the position pre-requisites
	 */
	public void setFamiliar(Familiar familiar) throws FamiliarInWrongPosition{	//Invoked when the player move the familiar in this very position 
		if( familiar != null ){
			applyPositionEffect(familiar);
			this.familiar=familiar;
			familiar.setPosition(this);
		}
		else 
			throw new FamiliarInWrongPosition("The familiar does not satisfy the position pre-requisites");
	}
	
	/**
	 * Setter for the Familiar, does not check if the Player can place in the Position the passed Familiar
	 * @param familiar the Familiar to set in the Position
	 * @throws ElementNotFoundException if the Familiar passed is null
	 */
	public void setFamiliarView(Familiar familiar) throws ElementNotFoundException{
		
		if(familiar != null)
			this.familiar = familiar;
		else throw new ElementNotFoundException("Null Familiar");
	}
	
	/**
	 * Method used to remove the Familiar from the Position, also reset the Familiar increment
	 */
	public void  removeFamiliar(){
		//Have to be invoked to remove the familiar when the round ends
		if(familiar != null)
			this.familiar.resetIncrement();
		this.familiar=null;
	}
	
	/**
	 * Method used to check if the Position is without a Familiar
	 * @return true if the Familiar is null
	 */
	public boolean isEmpty(){			//Checks if there is a familiar in the position
		return this.familiar == null;		
	}
	
	/**
	 * Method used to apply the Position Bonus
	 * @param familiar the Familiar to apply the Effect
	 */
	protected void applyPositionEffect(Familiar familiar){
		
		familiar.setIncrement(-malus);
		if(bonus!=null){
			bonus.enableEffect(familiar.getPlayer()); 		//enables the position bonus on the player
	
		}
	}
	
	/**
	 * Method used to check if the Familiar satisfy the level requisites
	 * @param familiar the Familiar to check 
	 * @return true if the Familiar values minus the malus and the Position level is positive
	 */
	protected boolean canStay(Familiar familiar){
		return (familiar.getValue()-this.malus)>=this.level;
	}
	
	/**
	 * Method used to reset the Bonus from the Position
	 * @param player the Player that needs to reset from the Bonus
	 */
	public void resetBonus(Player player) {
		Packet packet = bonus.getGains();
		try {
			player.decreaseResource(packet);
		} catch (NotEnoughResourcesException e) {
			logger = Logger.getLogger(Position.class);
			logger.error("Error in reset bonus...");
			logger.info(e);
		}
	}
	
}
