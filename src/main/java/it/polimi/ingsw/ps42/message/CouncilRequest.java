package it.polimi.ingsw.ps42.message;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.exception.WrongChoiceException;
import it.polimi.ingsw.ps42.model.player.Player;

/**
 * Request used to know which council the player wants
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class CouncilRequest extends Message {
	//Request used to know what kind of council player wants
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8759529288193414560L;
	private List<Obtain> choice;
	private final int quantity;
	private final List<Obtain> possibleChoice;
	
	/**
	 * Constructor of the message
	 * 
	 * @param playerID			The interested player
	 * @param possibleChoice	The array of available council obtain
	 * @param quantity			The quantity of this council obtain
	 */
	public CouncilRequest(String playerID, List<Obtain> possibleChoice, int quantity) {
		super(playerID);
		this.possibleChoice=possibleChoice;
		this.quantity=quantity;
		choice=new ArrayList<>();
	}
	
	/**
	 * 
	 * @return	The available councils privileges
	 */
	public List<Obtain> getPossibleChoice() {
		return possibleChoice;
	}
	
	/**
	 * 
	 * @return The quantity of this privileges
	 */
	public int getQuantity() {
		return quantity;
	}
	
	/**
	 * Method used to set the player choice
	 * 
	 * @param index						The index of the choice in the array
	 * @throws WrongChoiceException		Thrown if there is a wrong choice
	 */
	public void addChoice(int index) throws WrongChoiceException {
		//Adds a choice only if this is present in the possibleChoice ArrayList
		if(validChoice(index)){
			this.choice.add(possibleChoice.remove(index));
		}
		else throw new WrongChoiceException("The choice for this request is not valid");
		
	}
	
	/**
	 * Private method used to check the validity
	 * 
	 * @param choice	The choice to control
	 * @return			True if the choice is valid, otherwise False
	 */
	private boolean validChoice(int choice){
		//Checks if the choice made is a valid index
		return possibleChoice.size()>choice;
	}
	
	
	/**
	 * Method called by the Controller to apply the request
	 * 
	 * @param player	The interested player
	 * @return			The result of the appliance
	 */
	public boolean apply(Player player) {
		
		if(choice.size()==quantity){
			for (Obtain obtain : choice) {
				obtain.enableEffect(player);
			}
			return true;
		}
		else return false;
	}
	
	/**
	 * Method used to visit the message
	 */
	@Override
	public void accept(Visitor v) {
		//Method used to start the visit
		v.visit(this);
	}

		
}
