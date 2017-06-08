package it.polimi.ingsw.ps42.message;

import java.util.List;

import it.polimi.ingsw.ps42.model.Printable;

public interface RequestInterface {
	//Common Interface for all the requests
	
	//Apply the user's choice
	public void apply();
	
	//Show possible choice
	public List<Printable> showChoice();
	
	//Set user's choice
	public void setChoice(int choice);
}
