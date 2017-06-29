package it.polimi.ingsw.ps42.view;


import java.awt.image.BufferedImage;

import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.view.GUI.DraggableComponent;

public interface TableInterface {

	public boolean handleEvent(int x, int y, BufferedImage image, FamiliarColor color);
}
