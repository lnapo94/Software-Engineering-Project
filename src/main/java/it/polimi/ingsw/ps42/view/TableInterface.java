package it.polimi.ingsw.ps42.view;

import it.polimi.ingsw.ps42.view.GUI.DraggableComponent;

/**
 * Interface given to special component that needs to interact with the View
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public interface TableInterface {

	/**
	 * Method to handle a mouse released event in the View
	 * @param x the x position of the mouse on the screen
	 * @param y the y position of the mouse on the screen
	 * @param familiarMoving the familiar that was moving
	 * @param color the color of the familiar that was moving
	 * @return true if is a valid position to leave the Familiar
	 */
	public boolean handleEvent(int x, int y, DraggableComponent familiarMoving);
}
