package it.polimi.ingsw.ps42.view;

import java.awt.image.BufferedImage;

import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;

public interface TableInterface {

	public boolean handleEvent(int x, int y, BufferedImage image);
	public void askIncrement(ActionType type, int position, FamiliarColor color);
}
